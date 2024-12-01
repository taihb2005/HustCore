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
import java.util.Random;

import static main.GamePanel.*;
import static map.GameMap.childNodeSize;

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

    private int shootTimer = 0;
    private int shootInterval = 10;

    private int spawnPointX;
    private int spawnPointY;
    private int posX;
    private int posY;
    private int rangeRadius;

    private int detectionCounter = 0;
    private final int detectionToSetAggro = 180;
    private Projectile projectile1, projectile2, projectile3;
    ArrayList<Projectile> proj;
    private int currentColumn = 1;
    private boolean isShooting1, isShooting2, isShooting3;

//    private int flameColumnDelayCounter = 0; // Bộ đếm delay giữa các cột
//    private int flameCurrentColumn = 1;     // Cột hiện tại đang được bắn
//    private int maxFlameColumns = 5;        // Tổng số cột flame
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
        hitbox = new Rectangle(22 , 24 , 128 , 128);
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

    private void changeAnimationDirection(){
        switch(direction)
        {
            case "left" : CURRENT_DIRECTION = LEFT; break;
            case "right": CURRENT_DIRECTION = RIGHT; break;
        }
    }

    private void setAction()
    {
        actionLockCounter++;
        if (actionLockCounter >= 100 && !isShooting1 && !isShooting2 && !isShooting3 && !isDying) {
            Random random = new Random();
            int i = random.nextInt(300) + 1;  // pick up  a number from 1 to 100
            if (i <= 99) {
                actionWhileShoot1();
            }
            if (i > 99 && i <= 199) {
                actionWhileShoot2();
            }
            if (i > 199) {
                actionWhileShoot3();
            }
            actionLockCounter = 0; // reset
            isRunning = !isShooting && (right | left | up | down);
        }
        else if (isShooting3) {
            shootTimer++;
            actionWhileShoot3();
        };
    }

    @Override
    public void update() {
//        testTime++;
//        if (testTime == 100) shoot3();
//        if (shooting) {
//            shootTimer++;
//            if (shootTimer >= shootInterval) {
//                createFlameColumn(); // Tạo cột lửa
//                shootTimer = 0; // Reset timer
//            }
//        }
        setAction();
        handleAnimationState();
        handleStatus();
        if(!proj.isEmpty())proj.removeIf(pr -> !pr.active);
        changeAnimationDirection();
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

        if(!mon_animator_boss.isPlaying() && isShooting1){
            isShooting1 = false;
        }
        if(!mon_animator_boss.isPlaying() && isShooting2){
            isShooting2 = false;
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
//        if(mp.cChecker.checkInteractPlayer(this)) isInteracting = true;

        if(!collisionOn)
        {
            worldX = newWorldX;
            worldY = newWorldY;
        }

        newWorldX = worldX;
        newWorldY = worldY;
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

    public void shoot3() {
        shooting = true; // Đặt trạng thái bắn
        currentColumn = 1; // Bắt đầu từ cột đầu tiên
    }

    public void shoot1() {
        isShooting1 = true;
        if(!projectile1.active && shootAvailableCounter == SHOOT_INTERVAL) {
            projectile1.set(worldX+25, worldY+12, direction, true, this);
            projectile1.setHitbox();
            projectile1.setSolidArea();
            mp.addObject(projectile1, mp.projectiles);
            shootAvailableCounter = 0;
        }
    }
    public void shoot2() {
        if(!projectile2.active && shootAvailableCounter == SHOOT_INTERVAL) {
            projectile2.set(worldX+50, worldY+30, direction, true, this);
            projectile2.setHitbox();
            projectile2.setSolidArea();
            mp.addObject(projectile2, mp.projectiles);
            shootAvailableCounter = 0;
        }
    }
    public void createFlameColumn() {
            shootAvailableCounter++;
            if (shootAvailableCounter >= 10) {
                for (int j = 1; j <= 5; j++) {
                    Projectile newFlame = new Proj_Flame(mp);
                    newFlame.set(worldX + 50 * currentColumn+50, worldY + 50 * (j-1) - 41, direction, true, this);
                    newFlame.setHitbox();
                    newFlame.setSolidArea();
                    proj.add(newFlame);
                }
                currentColumn++;
                for(Projectile flame : proj) mp.addObject(flame , mp.projectiles);
                shootAvailableCounter = 0;
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

    public void actionWhileShoot1() {
        shoot1();
    }

    public void actionWhileShoot2() {
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
            isShooting2 = true;
            if(posCol < playerCol) direction = "right"; else
            if(posCol >  playerCol) direction = "left"; else
            if(posRow < playerRow) direction = "down"; else
                direction = "up";
            shoot2();
        }
        isRunning = !isShooting2;
    }

    public void actionWhileShoot3() {
        System.out.println("SHOOT3");
        System.out.println(currentColumn);
        // Kích hoạt trạng thái bắn
        if (!isShooting3) {
            isShooting3 = true;
            shootTimer = 0; // Reset shootTimer
            currentColumn = 1; // Reset vị trí cột lửa
        }

        // Xử lý bắn khi đang ở trạng thái bắn
        if (isShooting3) {
             // Tăng giá trị shootTimer mỗi frame
            // Nếu đạt đến khoảng thời gian bắn cho một cột
            if (shootTimer >= 10) {
                createFlameColumn(); // Tạo cột lửa
                shootTimer = 0; // Reset timer
            }
            // Nếu đã bắn đủ 5 cột, kết thúc trạng thái bắn
            if (currentColumn > 5) {
                currentColumn = 1;
                isShooting3 = false;
            }
        }
    }

}
