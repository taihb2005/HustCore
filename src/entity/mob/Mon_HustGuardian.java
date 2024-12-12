package entity.mob;

import entity.Actable;
import entity.effect.type.EffectNone;
import entity.projectile.Proj_GuardianProjectile;
import graphics.Animation;
import graphics.Sprite;
import map.GameMap;
import util.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static main.GamePanel.*;
import static map.GameMap.childNodeSize;

public class Mon_HustGuardian extends Monster implements Actable {
    public final static int IDLE = 0;
    public final static int RUN = 1;
    public final static int SHOOT = 2;
    public final static int DIE = 3;

    public final static int RIGHT = 0;
    public final static int LEFT = 1;

    private final BufferedImage[][][] mon_hust_guardian = new BufferedImage[4][][];
    private BufferedImage exclaimation;
    private Animation mon_animator_hust_guardian = new Animation();
    private int actionLockCounter = 0;
    private final int changeDirCounter = 240;

    private int detectionCounter = 0;
    private final int detectionToSetAggro = 180;

    public Mon_HustGuardian(GameMap mp , int x , int y , String idName){
        super(mp , x , y);
        name = "Hust Guardian";
        width = 64;
        height = 64;
        speed = 1;
        last_speed = 1;

        getImage();
        setDefault();
        this.idName = idName;
    }

    private void getImage(){
        mon_hust_guardian[IDLE]  = new Sprite("/entity/mob/hust_guardian/hust_guardian_idle.png"  , width , height).getSpriteArray();
        mon_hust_guardian[RUN]   = new Sprite("/entity/mob/hust_guardian/hust_guardian_run.png"   , width , height).getSpriteArray();
        mon_hust_guardian[SHOOT] = new Sprite("/entity/mob/hust_guardian/hust_guardian_shoot.png" , width , height).getSpriteArray();
        mon_hust_guardian[DIE]   = new Sprite("/entity/mob/hust_guardian/hust_guardian_die.png"   , width , height).getSpriteArray();
        exclaimation = new Sprite("/effect/exclaimation_mark.png" , 16 , 16).getSpriteSheet();
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
        CURRENT_DIRECTION = RIGHT;

        CURRENT_ACTION = IDLE;
        PREVIOUS_ACTION = IDLE;
        mon_animator_hust_guardian.setAnimationState(mon_hust_guardian[IDLE][CURRENT_DIRECTION] , 10);
    }

    private void handleAnimationState(){
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
            if(isRunning) mon_animator_hust_guardian.setAnimationState(mon_hust_guardian[RUN][CURRENT_DIRECTION] , 10);
            if(isDying){
                mon_animator_hust_guardian.setAnimationState(mon_hust_guardian[DIE][CURRENT_DIRECTION] , 10);
                mon_animator_hust_guardian.playOnce();
            }
            if(isShooting){
                mon_animator_hust_guardian.setAnimationState(mon_hust_guardian[SHOOT][CURRENT_DIRECTION] , 6);
                mon_animator_hust_guardian.playOnce();
            }
            if(isIdle){
                mon_animator_hust_guardian.setAnimationState(mon_hust_guardian[IDLE][CURRENT_DIRECTION] , 10);
            }
        }

        if(!mon_animator_hust_guardian.isPlaying() && isShooting){
            isShooting = false;
        }
        if(!mon_animator_hust_guardian.isPlaying() && isDying){
            isDying = false;
            canbeDestroyed = true;
            loot();
        }

    }

    private void changeAnimationDirection(){
        switch(direction)
        {
            case "left" : CURRENT_DIRECTION = LEFT; break;
            case "right": CURRENT_DIRECTION = RIGHT; break;
        }
    }

    private void setAction(){
        if(!getAggro){
            //checkIfInRange();//Không đuôi theo người chơi
            actionWhenNeutral();
        } else {
            actionWhenGetAggro();//Đuổi theo người chơi
        }
        isRunning = (up | down | right | left) & (!isShooting);
        damagePlayer();
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
        int playerCol = (mp.player.worldX + mp.player.solidArea1.x) / childNodeSize;
        int playerRow = (mp.player.worldY + mp.player.solidArea1.y) / childNodeSize;
        int posCol = (worldX + solidArea1.x) / childNodeSize;
        int posRow = (worldY + solidArea1.y) / childNodeSize;

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
        if(up && isRunning && !isDying) newWorldY = worldY - speed;
        if(down && isRunning && !isDying) newWorldY = worldY + speed;
        if(left && isRunning && !isDying) newWorldX = worldX - speed;
        if(right && isRunning && !isDying) newWorldX = worldX + speed;

        mp.cChecker.checkCollisionWithEntity(this , mp.inactiveObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.activeObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.npc);
        mp.cChecker.checkCollisionWithEntity(this , mp.enemy);
        mp.cChecker.checkCollisionPlayer(this);
        if(mp.cChecker.checkInteractPlayer(this)) isInteracting = true;
        if(!collisionOn)
        {
            worldX = newWorldX;
            worldY = newWorldY;
        } else {
            if(!onPath){
                if(checkForValidDirection()){
                    direction = getValidDirection();
                    decideToMove();
                    actionLockCounter = 0;
                }
            }
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
        spawnHeart();
    }

    @Override
    public void update() {
        setAction();
        handleStatus();
        changeAnimationDirection();
        move();
        handleAnimationState();
        mon_animator_hust_guardian.update();
        CURRENT_FRAME = mon_animator_hust_guardian.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) {
        super.renderHPBar(g2 , 18 , 0);
        if(isInvincible && !isDying){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 0.3f));
        }
        g2.drawImage(mon_hust_guardian[CURRENT_ACTION][CURRENT_DIRECTION][CURRENT_FRAME] , worldX - camera.getX() , worldY - camera.getY() , width , height , null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1.0f));
        if(isDetectPlayer) g2.drawImage(exclaimation , worldX - camera.getX() + 54 , worldY - camera.getY() , null);
    }

    public void dispose(){
        solidArea1 = null;
        solidArea2 = null;
        hitbox = null;
        interactionDetectionArea = null;
        mon_animator_hust_guardian = null;
        for(int i = 0 ; i < mon_hust_guardian.length ; i++){
            for(int j = 0 ; j < mon_hust_guardian[i].length ; j++){
                for(int k = 0 ; k < mon_hust_guardian[i][j].length ; k++){
                    mon_hust_guardian[i][j][k].flush();
                    mon_hust_guardian[i][j][k] = null;
                }
            }
        }
        exclaimation.flush(); exclaimation = null;
        projectile = null;
        projectile_name = null;
        effectDealByProjectile = null;
        effectDealOnTouch = null;
    }

}
