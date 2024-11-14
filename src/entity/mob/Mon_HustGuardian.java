package entity.mob;

import entity.Actable;
import entity.Entity;
import graphics.Animation;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Mon_HustGuardian extends Entity implements Actable {
    GameMap mp;
    public final static int IDLE = 0;
    public final static int RUN = 1;
    public final static int SHOOT = 2;
    public final static int DIE = 3;

    public final static int RIGHT = 0;
    public final static int LEFT = 1;

    private BufferedImage[][][] mon_hust_guardian = new BufferedImage[4][][];
    private Animation mon_animator_hust_guardian = new Animation();

    private int CURRENT_ACTION;
    private int PREVIOUS_ACTION;
    private int CURRENT_DIRECTION;
    private int CURRENT_FRAME;

    public Mon_HustGuardian(GameMap mp){
        super();
        name = "Hust Guardian";
        this.mp = mp;
        width = 64;
        height = 64;

        getImage();
        setDefault();
    }

    private void getImage(){
        mon_hust_guardian[IDLE]  = new Sprite("/entity/mob/hust_guardian/hust_guardian_idle.png"  , width , height).getSpriteArray();
        mon_hust_guardian[RUN]   = new Sprite("/entity/mob/hust_guardian/hust_guardian_run.png"   , width , height).getSpriteArray();
        mon_hust_guardian[SHOOT] = new Sprite("/entity/mob/hust_guardian/hust_guardian_shoot.png" , width , height).getSpriteArray();
        mon_hust_guardian[DIE]   = new Sprite("/entity/mob/hust_guardian/hust_guardian_die.png"   , width , height).getSpriteArray();
    }

    private void setDefault(){
        hitbox = new Rectangle(23 , 35 , 16 , 24);
        solidArea1 = new Rectangle(27 , 53 , 13 , 6);
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        setDefaultSolidArea();

        direction = "right";
        CURRENT_DIRECTION = RIGHT;

        CURRENT_ACTION = IDLE;
        PREVIOUS_ACTION = IDLE;
        mon_animator_hust_guardian.setAnimationState(mon_hust_guardian[IDLE][CURRENT_DIRECTION] , 10);
    }

    private void handleAnimationState(){

    }

    public void move() {

    }

    public void setDialogue() {

    }

    public void attack() {

    }

    public void loot() {

    }

    public void pathFinding() {

    }

    @Override
    public void update() {
        move();
        handleAnimationState();
        mon_animator_hust_guardian.update();
        CURRENT_FRAME = mon_animator_hust_guardian.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(mon_hust_guardian[CURRENT_ACTION][CURRENT_DIRECTION][CURRENT_FRAME],
                worldX - camera.getX() , worldY - camera.getY() , width , height , null);
    }

}
