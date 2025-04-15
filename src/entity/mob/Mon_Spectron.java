package entity.mob;

import entity.Actable;
import entity.effect.type.Slow;
import entity.projectile.Proj_BasicGreenProjectile;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import map.GameMap;
import util.KeyPair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;

import static main.GamePanel.camera;

/*
Mô tả:
+Di chuyển: Di chuyển một cách ngẫu nhiên trên bản đồ
+Tấn công: Tấn công một cách ngẫu nhiên
+Kĩ năng: Có thể bay qua các vật thể một cách dễ dàng
*/

public class Mon_Spectron extends Monster implements Actable {
    private static final HashMap<KeyPair<SpectronState, Direction>, Sprite> spectronSpritePool = new HashMap<>();
    private static final HashMap<KeyPair<SpectronState, Direction>, Animation> spectronAnimations = new HashMap<>();

    public static void load(){
        for(SpectronState state: SpectronState.values()){
            int speed = switch (state){
                case RUN,IDLE, DIE -> 10;
                case SHOOT -> 20;
            };

            boolean loop = switch (state){
                case RUN, IDLE -> true;
                case SHOOT, DIE -> false;
            };
            for(Direction direction: Direction.values()) {
                int row = switch (direction){
                    case RIGHT -> 0;
                    case LEFT -> 1;
                };

                KeyPair<SpectronState, Direction> key = new KeyPair<>(state, direction);
                spectronSpritePool.put(key,
                             new Sprite(AssetPool.getImage("spectron_" + state.name().toLowerCase() + ".png")));
                spectronAnimations.put(key,
                        new Animation(spectronSpritePool.get(key).getSpriteArrayRow(row), speed, loop));
            }
        }
    }

    private SpectronState currentState;
    private SpectronState lastState;
    private Direction currentDirection;
    private Direction lastDirection;
    private Animation currentAnimation;

    private void setState(){
        boolean check1 = false;
        boolean check2 = false;
        if(lastState != currentState){
            lastState = currentState;
            check1 = true;
        }

        if(lastDirection != currentDirection){
            lastDirection = currentDirection;
            check2 = true;
        }

        if(check1 || check2)
            currentAnimation = spectronAnimations.get(new KeyPair<>(currentState, currentDirection)).clone();
    }


    private int actionLockCounter = 0;
    private int attackLockCounter = 0;
    private int changeDirCounter = 120;
    private int shootInterval = 90;

    private int lastHP;

    public Mon_Spectron(GameMap mp , int x , int y , String idName)
    {
        super(mp , x , y);
        name = "Spectron";
        this.idName = idName;
        this.mp = mp;
        super.width = 64;
        super.height = 64;
        this.canbeDestroyed = false;
        onPath = false;

        set();
    }

    private void setDefault()
    {
        projectile = new Proj_BasicGreenProjectile(mp);
        invincibleDuration = 40;
        maxHP = 40;
        currentHP = maxHP;
        lastHP = currentHP;
        strength = 10;
        speed = 1;
        last_speed = speed;
        effectDealOnTouch = new Slow(mp.player , 60);
        effectDealByProjectile = new Slow(mp.player , 180);

        expDrop = 10;

        solidArea1 = new Rectangle(20 , 19 , 26 , 20);
        hitbox = new Rectangle(20 , 8 , 27 , 32);
        //solidArea2 = new Rectangle(0 , 0 , 0 ,0);
        super.setDefaultSolidArea();

        up = down = left = right = false;
        isRunning = false;
        direction = "right";

        currentDirection = Direction.RIGHT;
        lastDirection = Direction.RIGHT;
        currentState = SpectronState.IDLE;
        lastState = SpectronState.IDLE;

        currentAnimation = spectronAnimations.get(new KeyPair<>(currentState, currentDirection)).clone();
    }

    public void set() {
        //getImage();
        setDefault();
    }

    public void setDialogue() {

    }

