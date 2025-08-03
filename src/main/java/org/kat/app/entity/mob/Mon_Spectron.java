package org.kat.app.entity.mob;

import org.kat.app.entity.Actable;
import org.kat.app.entity.effect.type.Slow;
import org.kat.app.entity.projectile.Proj_BasicGreenProjectile;
import org.kat.app.graphics.Animation;
import org.kat.app.graphics.AssetPool;
import org.kat.app.graphics.Sprite;
import org.kat.app.map.GameMap;
import org.kat.app.util.GameTimer;
import org.kat.app.util.KeyPair;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

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

    private GameTimer moveTimer;
    private GameTimer shootTimer;

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

    public Mon_Spectron(GameMap mp, String idName, int x , int y)
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
        lastSpeed = speed;
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
        if(moveTimer == null){
            moveTimer = new GameTimer( () -> {
                if(!isDying && !isShooting){
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
                    isRunning = !isShooting && (right | left | up | down);
                }
            }, changeDirCounter);
        } else moveTimer.update();

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
            if(shootTimer == null){
                shootTimer = new GameTimer( () -> {
                    if(!isDying){
                        Random gen = new Random();
                        int i = gen.nextInt(100) + 1;
                        if (i >= 75 && i < 100) {
                            isShooting = true;
                            isRunning = false;
                        }
                    }
                }, shootInterval);
            } else shootTimer.update();
        }

        //INVINCIBLE
        updateInvincibility();
    }

    public void attack() {
        projectile.set(position, direction , true , this);
        mp.addObject(projectile , mp.projectiles);
    }


    public void move(){
        collisionOn = false;
        velocity.clear();

        if (up && isRunning && !isDying) velocity.add(UP_DIR);
        if (down && isRunning && !isDying) velocity.add(DOWN_DIR);
        if (left && isRunning && !isDying) velocity.add(LEFT_DIR);
        if (right && isRunning && !isDying) velocity.add(RIGHT_DIR);

        if (velocity.length() != 0) {
            velocity.normalize().scale(speed);
        }

        newPosition.set(position)
                .add(velocity);

        mp.cChecker.checkCollisionWithEntity(this , mp.inactiveObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.activeObj);
        mp.cChecker.checkCollisionPlayer(this);

        if(!collisionOn)
        {
            position.set(newPosition);
        }
        newPosition.set(position);
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
        super.render(g2);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1.0f));
    }

    public void dispose(){
        projectile = null;
        projectileName = null;
        effectDealByProjectile = null;
        effectDealOnTouch = null;

        super.dispose();
    }

    private enum SpectronState{
        IDLE, RUN, SHOOT, DIE
    }
}
