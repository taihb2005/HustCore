package entity.mob;

import entity.Actable;
import entity.effect.type.EffectNone;
import entity.projectile.Proj_GuardianProjectile;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import map.GameMap;
import util.KeyPair;
import util.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;

import static main.GamePanel.*;
import static map.GameMap.childNodeSize;

public class Mon_HustGuardian extends Monster implements Actable {
    private static final HashMap<KeyPair<GuardianState, Direction>, Sprite> guardianSpritePool = new HashMap<>();
    private static final HashMap<KeyPair<GuardianState, Direction>, Animation> guardianAnimations = new HashMap<>();

    public static void load(){
        for(GuardianState state: GuardianState.values()){
            int speed = switch (state){
                case IDLE, RUN, DIE -> 10;
                case SHOOT -> 6;
            };

            boolean loop = switch (state){
                case IDLE, RUN -> true;
                case SHOOT, DIE -> false;
            };
            for(Direction direction: Direction.values()) {
                int row = switch (direction){
                    case RIGHT -> 0;
                    case LEFT -> 1;
                };

                KeyPair<GuardianState, Direction> key = new KeyPair<>(state, direction);

                guardianSpritePool.put(key,
                        new Sprite(AssetPool.getImage("hust_guardian_" + state.name().toLowerCase() + ".png"))
                );
                guardianAnimations.put(key,
                        new Animation(guardianSpritePool.get(key).getSpriteArrayRow(row), speed, loop));
            }
        }

        exclamation = AssetPool.getImage("exclamation_mark.png");
    }

    private GuardianState currentState;
    private GuardianState lastState;
    private Direction currentDirection;
    private Direction lastDirection;

    private void setState(){
        boolean change1 = false;
        boolean change2 = false;
        if(lastState != currentState){
            lastState = currentState;
            change1 = true;
        }

        if(lastDirection != currentDirection){
            lastDirection = currentDirection;
            change2 = true;
        }

        if(change1 || change2) {
            currentAnimation.reset();
            currentAnimation = guardianAnimations.get(new KeyPair<>(currentState, currentDirection)).clone();
        }
    }

    private static BufferedImage exclamation;
    private int actionLockCounter = 0;
    private final int changeDirCounter = 240;

    private int detectionCounter = 0;
    private final int detectionToSetAggro = 180;

    public Mon_HustGuardian(GameMap mp, String idName, int x , int y){
        super(mp , x , y);
        name = "Hust Guardian";
        width = 64;
        height = 64;
        speed = 1;
        lastSpeed = 1;

        setDefault();
        this.idName = idName;
    }

    private void setDefault(){
        hitbox = new Rectangle(22 , 24 , 22 , 36);
        solidArea1 = new Rectangle(24 , 42 , 19 , 18);
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        interactionDetectionArea = new Rectangle(-50 , -50 , width + 100 , height + 100);
        setDefaultSolidArea();

        invincibleDuration = 40;
        maxHP = 200;
        currentHP = maxHP;
        strength = 80;
        level = 1;
        defense = 10;
        projectile = new Proj_GuardianProjectile(mp);
        effectDealOnTouch = new EffectNone(mp.player);
        effectDealByProjectile = new EffectNone(mp.player);

        SHOOT_INTERVAL = projectile.maxHP + 20;
        expDrop = 30;

        direction = "right";
        currentDirection = Direction.RIGHT;

        currentState = GuardianState.IDLE;
        lastState = GuardianState.IDLE;
        currentAnimation = guardianAnimations.get(new KeyPair<>(currentState, currentDirection)).clone();
    }

    private void handleAnimation() {
        if (isDying) {
            currentState = GuardianState.DIE;
        } else if (isShooting) {
            currentState = GuardianState.SHOOT;
        } else if (isRunning) {
            currentState = GuardianState.RUN;
        } else {
            currentState = GuardianState.IDLE;
        }

        changeDirection();

        setState();

        if (currentAnimation.isFinished()) {
            if (currentState == GuardianState.SHOOT) {
                isShooting = false;
            }
            if (currentState == GuardianState.DIE) {
                isDying = false;
                canbeDestroyed = true;
                loot();
            }
        }
    }


    private void changeDirection(){
        switch(direction)
        {
            case "left" : currentDirection = Direction.LEFT; break;
            case "right": currentDirection = Direction.RIGHT; break;
        }
    }

    private void setAction(){
        if(!getAggro){
            actionWhenNeutral();
        } else {
            actionWhenGetAggro();
        }
        isRunning = (up | down | right | left) & (!isShooting);
        damagePlayer();
        updateHP();
    }

