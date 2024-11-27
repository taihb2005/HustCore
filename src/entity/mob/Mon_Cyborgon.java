package entity.mob;

import entity.Actable;
import entity.effect.type.EffectNone;
import entity.projectile.Proj_GreenBullet;
import entity.projectile.Proj_GuardianProjectile;
import graphics.Animation;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static main.GamePanel.camera;
import static main.GamePanel.se;

public class Mon_Cyborgon extends Monster implements Actable {
    public final static int INACTIVE = 0;
    public final static int IDLE1 = 1;
    public final static int IDLE2 = 2;
    public final static int ACTIVE = 3;
    public final static int RUN = 4;
    public final static int SHOOT = 5;
    public final static int DIE = 6;

    public final static int RIGHT = 0;
    public final static int LEFT = 1;

    public int state;

    private final BufferedImage [][][] mon_cyborgon = new BufferedImage[7][][];
    private final Animation mon_animator_cyborgon = new Animation();

    private int actionLockCounter = 0;
    private int attackLockCounter = 0;
    private final int changeDirCounter = 120;
    private int shootInterval = 120;

    private int lastHP;

    public Mon_Cyborgon(GameMap mp){
        super(mp);
        name = "Cyborgon";
        width = 64;
        height = 64;
        state = INACTIVE;

        getImage();
        setDefault();
    }

    public Mon_Cyborgon(GameMap mp , int x , int y){
        super(mp , x , y);
        name = "Cyborgon";
        width = 64;
        height = 64;
        state = INACTIVE;

        getImage();
        setDefault();
    }

    private void getImage(){
        mon_cyborgon[INACTIVE] = new Sprite("/entity/mob/cyborgon/cyborgon_inactive.png" , 64 , 64).getSpriteArray();
        mon_cyborgon[IDLE1] = new Sprite("/entity/mob/cyborgon/cyborgon_idle1.png" , 64 , 64).getSpriteArray();
        mon_cyborgon[IDLE2] = new Sprite("/entity/mob/cyborgon/cyborgon_idle2.png" , 64 , 64).getSpriteArray();
        mon_cyborgon[ACTIVE] = new Sprite("/entity/mob/cyborgon/cyborgon_whenactive.png" , 64 , 64).getSpriteArray();
        mon_cyborgon[RUN] = new Sprite("/entity/mob/cyborgon/cyborgon_run.png" , 64 , 64).getSpriteArray();
        mon_cyborgon[SHOOT] = new Sprite("/entity/mob/cyborgon/cyborgon_shoot.png" , 64 , 64).getSpriteArray();
        mon_cyborgon[DIE] = new Sprite("/entity/mob/cyborgon/cyborgon_die.png" , 64 , 64).getSpriteArray();
    }
    private void setDefault(){
        hitbox = new Rectangle(22 , 24 , 22 , 36);
        solidArea1 = new Rectangle(18 , 40 , 28 , 22);
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        setDefaultSolidArea();

        invincibleDuration = 40;
        maxHP = 120;
        currentHP = maxHP;
        lastHP = currentHP;
        strength = 20;
        level = 1;
        defense = 0;
        projectile = new Proj_GreenBullet(mp);
        effectDealOnTouch = new EffectNone(mp.player);
        effectDealByProjectile = new EffectNone(mp.player);
        speed = 1;

        int dir = new Random().nextInt(2);
        if(dir == 0) {
            direction = "right";
            CURRENT_DIRECTION = RIGHT;
        } else {
            direction = "left";
            CURRENT_DIRECTION = LEFT;
        }

        CURRENT_ACTION = INACTIVE;
        PREVIOUS_ACTION = INACTIVE;
        mon_animator_cyborgon.setAnimationState(mon_cyborgon[INACTIVE][CURRENT_DIRECTION] , 120);
    }

    private void handleAnimationState(){
        if(state == INACTIVE)handleAnimationWhenInactive();
            else handleAnimationWhenActive();
    }

