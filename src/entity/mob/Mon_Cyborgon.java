package entity.mob;

import entity.Actable;
import entity.effect.type.EffectNone;
import entity.projectile.Proj_GreenBullet;
import level.progress.level02.EventHandler02;
import level.progress.level02.Level02;
import graphics.Animation;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static main.GamePanel.camera;
import static main.GamePanel.currentMap;
import static map.GameMap.childNodeSize;

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

    private int diameter = 100;
    private int newDiameter = diameter;
    private float alpha = 0.1f;
    private boolean increasing = true;
    private int exposureTime = 0;

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

    public Mon_Cyborgon(GameMap mp , int x , int y , String idName){
        super(mp , x , y);
        name = "Cyborgon";
        width = 64;
        height = 64;
        state = INACTIVE;

        getImage();
        setDefault();
        this.idName = idName;
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
        maxHP = 140;
        currentHP = maxHP;
        strength = 30;
        level = 1;
        defense = 0;
        projectile = new Proj_GreenBullet(mp);
        effectDealOnTouch = new EffectNone(mp.player);
        effectDealByProjectile = new EffectNone(mp.player);
        speed = 2;
        last_speed = speed;

        expDrop = 20;

        SHOOT_INTERVAL = projectile.maxHP + 5;

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
                isInvincible = true;
                mon_animator_cyborgon.playOnce();
            }
        }

        if(!mon_animator_cyborgon.isPlaying() && canChangeState){
            canChangeState = false;
            isInvincible = false;
            state = ACTIVE;
        }
    }

    private void setAction(){
        if(state == ACTIVE){
            if(!getAggro){
                //checkIfInRange();//Không đuôi theo người chơi
                actionWhenNeutral();
            } else {
                actionWhenGetAggro();//Đuổi theo người chơi
            }
            isRunning = (up | down | right | left) && (!isShooting);
            damagePlayer();
        }
    }

    private void actionWhenGetAggro() {
        int playerCol = (mp.player.worldX + mp.player.solidArea1.x) / childNodeSize;
        int playerRow = (mp.player.worldY + mp.player.solidArea1.y) / childNodeSize;

        searchPath(playerCol , playerRow);
        decideToMove();
        attack();
        isRunning = !isShooting;
    }

    private void actionWhenNeutral() {
        if (currentHP < 0.8 * maxHP) getAggro = true;
    }

    public void move() {
        collisionOn = false;
        if(up && isRunning && !isDying) newWorldY = worldY - speed; //Gì đây hả cđl
        if(down && isRunning && !isDying) newWorldY = worldY + speed;
        if(left && isRunning && !isDying) newWorldX = worldX - speed;
        if(right && isRunning && !isDying) newWorldX = worldX + speed;

        mp.cChecker.checkCollisionWithEntity(this , mp.inactiveObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.activeObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.npc);
        mp.cChecker.checkCollisionWithEntity(this , mp.enemy);
        mp.cChecker.checkCollisionPlayer(this);
        //if(mp.cChecker.checkInteractPlayer(this)) isInteracting = true;

        if(!collisionOn)
        {
            worldX = newWorldX;
            worldY = newWorldY;
        }

        newWorldX = worldX;
        newWorldY = worldY;
    }

    public void setDialogue() {

    }

    public void attack() {
        if(!projectile.active &&shootAvailableCounter == SHOOT_INTERVAL) {
            projectile.set(worldX, worldY, direction, true, this);
            projectile.setHitbox();
            projectile.setSolidArea();
            mp.addObject(projectile, mp.projectiles);
            shootAvailableCounter = 0;
        }
    }

    public void loot() {

    }

    @Override
    public void update(){
        setAction();
        handleStatus();
        updateDiameter(mp);
        changeAnimationDirection();
        move();
        handleAnimationState();
        updateInvincibility();
        mon_animator_cyborgon.update();
        CURRENT_FRAME = mon_animator_cyborgon.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2){

        //DRAW RADIATION CIRCLE
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.GREEN);
        g2.fillOval((worldX + width / 2 - diameter / 2) - camera.getX(), (worldY + height / 2 - diameter / 2) - camera.getY(), diameter, diameter);
        g2.setColor(Color.GREEN);
        g2.fillOval((worldX + width / 2 - diameter / 2) - camera.getX()-6, (worldY + height / 2 - diameter / 2) - camera.getY()-6, diameter+12, diameter+12);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        updateOpacity();

        if(isInvincible && !isDying){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 0.3f));
        }
        g2.drawImage(mon_cyborgon[CURRENT_ACTION][CURRENT_DIRECTION][CURRENT_FRAME] , worldX - camera.getX() , worldY - camera.getY() , width , height , null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1.0f));
    }

    private void updateOpacity() {
        if (increasing) {
            alpha += 0.001f;
            if (alpha >= 0.08f) {
                alpha = 0.08f;
                increasing = false;
            }
        } else {
            alpha -= 0.001f;
            if (alpha <= 0f) {
                alpha = 0f;
                increasing = true;
            }
        }
    }

    private void updateDiameter(GameMap mp) {
        newDiameter = 100 + (maxHP - currentHP) * 5;
        if (diameter != newDiameter) diameter++;
        if (Math.pow(worldX - mp.player.worldX, 2) + Math.pow(worldY - mp.player.worldY, 2) < diameter * diameter) {
            exposureTime++;
            if (exposureTime == 300) {
                mp.player.currentHP = (int) (0.9 * mp.player.currentHP);
                exposureTime = 0;
            }
        }

    }
}