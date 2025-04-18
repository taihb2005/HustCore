package entity.mob;

import ai.PathFinder2;
import entity.Actable;
import entity.effect.type.EffectNone;
import entity.projectile.Proj_ExplosivePlasma;
import entity.projectile.Proj_Flame;
import entity.projectile.Proj_TrackingPlasma;
import entity.projectile.Projectile;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import map.GameMap;
import util.KeyPair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import static main.GamePanel.*;
import static map.GameMap.childNodeSize;

public class Mon_Boss extends Monster implements Actable {

    private static final HashMap<KeyPair<BossState, Direction>, Sprite> bossSpritePool = new HashMap<>();
    private static final HashMap<KeyPair<BossState, Direction>, Animation> bossAnimations = new HashMap<>();

    public static void load(){
        for(BossState state: BossState.values()){
            int speed = switch (state){
                case IDLE, DIE -> 10;
                case RUN -> 7;
                case SHOOT1, SHOOT2, SHOOT3 -> 6;
                case TALK -> 10;
            };

            boolean loop = switch (state){
                case IDLE, RUN, TALK -> true;
                case SHOOT1, SHOOT2, SHOOT3, DIE -> false;
            };
            for(Direction direction: Direction.values()){
                int row = switch (direction){
                    case RIGHT -> 0;
                    case LEFT -> 1;
                };
                KeyPair<BossState, Direction> key = new KeyPair<>(state, direction);
                bossSpritePool.put(key,
                        new Sprite(AssetPool.getImage("boss_" + state.name().toLowerCase() + ".png"), 128, 128)
                );
                bossAnimations.put(key,
                        new Animation(bossSpritePool.get(key).getSpriteArrayRow(row), speed, loop)
                );
            }
        }
    }

    private int CURRENT_SKILL = 1;
    private Stage currentStage;
    public boolean isStage2;
    private PathFinder2 bossFinder;
    private BossState currentState;
    private BossState lastState;
    private Direction currentDirection;
    private Direction lastDirection;
    private Animation currentAnimation;

    private void setState(){
        boolean change1 = false;
        boolean change2 = false;

        if(lastState != currentState){
            lastState = currentState;
            change1 = true;
        }

        if(lastDirection != currentDirection){
            ;lastDirection = currentDirection;
            change2 = true;
        }

        if(change1 || change2){
            currentAnimation = bossAnimations.get(new KeyPair<>(currentState, currentDirection)).clone();
        }
    }

    private int actionLockCounter = 550;

    private int shootTimer = 0;

    private boolean nearlyDie = false;
    private boolean fullyDie = false;

    private Projectile projectile1, projectile2, projectile3;
    ArrayList<Projectile> proj;
    private int currentColumn = 1;
    private boolean isShooting1, isShooting2, isShooting3;
    private boolean shoot3;
    // Tổng số cột flame
    private boolean shooting = false;      // Trạng thái đang bắn flame
    private BufferedImage hpFrame;

    public Mon_Boss(GameMap mp , int x , int y){
        super(mp , x , y);
        name = "Boss";
        width = 128;
        height = 128;
        speed = 1;

        hpFrame = AssetPool.getImage("boss_hpFrame.png");

        //getImage();
        setDefault();
    }

    private void setDefault(){
        hitbox = new Rectangle(20 , 40 , 80 , 80);
        solidArea1 = new Rectangle(20 , 110 , 90 , 18);
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        setDefaultSolidArea();

        invincibleDuration = 30;
        maxHP = 1700;
        currentHP = maxHP;
        strength = 50;
        level = 1;
        defense = 10;
        projectile1 = new Proj_TrackingPlasma(mp);
        projectile2 = new Proj_ExplosivePlasma(mp);
        projectile3 = new Proj_Flame(mp);
        proj = new ArrayList<>();
        effectDealOnTouch = new EffectNone(mp.player);
        effectDealByProjectile = new EffectNone(mp.player);

        SHOOT_INTERVAL = 45;
        expDrop = 0;

        direction = "right";
        currentDirection = Direction.RIGHT;
        lastDirection = Direction.RIGHT;
        setDialogue();

        currentState = BossState.IDLE;
        lastState = BossState.IDLE;

        currentStage = Stage.STAGE1;

        currentAnimation = bossAnimations.get(new KeyPair<>(currentState, currentDirection)).clone();
    }

    private void changeDirection(){
        switch(direction)
        {
            case "left" : currentDirection = Direction.LEFT; break;
            case "right": currentDirection = Direction.RIGHT; break;
        }
    }

    private void setAction() {
        switch (CURRENT_SKILL) {
            case 1: actionWhileShoot1();
            actionLockCounter--;
            if (actionLockCounter == 0) {
                CURRENT_SKILL = 2;
                actionLockCounter = 100;
            }
            break;
            case 2: actionWhileShoot2();
            actionLockCounter--;
            if (actionLockCounter == 0) CURRENT_SKILL = 3;
            break;
            case 3: actionWhileShoot3();
            if (currentColumn >= 10) {
                CURRENT_SKILL = 1;
                actionLockCounter = 100;
                currentColumn = 1;
            }
            break;
        }

        if(isStage2){
            currentStage = Stage.STAGE2;
        }

        updateHP();
    }