    private void changeAnimationDirection(){
        switch(direction)
        {
            case "left" : CURRENT_DIRECTION = LEFT; break;
            case "right": CURRENT_DIRECTION = RIGHT; break;
        }
    }

    private void handleAnimationWhenActive(){
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
            CURRENT_ACTION = IDLE1;
        }

        if(PREVIOUS_ACTION != CURRENT_ACTION)
        {
            PREVIOUS_ACTION = CURRENT_ACTION;
            if(isRunning) mon_animator_cyborgon.setAnimationState(mon_cyborgon[RUN][CURRENT_DIRECTION] , 11);
            if(isDying){
                mon_animator_cyborgon.setAnimationState(mon_cyborgon[DIE][CURRENT_DIRECTION] , 10);
                mon_animator_cyborgon.playOnce();
            }
            if(isShooting){
                mon_animator_cyborgon.setAnimationState(mon_cyborgon[SHOOT][CURRENT_DIRECTION] , 6);
                mon_animator_cyborgon.playOnce();
            }
            if(isIdle){
                mon_animator_cyborgon.setAnimationState(mon_cyborgon[IDLE1][CURRENT_DIRECTION] , 10);
            }
        }

        if(!mon_animator_cyborgon.isPlaying() && isShooting){
            isShooting = false;
        }
        if(!mon_animator_cyborgon.isPlaying() && isDying){
            isDying = false;
            canbeDestroyed = true;
        }
    }

    private void handleAnimationWhenInactive(){
        if(isIdle) CURRENT_ACTION = INACTIVE; else
            if(canChangeState) CURRENT_ACTION = ACTIVE;

        if(PREVIOUS_ACTION != CURRENT_ACTION){
            PREVIOUS_ACTION = CURRENT_ACTION;
            if(isIdle) mon_animator_cyborgon.setAnimationState(mon_cyborgon[INACTIVE][CURRENT_DIRECTION] , 120);
            if(canChangeState){
                mon_animator_cyborgon.setAnimationState(mon_cyborgon[ACTIVE][CURRENT_DIRECTION] , 17);
                mon_animator_cyborgon.playOnce();
            }
        }

        if(!mon_animator_cyborgon.isPlaying() && canChangeState){
            canChangeState = false;
            state = ACTIVE;
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
    
    public void move() {
        collisionOn = false;
        if(up && isRunning && !isDying) newWorldY = worldY - speed;
        if(down && isRunning && !isDying) newWorldY = worldY + speed;
        if(left && isRunning && !isDying) newWorldX = worldX - speed;
        if(right && isRunning && !isDying) newWorldX = worldX + speed;

        mp.cChecker.checkCollisionWithEntity(this , mp.inactiveObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.activeObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.npc);
        mp.cChecker.checkCollisionPlayer(this);
        //if(mp.cChecker.checkInteractPlayer(this)) isInteracting = true;

        if(!collisionOn)
        {
            speed = 2;
            worldX = newWorldX;
            worldY = newWorldY;
        }

        newWorldX = worldX;
        newWorldY = worldY;
    }

    public void setDialogue() {

    }

    public void attack() {
        projectile.set(worldX, worldY, direction, true, this);
        projectile.setHitbox();
        projectile.setSolidArea();
        mp.addObject(projectile, mp.projectiles);
        shootAvailableCounter = 0;
    }

    public void loot() {

    }

    @Override
    public void update(){
        setAction();
        move();
        changeAnimationDirection();
        handleAnimationState();
        updateInvincibility();
        mon_animator_cyborgon.update();
        CURRENT_FRAME = mon_animator_cyborgon.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2){
        if(isInvincible && !isDying){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 0.3f));
        }
        g2.drawImage(mon_cyborgon[CURRENT_ACTION][CURRENT_DIRECTION][CURRENT_FRAME] , worldX - camera.getX() , worldY - camera.getY() , width , height , null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1.0f));
    }
}
