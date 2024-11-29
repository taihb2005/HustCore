package entity.mob;

import entity.Actable;
import entity.effect.type.EffectNone;
import entity.projectile.Proj_GuardianProjectile;
import graphics.Animation;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.*;

public class Mon_Boss extends Monster implements Actable {
    public final static int IDLE = 0;
    public final static int TALK = 1;
    public final static int RUN = 2;
    public final static int SHOOT_1 = 3;
    public final static int SHOOT_2 = 4;
    public final static int SHOOT_3 = 5;
    public final static int DIE = 6;

    public final static int RIGHT = 0;
    public final static int LEFT = 1;

    private final BufferedImage[][][] mon_boss = new BufferedImage[7][][];
    private final Animation mon_animator_boss = new Animation();
    private int actionLockCounter = 0;
    private final int changeDirCounter = 240;

    private int spawnPointX;
    private int spawnPointY;
    private int posX;
    private int posY;
    private int rangeRadius;

    private int detectionCounter = 0;
    private final int detectionToSetAggro = 180;

    public Mon_Boss(GameMap mp){
        super(mp);
        name = "Boss";
        width = 128;
        height = 128;
        speed = 1;

        getImage();
        setDefault();
    }

    private void getImage(){
        mon_boss[IDLE]  = new Sprite("/entity/mob/boss/boss_idle.png"  , width , height).getSpriteArray();
        mon_boss[TALK]  = new Sprite("/entity/mob/boss/boss_talk.png"  , width , height).getSpriteArray();
        mon_boss[RUN]   = new Sprite("/entity/mob/boss/boss_run.png"   , width , height).getSpriteArray();
        mon_boss[SHOOT_1] = new Sprite("/entity/mob/boss/boss_shoot1.png" , width , height).getSpriteArray();
        mon_boss[SHOOT_2] = new Sprite("/entity/mob/boss/boss_shoot2.png" , width , height).getSpriteArray();
        mon_boss[SHOOT_3] = new Sprite("/entity/mob/boss/boss_shoot3.png" , width , height).getSpriteArray();
        mon_boss[DIE]   = new Sprite("/entity/mob/boss/boss_die.png"   , width , height).getSpriteArray();
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
        rangeRadius = 200;
        projectile = new Proj_GuardianProjectile(mp);
        effectDealOnTouch = new EffectNone(mp.player);
        effectDealByProjectile = new EffectNone(mp.player);

        SHOOT_INTERVAL = projectile.maxHP + 5;
        expDrop = 30;

        direction = "right";
        CURRENT_DIRECTION = RIGHT;

        CURRENT_ACTION = IDLE;
        PREVIOUS_ACTION = IDLE;
        mon_animator_boss.setAnimationState(mon_boss[IDLE][CURRENT_DIRECTION] , 10);
    }

    @Override
    public void update() {
        handleStatus();
        move();
        mon_animator_boss.update();
        CURRENT_FRAME = mon_animator_boss.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) {
        if(isInvincible && !isDying){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 0.3f));
        }
        g2.drawImage(mon_boss[TALK][1][CURRENT_FRAME] , worldX - camera.getX() , worldY - camera.getY() , width , height , null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1.0f));
    }

    @Override
    public void move() {

    }

    @Override
    public void setDialogue() {

    }

    @Override
    public void attack() {

    }

    @Override
    public void loot() {

    }
}
