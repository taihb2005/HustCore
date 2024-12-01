package entity.mob;

import entity.Actable;
import entity.effect.type.EffectNone;
import entity.projectile.Proj_ExplosivePlasma;
import entity.projectile.Proj_Flame;
import entity.projectile.Proj_TrackingPlasma;
import entity.projectile.Projectile;
import graphics.Animation;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
    private Projectile projectile1, projectile2, projectile3;
    private ArrayList<Projectile> proj;

    private boolean isShooting1, isShooting2, isShooting3;

    private int flameColumnDelayCounter = 0; // Bộ đếm delay giữa các cột
    private int flameCurrentColumn = 1;     // Cột hiện tại đang được bắn
    private int maxFlameColumns = 5;        // Tổng số cột flame
    private boolean shooting = false;      // Trạng thái đang bắn flame


    private int testTime = 0;
    public Mon_Boss(GameMap mp){
        super(mp);
        name = "Boss";
        width = 128;
        height = 128;
        speed = 1;

        getImage();
        setDefault();
    }

    public Mon_Boss(GameMap mp , int x , int y){
        super(mp , x , y);
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
        setDefaultSolidArea();

        invincibleDuration = 30;
        maxHP = 400;
        currentHP = maxHP;
        strength = 10;
        level = 1;
        defense = 10;
        rangeRadius = 200;
        projectile1 = new Proj_TrackingPlasma(mp);
        projectile2 = new Proj_ExplosivePlasma(mp);
        projectile3 = new Proj_Flame(mp);
        proj = new ArrayList<>();
        effectDealOnTouch = new EffectNone(mp.player);
        effectDealByProjectile = new EffectNone(mp.player);

        SHOOT_INTERVAL = 45;
        expDrop = 100;

        direction = "right";
        CURRENT_DIRECTION = RIGHT;

        CURRENT_ACTION = IDLE;
        PREVIOUS_ACTION = IDLE;
        mon_animator_boss.setAnimationState(mon_boss[IDLE][CURRENT_DIRECTION] , 10);
    }

    @Override
    public void update() {
        testTime++;
//        System.out.println(testTime);
        for (int i = 1; i < 2; i++) {
            if (testTime % 200 == i*10) {
                shoot3(i);
            }
        }
//        if (testTime % 50 == 0) shoot2();
        if (testTime == Integer.MAX_VALUE) testTime = 0;
        handleAnimationState();
        handleStatus();
        if(!proj.isEmpty())proj.removeIf(pr -> !pr.active);
        move();
        mon_animator_boss.update();
        CURRENT_FRAME = mon_animator_boss.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) {
        if(isInvincible && !isDying){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 0.3f));
        }
        g2.drawImage(mon_boss[CURRENT_ACTION][CURRENT_DIRECTION][CURRENT_FRAME] , worldX - camera.getX() , worldY - camera.getY() , width , height , null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1.0f));
    }

    private void handleAnimationState(){
        if(isRunning && !isDying) {
            isIdle = false;
            CURRENT_ACTION = RUN;
        }
        else
        if(isShooting1 && !isDying){
            isIdle = false;
            CURRENT_ACTION = SHOOT_1;
        } else
        if(isShooting2 && !isDying){
            isIdle = false;
            CURRENT_ACTION = SHOOT_2;
        } else
        if(isShooting3 && !isDying){
            isIdle = false;
            CURRENT_ACTION = SHOOT_3;
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
            if(isRunning) mon_animator_boss.setAnimationState(mon_boss[RUN][CURRENT_DIRECTION] , 10);
            if(isDying){
                mon_animator_boss.setAnimationState(mon_boss[DIE][CURRENT_DIRECTION] , 10);
                mon_animator_boss.playOnce();
            }
            if(isShooting1){
                mon_animator_boss.setAnimationState(mon_boss[SHOOT_1][CURRENT_DIRECTION] , 6);
                mon_animator_boss.playOnce();
            }
            if(isShooting2){
                mon_animator_boss.setAnimationState(mon_boss[SHOOT_2][CURRENT_DIRECTION] , 6);
                mon_animator_boss.playOnce();
            }
            if(isShooting3){
                mon_animator_boss.setAnimationState(mon_boss[SHOOT_3][CURRENT_DIRECTION] , 6);
                mon_animator_boss.playOnce();
            }
            if(isIdle){
                mon_animator_boss.setAnimationState(mon_boss[IDLE][CURRENT_DIRECTION] , 10);
            }
        }

        if(!mon_animator_boss.isPlaying() && isShooting){
            isShooting = false;
        }
        if(!mon_animator_boss.isPlaying() && isShooting3){
            isShooting3 = false;
        }
        if(!mon_animator_boss.isPlaying() && isDying){
            isDying = false;
            canbeDestroyed = true;
        }

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

    public void shoot1() {
        if(!projectile1.active && shootAvailableCounter == SHOOT_INTERVAL) {
            projectile1.set(worldX, worldY, direction, true, this);
            projectile1.setHitbox();
            projectile1.setSolidArea();
            mp.addObject(projectile1, mp.projectiles);
            shootAvailableCounter = 0;
        }
    }
    public void shoot2() {
        if(!projectile2.active && shootAvailableCounter == SHOOT_INTERVAL) {
            projectile2.set(worldX, worldY, direction, true, this);
            projectile2.setHitbox();
            projectile2.setSolidArea();
            mp.addObject(projectile2, mp.projectiles);
            shootAvailableCounter = 0;
        }
    }
    public void shoot3(int i) {
        // Nếu chưa kích hoạt trạng thái bắn hoặc khoảng thời gian cho phép
        if (!shooting && shootAvailableCounter == SHOOT_INTERVAL) {
            shooting = true;
            shootAvailableCounter = 0; // Reset shoot counter
        }

        // Nếu đang bắn
        if (shooting) {
            for (int j = 1; j <= 5; j++) {
                Projectile newFlame = new Proj_Flame(mp);
                newFlame.set(worldX + 50 * i+50, worldY + 50 * (j-2) - 41, direction, true, this);
                newFlame.setHitbox();
                newFlame.setSolidArea();
                proj.add(newFlame);
            }
            for(Projectile flame : proj) mp.addObject(flame , mp.projectiles);
        }
    }
    public void shoot4() {
        if(!projectile3.active && shootAvailableCounter == SHOOT_INTERVAL) {
            projectile3.set(worldX, worldY, direction, true, this);
            projectile3.setHitbox();
            projectile3.setSolidArea();
            mp.addObject(projectile3, mp.projectiles);
            shootAvailableCounter = 0;
        }
    }
}
