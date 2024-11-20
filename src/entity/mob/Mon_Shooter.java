package entity.mob;

import entity.Actable;
import entity.effect.EffectType;
import entity.projectile.Obj_Plasma;
import graphics.Animation;
import graphics.Sprite;
import main.GamePanel;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;
/*
Type of monster only attack in one direction
Only attack at a certain of time, but deal massive damage when the projectile hits and slow player down
This thing only take damage when the player shoots in front of it, and when the player is 2 tiles away.
Sleeps when no player is nearby, goes active when the player in its detection range
 */
public class Mon_Shooter extends Monster implements Actable {
    GameMap mp;
    public final static int IDLE = 0;
    public final static int ACTIVE = 1;
    public final static int SHOOT = 2;
    public final static int DIE = 3;

    private final static int RIGHT = 0;
    private final static int LEFT = 1;
    private final static int DOWN = 2;
    private final static int UP = 3;

    private boolean isAlwaysUp;
    private boolean canChangeState;

    private int maxActiveTime = 1800;
    private int activeTimeCounter = 0;
    private int shotInterval = 120;
    private int shootCounter = 0;

    private final BufferedImage[][][] mon_shooter = new BufferedImage[4][][];
    private final Animation mon_animator_shooter = new Animation();
    public int type;
    public Mon_Shooter(GameMap mp){
        super();
        this.mp = mp;
        name = "Shooter";
        this.type = IDLE;
        width = 64;
        height = 64;
        speed = 0;
        strength = 10;
        level = 1;

        getImage();
        setDefault();
    }
    public Mon_Shooter(GameMap mp , int type){
        super();
        this.mp = mp;
        name = "Shooter";
        this.type = type;
        width = 64;
        height = 64;
        speed = 0;
        strength = 10;
        level = 1;

        getImage();
        setDefault();
    }
    public Mon_Shooter(GameMap mp , int type , boolean isAlwaysUp){
        super();
        this.mp = mp;
        name = "Shooter";
        this.type = type;
        this.isAlwaysUp = isAlwaysUp;
        width = 64;
        height = 64;
        speed = 0;
        strength = 10;
        level = 1;

        getImage();
        setDefault();
    }

    private void setDefault(){
        maxHP = 200;
        currentHP = maxHP;
        invincibleDuration = 60;
        projectile_name = "Plasma";
        projectile = new Obj_Plasma(mp);

        expDrop = 20;

        hitbox = new Rectangle (24 , 23 , 14 , 25);
        solidArea1 = hitbox;
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        interactionDetectionArea = new Rectangle(-100 , -100 , width + 200 , height + 200);
        setDefaultSolidArea();

        direction = "right";
        CURRENT_DIRECTION = RIGHT;
        if(type == IDLE){
            isIdle = true;
            PREVIOUS_ACTION = IDLE;
            CURRENT_ACTION = IDLE;
            mon_animator_shooter.setAnimationState(mon_shooter[IDLE][CURRENT_DIRECTION] , 100 );
        } else{
            PREVIOUS_ACTION = ACTIVE;
            CURRENT_ACTION = ACTIVE;
            mon_animator_shooter.setAnimationState(mon_shooter[ACTIVE][CURRENT_DIRECTION] , 20);
        }

        CURRENT_FRAME = 0;
    }

    private void getImage(){
        mon_shooter[IDLE]   = new Sprite("/entity/mob/shooter/shooter_idle.png" , width , height).getSpriteArray();
        mon_shooter[ACTIVE] = new Sprite("/entity/mob/shooter/shooter_active.png" , width , height).getSpriteArray();
        mon_shooter[SHOOT]  = new Sprite("/entity/mob/shooter/shooter_shoot.png" , width , height).getSpriteArray();
        mon_shooter[DIE]    = new Sprite("/entity/mob/shooter/shooter_die.png" , width , height).getSpriteArray();
    }

    public void move() {
        //No movement
    }

    public void setDialogue() {

    }

    public void loot() {

    }

    public void pathFinding() {

    }

    /*
    Phương thức tấn công: Tấn công theo chu kì
     */
    public void updateAttackCycle() {
        if(type == ACTIVE && !isDying){
            shootCounter++;
            if(shootCounter >= shotInterval){
                isShooting = true;
                shootCounter = 0;
            }
        }
    }