    private void updateHP(){
        if (currentHP <= 0 && fullyDie) {
            this.startDialogue(this, 1);
            fullyDie = true;
        }
        else if (2*currentHP < maxHP && !nearlyDie) {
            this.startDialogue(this, 0);
            isStage2 = true;
        }
    }

    private void handleAnimation() {
        if (isDying) {
            currentState = BossState.DIE;
        } else if (isShooting1) {
            currentState = BossState.SHOOT1;
        } else if (isShooting2) {
            currentState = BossState.SHOOT2;
        } else if (shoot3) {
            currentState = BossState.SHOOT3;
        } else if (isRunning) {
            currentState = BossState.RUN;
        } else {
            currentState = BossState.IDLE;
        }

        changeDirection();
        setState();

        if (currentAnimation.isFinished()) {
            if (isShooting1) {
                isShooting1 = false;
                currentState = BossState.IDLE;
            }
            if (isShooting2) {
                isShooting2 = false;
            }
            if (shoot3) {
                shoot3 = false;
                isShooting3 = false;
            }
            if (isDying) {
                isDying = false;
                canbeDestroyed = true;
            }
        }

        updateInvincibility();
    }

    @Override
    public void move() {
        collisionOn = false;
        if(up && isRunning && !isDying) newWorldY = worldY - speed;
        else if(down && isRunning && !isDying) newWorldY = worldY + speed;
        else if(left && isRunning && !isDying) newWorldX = worldX - speed;
        else if(right && isRunning && !isDying) newWorldX = worldX + speed;

        mp.cChecker.checkCollisionWithEntity(this , mp.inactiveObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.activeObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.npc);
        mp.cChecker.checkCollisionWithEntity(this , mp.enemy);
        mp.cChecker.checkCollisionPlayer(this);
//        if(mp.cChecker.checkInteractPlayer(this)) isInteracting = true;

        if(!collisionOn)
        {
            worldX = newWorldX;
            worldY = newWorldY;
        }

        newWorldX = worldX;
        newWorldY = worldY;
    }

    @Override
    public void setDialogue() {
        this.dialogues[0][0] = new StringBuilder("Ngươi cũng mạnh phết đấy.");
        this.dialogues[0][1] = new StringBuilder("Xem ra ta phải nhờ đến sự trợ\ngiúp của thuộc hạ rồi.");
        this.dialogues[1][0] = new StringBuilder("Á hự... Không thể tin ngươi đã đánh bại được ta...");
        this.dialogues[1][1] = new StringBuilder("Huhuhu...");
    }

    @Override
    public void attack() {
    }

    @Override
    public void loot() {

    }

