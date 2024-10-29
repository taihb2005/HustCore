package entity.player;

import entity.Entity;
import graphics.Sprite;
import main.GamePanel;
import main.KeyHandler;
import map.GameMap;
import graphics.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    GameMap mp;
    final int IDLE = 0;
    final int RUN = 1;
    final int TALK = 2;
    final int SHOOT = 3;
    final int RELOAD = 4;
    final int DEATH = 5;

    final int RIGHT = 0;
    final int LEFT = 1;

    private int PREVIOUS_ACTION;
    private int CURRENT_ACTION;
    private int CURRENT_DIRECTION;

    private boolean isRunning;
    private boolean isShooting;
    private boolean isReloading;
    private boolean isDying;

    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean shoot;
    private boolean die;
    
    public final int screenX, screenY;

    private BufferedImage[][][] player_gun = new BufferedImage[7][][];;
    private BufferedImage[][][] player_nogun = new BufferedImage[7][][];

    private int CURRENT_FRAME;


    final protected Animation animator = new Animation();

    public Player(GameMap mp) {
        super();
        this.mp = mp;
        width = 64;
        height = 64;

        solidArea1 = new Rectangle(27 , 53 , 13 , 6);
        solidAreaDefaultX1 = 27;
        solidAreaDefaultY1 = 53;

        screenX = GamePanel.windowWidth/2 - 32;
        screenY = GamePanel.windowHeight/2 - 32;

        getPlayerImages();
        setDefaultValue();
    }

    private void setDefaultValue()
    {
        worldX = 1300;
        worldY = 1700;
        newWorldX = worldX;
        newWorldY = worldY;
        speed = 3;

        up = down = left = right = false;
        direction = "right";
        CURRENT_DIRECTION = RIGHT;
        PREVIOUS_ACTION = IDLE;
        CURRENT_ACTION = IDLE;
        CURRENT_FRAME = 0;
        animator.setAnimationState(player_gun[IDLE][RIGHT] , 5);
    }

    private void getPlayerImages()
    {
        player_gun[IDLE]   = new Sprite("/entity/player/idle_gun_blue.png"   , width , height).getSpriteArray();
        player_gun[TALK]   = new Sprite("/entity/player/talk_gun_blue.png"   , width , height).getSpriteArray();
        player_gun[RUN]    = new Sprite("/entity/player/run_gun_blue.png"    , width , height).getSpriteArray();
        player_gun[SHOOT]  = new Sprite("/entity/player/shoot_gun_blue.png"  , width , height).getSpriteArray();
        player_gun[RELOAD] = new Sprite("/entity/player/reload_gun_blue.png" , width , height).getSpriteArray();
        player_gun[DEATH]  = new Sprite("/entity/player/death_blue.png"      , width , height).getSpriteArray();
    }


    @Override
    public void update()
    {
        keyInput();
        handlePosition();
        changeDirection();
        //handleCollision();
        handleAnimationState();
        animator.update();
        CURRENT_FRAME = animator.getCurrentFrames();
    }


    @Override
    public void render(Graphics2D g2)
    {
        g2.drawImage(player_gun[CURRENT_ACTION][CURRENT_DIRECTION][CURRENT_FRAME] ,
                     worldX - GamePanel.camera.getX() , worldY - GamePanel.camera.getY(),
                     width, height, null);
    }

    private void keyInput()
    {
        up    = KeyHandler.upPressed;
        down  = KeyHandler.downPressed;
        left  = KeyHandler.leftPressed;
        right = KeyHandler.rightPressed;

        isRunning = up | down | left | right;
    }

    private void handleAnimationState()
    {
        if(isRunning)
        {
            CURRENT_ACTION = RUN;
            if(left)
            {
                direction = "left";
            } else if(right)
            {
                direction = "right";
            }
        } else
        {
            animator.setAnimationState(player_gun[IDLE][CURRENT_DIRECTION] , 5);
            CURRENT_ACTION = IDLE;
            PREVIOUS_ACTION = IDLE;
        }

        if(PREVIOUS_ACTION != CURRENT_ACTION)
        {
            PREVIOUS_ACTION = CURRENT_ACTION;
            if(isRunning)
            {
                animator.setAnimationState(player_gun[CURRENT_ACTION][CURRENT_DIRECTION] , 10);
            }
        }
        //System.out.println(frameCounts);

    }

    private void changeDirection()
    {
        switch(direction)
        {
            case "left" : CURRENT_DIRECTION = LEFT; break;
            case "right": CURRENT_DIRECTION = RIGHT; break;
        }
    }

//    private void handleCollision(){
//        collisionOn = false;
//        onlyChangeDirection = false;
//        mp.cChecker.checkObjectCollsion(this);
//    }

    private void handlePosition()
    {
        collisionOn = false;
        if (up && isRunning) {
            if(right){newWorldX += 1; newWorldY -= 1;} else
            if(left){newWorldX -= 1 ; newWorldY -= 1;} else
                if(!down) newWorldY -= speed;
        }
        if (down && isRunning) {
            if(right){newWorldX += 1; newWorldY += 1;} else
            if(left){newWorldX -= 1 ; newWorldY += 1;} else
            if(!up) newWorldY += speed;
        }
        if (left && isRunning) {
            if(up){newWorldX -= 1; newWorldY -= 1;} else
            if(down){newWorldX -= 1 ; newWorldY +=1;} else
                if(!right) newWorldX -= speed;
        }
        if (right && isRunning) {
            if(up){newWorldX += 1; newWorldY -= 1;} else
            if(down){newWorldX += 1 ; newWorldY += 1;} else
            if(!left) newWorldX += speed;
        }

        mp.cChecker.checkCollisionWithInactiveObject(this);

        if(!collisionOn)
        {
            worldX = newWorldX;
            worldY = newWorldY;
        }
        newWorldX = worldX;
        newWorldY = worldY;

        GamePanel.camera.centerOn(worldX , worldY);
    }


}
