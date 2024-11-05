package entity.mob;

import entity.Actable;
import entity.Entity;
import graphics.Animation;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static main.GamePanel.camera;

public class Mon_Spectron extends Entity implements Actable {

    GameMap mp;
    public static String trait = "fly";
    final private int IDLE = 0;
    final private int RUN = 1;
    final private int SHOOT = 2;
    final private int DIE = 3;

    final private int RIGHT = 0;
    final private int LEFT = 1;

    private int PREVIOUS_ACTION;
    private int CURRENT_ACTION;
    private int CURRENT_DIRECTION;

    private boolean isRunning;
    private boolean isShooting;
    private boolean isDying;

    final private BufferedImage [][][] mon_spectron = new BufferedImage[4][][];
    final private Animation mon_animator_spectron = new Animation();

    private int actionLockCounter = 0;

    private int CURRENT_FRAME;

    public Mon_Spectron(GameMap mp)
    {
        super();
        this.mp = mp;
        super.width = 64;
        super.height = 64;

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
        speed = 1;

        solidArea1 = new Rectangle(20 , 19 , 26 , 15);
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

    }

    public void pathFinding() {

    }

    private void changeDirection()
    {
        switch(direction)
        {
            case "left" : CURRENT_DIRECTION = LEFT; break;
            case "right": CURRENT_DIRECTION = RIGHT; break;
        }
    }

    private void handleAnimationState()
    {
        if(isRunning) CURRENT_ACTION = RUN;else
        {
            mon_animator_spectron.setAnimationState(mon_spectron[IDLE][CURRENT_DIRECTION] , 10);
            CURRENT_ACTION = IDLE;
            PREVIOUS_ACTION = IDLE;
        }

        if(PREVIOUS_ACTION != CURRENT_ACTION)
        {
            PREVIOUS_ACTION = CURRENT_ACTION;
            if(isRunning) mon_animator_spectron.setAnimationState(mon_spectron[CURRENT_ACTION][CURRENT_DIRECTION] , 10);
        }

    }

    private void setAction()
    {
        actionLockCounter++;

        if(actionLockCounter == 120)
        {
            up = down = left = right = false;
            Random random = new Random();
            int i = random.nextInt(100) + 1;  // pick up  a number from 1 to 100
            if(i <= 25)
            {
                up = true;
            }
            if(i>25 && i <= 50)
            {
                down = true;
            }
            if(i>50 && i <= 75)
            {
                direction = "left";
                left = true;
            }
            if(i>75 && i <= 100)
            {
                direction = "right";
                right = true;
            }
            actionLockCounter = 0; // reset
        }
        isRunning = true;
    }

    public void move() {
        collisionOn = false;
        if(up && isRunning) newWorldY = worldY - speed;
        if(down && isRunning) newWorldY = worldY + speed;
        if(left && isRunning) newWorldX = worldX - speed;
        if(right && isRunning) newWorldX = worldX + speed;

        mp.cChecker.checkCollisionWithInactiveObject(this);
        mp.cChecker.checkCollisionWithActiveObject(this);

        if(!collisionOn)
        {
            worldX = newWorldX;
            worldY = newWorldY;
        }
        newWorldX = worldX;
        newWorldY = worldY;
    }


    @Override
    public void update() {
        setAction();
        move();
        changeDirection();
        handleAnimationState();
        mon_animator_spectron.update();
        CURRENT_FRAME = mon_animator_spectron.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(mon_spectron[CURRENT_ACTION][CURRENT_DIRECTION][CURRENT_FRAME] , worldX - camera.getX() , worldY - camera.getY() , width , height , null);
    }
}