    public void searchPathForBoss(int goalCol, int goalRow) {
        executor.execute( () -> {
            int startCol = (worldX + solidArea1.x) / GameMap.childNodeSize;
            int startRow = (worldY + solidArea1.y) / GameMap.childNodeSize;
            synchronized (bossFinder) {
                bossFinder.setNodes(startCol, startRow, goalCol, goalRow);
                if (bossFinder.search()) {
                    //Next WorldX and WorldY
                    if (bossFinder.pathList.isEmpty()) {
                        onPath = false;
                        up = down = left = right = false;
                    } else {
                        int nextX = bossFinder.pathList.get(0).col * GameMap.childNodeSize;
                        int nextY = bossFinder.pathList.get(0).row * GameMap.childNodeSize;


                        //Entity's solidArea position
                        int enLeftX = worldX + solidArea1.x;
                        int enRightX = worldX + solidArea1.x + solidArea1.width;
                        int enTopY = worldY + solidArea1.y;
                        int enBottomY = worldY + solidArea1.y + solidArea1.height;

                        // TOP PATH
                        if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + GameMap.childNodeSize) {
                            direction = "up";
                        }
                        // BOTTOM PATH
                        else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + GameMap.childNodeSize) {
                            direction = "down";
                        }
                        // RIGHT - LEFT PATH
                        else if (enTopY >= nextY && enBottomY < nextY + GameMap.childNodeSize) {
                            //either left or right
                            // LEFT PATH
                            if (enLeftX > nextX) {
                                direction = "left";
                            }
                            // RIGHT PATH
                            if (enLeftX < nextX) {
                                direction = "right";
                            }
                        }
                        //OTHER EXCEPTIONS
                        else if (enTopY > nextY && enLeftX > nextX) {
                            // up or left
                            direction = "up";
                            newWorldY -= speed;
                            checkCollision();
                            //System.out.println(collisionOn);
                            if (collisionOn) {
                                direction = "left";
                            }
                            newWorldY += speed;
                        } else if (enTopY > nextY && enLeftX < nextX) {
                            // up or right
                            direction = "up";
                            newWorldY -= speed;
                            checkCollision();
                            if (collisionOn) {
                                direction = "right";
                            }
                            newWorldY += speed;
                        } else if (enTopY < nextY && enLeftX > nextX) {
                            // down or left
                            direction = "down";
                            newWorldY += speed;
                            checkCollision();
                            if (collisionOn) {
                                direction = "left";
                            }
                            newWorldY -= speed;
                        } else if (enTopY < nextY && enLeftX < nextX) {
                            // down or right
                            direction = "down";
                            newWorldY += speed;
                            checkCollision();
                            if (collisionOn) {
                                direction = "right";
                            }
                            newWorldY -= speed;
                        }
                    }
                } else {
                    getAggro = false;
                    onPath = false;
                    up = down = right = left = false;
                    isRunning = false;
                }
            }
        });
    }

    public void shoot1() {
        isShooting1 = !isDying;
        if(!projectile1.active && shootAvailableCounter == SHOOT_INTERVAL) {
            projectile1.set(worldX+25, worldY+12, direction, true, this);
            projectile1.setHitbox();
            projectile1.setSolidArea();
            mp.addObject(projectile1, mp.projectiles);
            shootAvailableCounter = 0;
        }
    }
    public void shoot2() {
        if(!projectile2.active && shootAvailableCounter == SHOOT_INTERVAL) {
            projectile2.set(worldX+78, worldY+60, direction, true, this);
            projectile2.setHitbox();
            projectile2.setSolidArea();
            mp.addObject(projectile2, mp.projectiles);
            shootAvailableCounter = 0;
        }
    }
    public void createFlameColumn(int isLeft) {
            shootAvailableCounter++;
            if (shootAvailableCounter >= 10) {
                for (int j = 1; j <= 5; j++) {
                    Projectile newFlame = new Proj_Flame(mp);
                    newFlame.set(worldX + 50 * isLeft* currentColumn+50, worldY + 50 * (j-1) - 41, direction, true, this);
                    newFlame.setHitbox();
                    newFlame.setSolidArea();
                    proj.add(newFlame);
                }
                currentColumn++;
                for(Projectile flame : proj) mp.addObject(flame , mp.projectiles);
                shootAvailableCounter = 0;
            }
    }

    public void actionWhileShoot1() {
        shoot1();
    }

    public void actionWhileShoot2() {
        int playerCol = (mp.player.worldX + mp.player.solidArea1.x) / childNodeSize;
        int playerRow = (mp.player.worldY + mp.player.solidArea1.y) / childNodeSize;
        int posCol = (worldX + solidArea1.x) / childNodeSize;
        int posRow = (worldY + solidArea1.y) / childNodeSize;
        int isLeft = (worldX < mp.player.worldX)?-1:1;
        searchPathForBoss(playerCol, playerRow);
        decideToMove();
        boolean check3TilesAway = (Math.abs(playerCol - posCol) <= 12) || (Math.abs(playerRow - posRow) <= 12);
        boolean checkShootInterval = (shootAvailableCounter == SHOOT_INTERVAL);
        boolean checkIfConcurrent = (Math.abs(playerCol - posCol) == 0) || (Math.abs(playerRow - posRow) == 0);
        if (check3TilesAway && checkShootInterval && checkIfConcurrent) {
            isShooting2 = true;
            if (Math.abs(worldX-mp.player.worldX) > Math.abs(worldY-mp.player.worldY)) {
                if(worldX < mp.player.worldX)  direction = "right";
                else if(worldX > mp.player.worldX) direction = "left";
            }
             else if(worldY < mp.player.worldY) direction = "down"; else
                direction = "up";
            shoot2();
        }
        isRunning = !isShooting2 && !isDying;
    }

    public void actionWhileShoot3() {
        if (!isShooting3) {
            shoot3 = true;
            isShooting3 = true;
            isRunning = false;
            shootTimer = 0;
            currentColumn = 1;
        }

        if (isShooting3) {
            shootTimer++;
            if (shootTimer >= 10) {
                int isLeft = direction.equals("left") ? -1 : 1;
                createFlameColumn(isLeft);
                shootTimer = 0;
            }

            if (currentColumn > 10) {
                currentColumn = 1;
                isShooting3 = false;
                shoot3 = false;
            }
        }
    }

    @Override
    public void update(){
        setAction();
        if(!proj.isEmpty())proj.removeIf(pr -> !pr.active);
        move();
        handleAnimation();
        currentAnimation.update();

        System.out.println(currentState);
    }

    @Override
    public void render(Graphics2D g2) {
        if (isInvincible && !isDying) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        currentAnimation.render(g2, worldX - camera.getX(), worldY - camera.getY());

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    public void setPathFinder(PathFinder2 finder){
        this.bossFinder = finder;
    }

    private enum Stage{
        STAGE1, STAGE2;
    }

    private enum BossState{
        IDLE, RUN, SHOOT1, SHOOT2, SHOOT3, TALK, DIE
    }
}
