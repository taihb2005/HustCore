package entity.mob;

import entity.Actable;
import entity.effect.type.Slow;
import entity.projectile.Proj_BasicGreenProjectile;
import graphics.Animation;
import graphics.Sprite;
import map.GameMap;
import entity.object.Obj_Heart;
import entity.object.Obj_Coin;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static main.GamePanel.camera;

/*
Mô tả:
+Di chuyển: Di chuyển một cách ngẫu nhiên trên bản đồ
+Tấn công: Tấn công một cách ngẫu nhiên
+Kĩ năng: Có thể bay qua các vật thể một cách dễ dàng
*/

public class Mon_Spectron extends Monster implements Actable {
    final private static int IDLE = 0;
    final private static int RUN = 1;
    final private static int SHOOT = 2;
    final private static int DIE = 3;

    final private static int RIGHT = 0;
    final private static int LEFT = 1;


    final private BufferedImage [][][] mon_spectron = new BufferedImage[4][][];
    final private Animation mon_animator_spectron = new Animation();

    private int actionLockCounter = 0;
    private int attackLockCounter = 0;
    private int changeDirCounter = 120;
    private int shootInterval = 120;

    private int lastHP;

    //DEMO
    private Obj_Heart heart;
    private Obj_Coin coin;

    public Mon_Spectron(GameMap mp)
    {
        super(mp);
        name = "Spectron";
        this.mp = mp;
        super.width = 64;
        super.height = 64;
        this.canbeDestroyed = false;
        onPath = false;

        set();
    }

    public Mon_Spectron(GameMap mp , int x , int y)
    {
        super(mp , x , y);
        name = "Spectron";
        this.mp = mp;
        super.width = 64;
        super.height = 64;
        this.canbeDestroyed = false;
        onPath = false;

        set();
    }

    private void getImage()
    {
        mon_spectron[IDLE]  = new Sprite("/entity/mob/spectron/spectron_idle.png" , width  , height).getSpriteArray();
        mon_spectron[RUN]   = new Sprite("/entity/mob/spectron/spectron_run.png"  , width  , height).getSpriteArray();
        mon_spectron[SHOOT] = new Sprite("/entity/mob/spectron/spectron_shoot.png" , width  , height).getSpriteArray();
        mon_spectron[DIE]   = new Sprite("/entity/mob/spectron/spectron_die.png" , width  , height).getSpriteArray();
    }

    private void setDefault()
    {
        projectile = new Proj_BasicGreenProjectile(mp);
        invincibleDuration = 40; // 1s
        maxHP = 40;
        currentHP = maxHP;
        lastHP = currentHP;
        strength = 10;
        speed = 1;
        last_speed = speed;
        effectDealOnTouch = new Slow(mp.player , 60);
        effectDealByProjectile = new Slow(mp.player , 180);

        expDrop = 10;

        solidArea1 = new Rectangle(20 , 19 , 26 , 15);
        hitbox = new Rectangle(20 , 8 , 27 , 32);
        //solidArea2 = new Rectangle(0 , 0 , 0 ,0);
        super.setDefaultSolidArea();

        up = down = left = right = false;
        isRunning = false;
        direction = "right";
        CURRENT_DIRECTION = RIGHT;
        PREVIOUS_ACTION = IDLE;
        CURRENT_ACTION = IDLE;
        CURRENT_FRAME = 0;
        mon_animator_spectron.setAnimationState(mon_spectron[IDLE][RIGHT] , 10);
    }

    public void set() {
        getImage();
        setDefault();
    }

    public void setDialogue() {

    }

    public void loot() {
        spawnHeart();
        spawnCoin();
    }

    private void changeAnimationDirection()
    {
        switch(direction)
        {
            case "left" : CURRENT_DIRECTION = LEFT; break;
            case "right": CURRENT_DIRECTION = RIGHT; break;
        }
    }

    private void handleAnimationState()
    {
        if(isRunning && !isDying) {
            isIdle = false;
            CURRENT_ACTION = RUN;
        }
        else
        if(isShooting && !isDying){
            isIdle = false;
            CURRENT_ACTION = SHOOT;
        } else
        if(isDying) {
            isIdle = false;
            CURRENT_ACTION = DIE;
        } else
        {
            isIdle = true;
            CURRENT_ACTION = IDLE;
        }

        if(PREVIOUS_ACTION != CURRENT_ACTION)
        {
            PREVIOUS_ACTION = CURRENT_ACTION;
            if(isRunning) mon_animator_spectron.setAnimationState(mon_spectron[RUN][CURRENT_DIRECTION] , 10);
            if(isDying){
                mon_animator_spectron.setAnimationState(mon_spectron[DIE][CURRENT_DIRECTION] , 10);
                mon_animator_spectron.playOnce();
            }
            if(isShooting){
                mon_animator_spectron.setAnimationState(mon_spectron[SHOOT][CURRENT_DIRECTION] , 20);
                mon_animator_spectron.playOnce();
            }
            if(isIdle){
                mon_animator_spectron.setAnimationState(mon_spectron[IDLE][CURRENT_DIRECTION] , 10);
            }
        }

        if(!mon_animator_spectron.isPlaying() && isShooting){
            attack();
            isShooting = false;
        }
        if(!mon_animator_spectron.isPlaying() && isDying){
            isDying = false;
            canbeDestroyed = true;
            loot();
        }

    }

    private void setAction()
    {
        //SPEED
        //MOVE
        if(onPath){
            int playerCol = (mp.player.worldX + mp.player.solidArea1.x) / 16;
            int playerRow = (mp.player.worldY + mp.player.solidArea1.y) / 16;
            up = down = left = right = false;
            searchPath(playerCol , playerRow);
            decideToMove();
            isRunning = (up | down | left | right) && !isShooting;
        } else {
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
                isRunning = !isShooting;
            }
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


    public void move() {
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

    private void spawnHeart() {
        heart = new Obj_Heart(mp);
        heart.worldX = worldX + 16; heart.worldY = worldY + 16;
        mp.addObject(heart , mp.activeObj);
    }

    private void spawnCoin() {
        coin = new Obj_Coin(mp);
        coin.worldX = worldX; coin.worldY = worldY + 30;
        mp.addObject(coin , mp.activeObj);
    }


    @Override
    public void update() {
        setAction();
        move();
        changeAnimationDirection();
        handleAnimationState();
        mon_animator_spectron.update();
        CURRENT_FRAME = mon_animator_spectron.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) {
        if(isInvincible && !isDying){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 0.3f));
        }
        g2.drawImage(mon_spectron[CURRENT_ACTION][CURRENT_DIRECTION][CURRENT_FRAME] , worldX - camera.getX() , worldY - camera.getY() , width , height , null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1.0f));
    }
}