    public void loot() {
        spawnHeart();
    }

    private void changeDirection()
    {
        switch(direction)
        {
            case "left" : currentDirection = Direction.LEFT; break;
            case "right": currentDirection = Direction.RIGHT; break;
        }
    }

    private void handleAnimation()
    {
        if(isRunning && !isDying) {
            currentState = SpectronState.RUN;
        } else
        if(isShooting && !isDying){
            currentState = SpectronState.SHOOT;
        } else
        if(isDying) {
            currentState = SpectronState.DIE;
        } else
        {
            currentState = SpectronState.IDLE;
        }

        setState();

        if(currentAnimation.isFinished() && isShooting){
            attack();
            isShooting = false;
        }
        if(currentAnimation.isFinished() && isDying){
            isDying = false;
            canbeDestroyed = true;
            loot();
        }

    }

    private void setAction()
    {
        //SPEED
        //MOVE
        actionLockCounter++;
        if (actionLockCounter >= changeDirCounter && !isDying && !isShooting) {
            up = down = left = right = false;
            Random random = new Random();
            int i = random.nextInt(100) + 1;  // pick up  a number from 1 to 100
            if (i <= 28) {
                direction = "up";
                up = true;
            }
            if (i > 28 && i <= 50) {
                direction = "down";
                down = true;
            }
            if (i > 50 && i <= 75) {
                direction = "left";
                left = true;
            }
            if (i > 75 && i < 100) {
                direction = "right";
                right = true;
            }
            actionLockCounter = 0; // reset
            isRunning = !isShooting && (right | left | up | down);
        }

        //ATTACK
        damagePlayer();
        if(lastHP > currentHP){
            lastHP = currentHP;
            reactForDamage();
            isShooting = true;
            isRunning = false;
            if(isDying) isShooting = false;
        } else {
            if(isShooting && isDying) isShooting = false;
            attackLockCounter++;
            if (attackLockCounter >= shootInterval && !isDying) {
                Random gen = new Random();
                int i = gen.nextInt(100) + 1;
                if (i >= 75 && i < 100) {
                    isShooting = true;
                    isRunning = false;
                }
                attackLockCounter = 0;
            }
        }

        //INVINCIBLE
        updateInvincibility();
    }

    public void attack() {
        projectile.set(worldX , worldY , direction , true , this);
        mp.addObject(projectile , mp.projectiles);
    }


    public void move(){
        collisionOn = false;
        if(up && isRunning && !isDying) newWorldY = worldY - speed;
        if(down && isRunning && !isDying) newWorldY = worldY + speed;
        if(left && isRunning && !isDying) newWorldX = worldX - speed;
        if(right && isRunning && !isDying) newWorldX = worldX + speed;

        mp.cChecker.checkCollisionWithEntity(this , mp.inactiveObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.activeObj);
        mp.cChecker.checkCollisionPlayer(this);

        if(!collisionOn)
        {
            worldX = newWorldX;
            worldY = newWorldY;
        }
        newWorldX = worldX;
        newWorldY = worldY;
    }


    @Override
    public void update() throws NullPointerException{
        setAction();
        move();
        changeDirection();
        handleAnimation();
        currentAnimation.update();
    }

    @Override
    public void render(Graphics2D g2) {
        super.renderHPBar(g2 , 18 , 0);
        if(isInvincible && !isDying){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 0.3f));
        }
        currentAnimation.render(g2, worldX - camera.getX(), worldY - camera.getY());
        //g2.drawImage(mon_spectron[CURRENT_ACTION][CURRENT_DIRECTION][CURRENT_FRAME] , worldX - camera.getX() , worldY - camera.getY() , width , height , null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1.0f));
    }

    public void dispose(){
        projectile = null;
        projectile_name = null;
        effectDealByProjectile = null;
        effectDealOnTouch = null;
    }

    private enum SpectronState{
        IDLE, RUN, SHOOT, DIE
    }
}
