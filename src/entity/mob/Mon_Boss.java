package entity.mob;

import entity.Actable;
import entity.Entity;
import entity.effect.type.EffectNone;
import entity.object.Obj_Door;
import entity.projectile.Proj_ExplosivePlasma;
import entity.projectile.Proj_Flame;
import entity.projectile.Proj_TrackingPlasma;
import entity.projectile.Projectile;
import graphics.Animation;
import graphics.Sprite;
import level.AssetSetter;
import main.UI;
import map.GameMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
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

    private int CURRENT_SKILL = 1;
    private final BufferedImage[][][] mon_boss = new BufferedImage[7][][];
    private final Animation mon_animator_boss = new Animation();
    private int actionLockCounter = 550;

    private int shootTimer = 0;

    private boolean nearlyDie = false;
    private boolean fullyDie = false;

    private Projectile projectile1, projectile2, projectile3;
    ArrayList<Projectile> proj;
    private int currentColumn = 1;
    private boolean isShooting1, isShooting2, isShooting3;
    private boolean shoot3;
    // Tổng số cột flame
    private boolean shooting = false;      // Trạng thái đang bắn flame
    private BufferedImage hpFrame;
    public Mon_Boss(GameMap mp){
        super(mp);
        name = "Boss";
        width = 64;
        height = 64;
        speed = 2;

        getImage();
        setDefault();
    }

    public Mon_Boss(GameMap mp , int x , int y){
        super(mp , x , y);
        name = "Boss";
        width = 128;
        height = 128;
        speed = 1;

        try {
            hpFrame = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/ui/boss_hpFrame.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        hitbox = new Rectangle(20 , 40 , 80 , 80);
        solidArea1 = new Rectangle(20 , 110 , 90 , 18);
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        setDefaultSolidArea();

        invincibleDuration = 30;
        maxHP = 1400;
        currentHP = maxHP;
        strength = 50;
        level = 1;
        defense = 10;
        projectile1 = new Proj_TrackingPlasma(mp);
        projectile2 = new Proj_ExplosivePlasma(mp);
        projectile3 = new Proj_Flame(mp);
        proj = new ArrayList<>();
        effectDealOnTouch = new EffectNone(mp.player);
        effectDealByProjectile = new EffectNone(mp.player);

        SHOOT_INTERVAL = 45;
        expDrop = 0;

        direction = "right";
        CURRENT_DIRECTION = RIGHT;
        setDialogue();

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

    private void setAction() {
        switch (CURRENT_SKILL) {
            case 1: actionWhileShoot1();
            actionLockCounter--;
            if (actionLockCounter == 0) {
                CURRENT_SKILL = 2;
                actionLockCounter = 100;
            }
            break;
            case 2: actionWhileShoot2();
            actionLockCounter--;
            if (actionLockCounter == 0) CURRENT_SKILL = 3;
            break;
            case 3: actionWhileShoot3();
            if (currentColumn >= 10) {
                CURRENT_SKILL = 1;
                actionLockCounter = 100;
                currentColumn = 1;
            }
            break;
        }
    }

    @Override
    public void update() throws NullPointerException{
        setAction();
        handleAnimationState();
        handleStatus();
        if(!proj.isEmpty())proj.removeIf(pr -> !pr.active);
        changeAnimationDirection();
        move();
        mon_animator_boss.update();
        UI.bossHP = currentHP;
        CURRENT_FRAME = mon_animator_boss.getCurrentFrames();
        if (currentHP <= 0 && fullyDie) {
            this.startDialogue(this, 1);
            fullyDie = true;
        }
        else if (2*currentHP < maxHP && !nearlyDie) {
            this.startDialogue(this, 0);
            nearlyDie = true;
            AssetSetter setter = new AssetSetter(mp);
            setter.setFilePathEnemy("/level/level04/enemy_level04.json");
            try{setter.setEnemy();} catch(IOException ioe){throw new RuntimeException("Lỗi tên file trong boss r kìa");}
            for(Entity e : mp.activeObj)
                if(e != null && e.idName.equals("Door 7")){
                    e.canbeDestroyed = true;
                }
        }
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
        if(shoot3 && !isDying){
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
            if(isRunning) mon_animator_boss.setAnimationState(mon_boss[RUN][CURRENT_DIRECTION] , 7);
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
            if(shoot3){
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
        if(!mon_animator_boss.isPlaying() && shoot3){
            shoot3 = false;
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
        else if(down && isRunning && !isDying) newWorldY = worldY + speed;
        else if(left && isRunning && !isDying) newWorldX = worldX - speed;
        else if(right && isRunning && !isDying) newWorldX = worldX + speed;

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
        this.dialogues[0][0] = new StringBuilder("Ngươi cũng mạnh phết đấy.");
        this.dialogues[0][1] = new StringBuilder("Xem ra ta phải nhờ đến sự trợ\ngiúp của thuộc hạ rồi.");
        this.dialogues[1][0] = new StringBuilder("Á hự... Không thể tin ngươi đã đánh bại được ta...");
        this.dialogues[1][1] = new StringBuilder("Huhuhu...");
    }

    @Override
    public void attack() {
    }

    @Override
    public void loot() {

    }

    public void shoot1() {
        isShooting1 = !isDying;
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
            projectile2.set(worldX+78, worldY+60, direction, true, this);
            projectile2.setHitbox();
            projectile2.setSolidArea();
            mp.addObject(projectile2, mp.projectiles);
            shootAvailableCounter = 0;
        }
    }
    public void createFlameColumn(int isLeft) {
            shootAvailableCounter++;
            if (shootAvailableCounter >= 10) {
                for (int j = 1; j <= 5; j++) {
                    Projectile newFlame = new Proj_Flame(mp);
                    newFlame.set(worldX + 50 * isLeft* currentColumn+50, worldY + 50 * (j-1) - 41, direction, true, this);
                    newFlame.setHitbox();
                    newFlame.setSolidArea();
                    proj.add(newFlame);
                }
                currentColumn++;
                for(Projectile flame : proj) mp.addObject(flame , mp.projectiles);
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
        int isLeft = (worldX < mp.player.worldX)?-1:1;
        searchPathforBoss(playerCol, playerRow);
        decideToMove();
        boolean check3TilesAway = (Math.abs(playerCol - posCol) <= 12) || (Math.abs(playerRow - posRow) <= 12);
        boolean checkShootInterval = (shootAvailableCounter == SHOOT_INTERVAL);
        boolean checkIfConcurent = (Math.abs(playerCol - posCol) == 0) || (Math.abs(playerRow - posRow) == 0);
        if (check3TilesAway && checkShootInterval && checkIfConcurent) {
            isShooting2 = true;
            if (Math.abs(worldX-mp.player.worldX) > Math.abs(worldY-mp.player.worldY)) {
                if(worldX < mp.player.worldX)  direction = "right";
                else if(worldX > mp.player.worldX) direction = "left";
            }
             else if(worldY < mp.player.worldY) direction = "down"; else
                direction = "up";
            shoot2();
        }
        isRunning = !isShooting2 && !isDying;
    }

    public void actionWhileShoot3() {
        // Kích hoạt trạng thái bắn nếu chưa bắt đầu
        if (!isShooting3) {
            shoot3 = true;
            isShooting3 = true;
            isRunning = false;
            shootTimer = 0; // Reset shootTimer
            currentColumn = 1; // Reset vị trí cột lửa
        }

        // Xử lý trạng thái bắn
        if (isShooting3) {
            shootTimer++; // Tăng timer mỗi frame
            if (shootTimer >= 10) {
                int isLeft = direction.equals("left") ? -1 : 1;
                createFlameColumn(isLeft); // Tạo cột lửa
                shootTimer = 0; // Reset timer sau khi bắn
            }

            // Dừng bắn sau khi hoàn thành 10 cột lửa
            if (currentColumn > 10) {
                currentColumn = 1;
                isShooting3 = false;
                shoot3 = false;
            }
        }
    }
}