    public void attack(){
        if(!isDying) {
            projectile.set(worldX, worldY, direction, true, this);
            for (int i = 0; i < mp.projectiles.length; i++) {
                if (mp.projectiles[i] == null) {
                    mp.projectiles[i] = projectile;
                    break;
                }
            }
        }
    }

    private void changeDirection(){
        switch (direction){
            case "right" : CURRENT_DIRECTION = RIGHT; break;
            case "left"  : CURRENT_DIRECTION = LEFT; break;
            case "up"    : CURRENT_DIRECTION = UP; break;
            case "down"  : CURRENT_DIRECTION = DOWN ; break;
        }
    }


    private void changeState(){
        if(canChangeState && type == IDLE){
            type = ACTIVE;
            canChangeState = false;
        } else
        if(canChangeState && type == ACTIVE && !isAlwaysUp){
            type = IDLE;
            isIdle = true;
            canChangeState = false;
        }
    }

    private void checkForPlayer(){
        boolean contactPlayer = mp.cChecker.checkPlayer(this);
        if(contactPlayer && !mp.player.isInvincible){
            mp.player.receiveDamage(this);
            mp.player.isInvincible = true;
        }
        if(type == IDLE) canChangeState = isInteracting;
        if(!isInteracting && type == ACTIVE){
            activeTimeCounter++;
            if(activeTimeCounter >= maxActiveTime){
                activeTimeCounter = 0;
                canChangeState = true;
            }
        } else if(isInteracting) activeTimeCounter = 0;
        changeState();
        isInteracting = false;
    }

    private void handleAnimationState(){
        if(type == IDLE){
            if(isDying) {
                isIdle = false;
                CURRENT_ACTION = DIE;
            } else {
                isIdle = true;
                CURRENT_ACTION = IDLE;
            }
            if(PREVIOUS_ACTION != CURRENT_ACTION){
                PREVIOUS_ACTION = CURRENT_ACTION;
                if(isDying){
                    mon_animator_shooter.setAnimationState(mon_shooter[DIE][CURRENT_DIRECTION] , 15);
                    mon_animator_shooter.playOnce();
                }
                if(isIdle)mon_animator_shooter.setAnimationState(mon_shooter[IDLE][CURRENT_DIRECTION] , 100);
            }
            if(!mon_animator_shooter.isPlaying() && isDying) {
                isDying = false;
                canbeDestroyed = true;
            }
        } else
        if(type == ACTIVE){
                 if(isShooting && !isDying) {CURRENT_ACTION = SHOOT ; isIdle = false;}
            else if(isDying)    {CURRENT_ACTION = DIE; isIdle = false;}
            else {
                isIdle = true;
                CURRENT_ACTION = ACTIVE;
            }

            if(PREVIOUS_ACTION != CURRENT_ACTION){
                PREVIOUS_ACTION = CURRENT_ACTION;
                if(isShooting){
                    mon_animator_shooter.setAnimationState(mon_shooter[SHOOT][CURRENT_DIRECTION] , 25);
                    mon_animator_shooter.playOnce();
                }
                if(isDying){
                    mon_animator_shooter.setAnimationState(mon_shooter[DIE][CURRENT_DIRECTION] , 15);
                    mon_animator_shooter.playOnce();
                }
                if(isIdle){mon_animator_shooter.setAnimationState(mon_shooter[ACTIVE][CURRENT_DIRECTION] , 20);}
            }

            if(!mon_animator_shooter.isPlaying() && isShooting) {
                isShooting = false;
                attack();
            }
            if(!mon_animator_shooter.isPlaying() && isDying) {
                isDying = false;
                canbeDestroyed = true;
            }
        }
    }

    @Override
    public void update() {
        checkForPlayer();
        updateAttackCycle();
        updateInvincibility();
        handleAnimationState();
        mon_animator_shooter.update();
        CURRENT_FRAME = mon_animator_shooter.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) {
        if(isInvincible && !isDying){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 0.3f));
        }
        g2.drawImage(mon_shooter[CURRENT_ACTION][CURRENT_DIRECTION][CURRENT_FRAME] , worldX - camera.getX() , worldY - camera.getY() , width , height , null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1.0f));
    }

    public int getInterval(){return shotInterval;}
    public void setInterval(int dt){this.shotInterval = dt;}
    public void setDirection(String dir){
        direction = dir;
        changeDirection();
    }
}