    private void actionWhenNeutral(){
        actionLockCounter++;
        if (actionLockCounter >= changeDirCounter && !isDying && !isShooting) {
            up = down = left = right = false;
            Random random = new Random();
            int i = random.nextInt(100) + 1;  // pick up  a number from 1 to 100
            if(i <= 60){
                up = down = left = right = false;
            }
            if ( i > 60 && i <= 70) {
                direction = "up";
                up = true;
            }
            if (i > 70 && i <= 80) {
                direction = "down";
                down = true;
            }
            if (i > 80 && i <= 90) {
                direction = "left";
                left = true;
            }
            if (i > 90 && i < 100) {
                direction = "right";
                right = true;
            }
            actionLockCounter = 0;
            isRunning = !isShooting && (right | left | up | down);
        }

        if(isInteracting){
            detectionCounter++;
            up = down = left = right = false;
            isRunning = false;
            isDetectPlayer = true;
            if(detectionCounter >= detectionToSetAggro){
                isDetectPlayer = false;
                detectionCounter = 0;
                speed = 2;
                getAggro = true;
            }
        } else{
            if(!getAggro)isDetectPlayer = false;
            detectionCounter = 0;
        }

        if(isDetectPlayer) facePlayer();

        isInteracting = false;
    }

    private void actionWhenGetAggro(){
        int playerCol = ((int)mp.player.position.x + mp.player.solidArea1.x) / childNodeSize;
        int playerRow = ((int)mp.player.position.y + mp.player.solidArea1.y) / childNodeSize;
        int posCol = ((int)position.x + solidArea1.x) / childNodeSize;
        int posRow = ((int)position.y + solidArea1.y) / childNodeSize;

        searchPath(playerCol , playerRow);
        decideToMove();

        boolean check3TilesAway = (Math.abs(playerCol - posCol) <= 12) || (Math.abs(playerRow - posRow) <= 12);
        boolean checkShootInterval = (shootAvailableCounter == SHOOT_INTERVAL);
        boolean checkIfConcurent = (Math.abs(playerCol - posCol) == 0) || (Math.abs(playerRow - posRow) == 0);
        if(check3TilesAway && checkShootInterval && checkIfConcurent){
            isShooting = true;
            if(posCol < playerCol) direction = "right"; else
            if(posCol >  playerCol) direction = "left"; else
            if(posRow < playerRow) direction = "down"; else
                direction = "up";
            attack();
        }
        isRunning = !isShooting;
    }

    public void move() {
        collisionOn = false;
        velocity = new Vector2D(0, 0);

        if (up && isRunning && !isDying) velocity = velocity.add(new Vector2D(0, -1));
        if (down && isRunning && !isDying) velocity = velocity.add(new Vector2D(0, 1));
        if (left && isRunning && !isDying) velocity = velocity.add(new Vector2D(-1, 0));
        if (right && isRunning && !isDying) velocity = velocity.add(new Vector2D(1, 0));

        if (velocity.length() != 0) {
            velocity = velocity.normalize().scale(speed);
        }

        newPosition = position.add(velocity);

        mp.cChecker.checkCollisionWithEntity(this , mp.inactiveObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.activeObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.npc);
        mp.cChecker.checkCollisionWithEntity(this , mp.enemy);
        mp.cChecker.checkCollisionPlayer(this);
        if(mp.cChecker.checkInteractPlayer(this)) isInteracting = true;

        if(!collisionOn)
        {
            position = newPosition.copy();
        } else {
            if(!onPath){
                if(checkForValidDirection()){
                    direction = getValidDirection();
                    decideToMove();
                    actionLockCounter = 0;
                }
            }
        }
        newPosition = position.copy();
    }

    public void setDialogue() {

    }

    private void updateHP(){
        if(currentHP <= 0) isDying = true;
    }

    public void attack() {
        if(!projectile.active &&shootAvailableCounter == SHOOT_INTERVAL) {
            projectile.set(position, direction, true, this);
            projectile.setHitbox();
            projectile.setSolidArea();
            mp.addObject(projectile, mp.projectiles);
            shootAvailableCounter = 0;
        }
    }

    public void loot() {
        spawnHeart();
    }

    @Override
    public void update() throws  NullPointerException{
        setAction();
        handleStatus();
        move();
        handleAnimation();
        currentAnimation.update();
    }

    @Override
    public void render(Graphics2D g2) {
        super.renderHPBar(g2 , 18 , 0);
        if(isInvincible && !isDying){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 0.3f));
        }
        super.render(g2);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1.0f));
        if(isDetectPlayer) g2.drawImage(exclamation , (int)position.x - camera.getX() + 54 , (int)position.y - camera.getY() , null);
    }

    public void dispose(){
        solidArea1 = null;
        solidArea2 = null;
        hitbox = null;
        interactionDetectionArea = null;
        exclamation = null;
        projectile = null;
        projectileName = null;
        effectDealByProjectile = null;
        effectDealOnTouch = null;
    }

    private enum GuardianState{
        IDLE, RUN, SHOOT, DIE
    }

}
