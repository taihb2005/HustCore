package entity;

import graphics.Sprite;
import util.Camera;
import graphics.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.*;
import util.Camera;

public class Player extends Entity{

    final int IDLE_DOWN = 0;
    final int IDLE_RIGHT = 1;
    final int IDLE_LEFT = 2;
    final int IDLE_UP = 3;
    public float drawX, drawY;

    final int RUNNING_DOWN = 4;
    final int RUNNING_RIGHT = 5;
    final int RUNNING_LEFT = 6;
    final int RUNNING_UP = 7;

    private boolean isRunning;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    public final float screenX, screenY;

    private String dir;


    final private BufferedImage[][] player_sprite;

    private int currentAnimationState;
    private int currentFrames;

    final protected Animation animator = new Animation();

    public Player(Sprite entity_sprite, int x, int y, int speed) {
        super(entity_sprite, x, y , speed);

        currentFrames = 0;
        screenX = windowWidth/2 - tileSize;
        screenY = windowHeight/2 - tileSize;
        up = down = left = right = false;
        dir = "down";

        player_sprite = entity_sprite.getSpriteArray();

        currentAnimationState = IDLE_DOWN; //Initial set to down animation
        animator.setAnimationState(player_sprite[IDLE_DOWN] , 5);
    }

    @Override
    public void update()
    {
        switch(dir)
        {
            case "up" : currentAnimationState = IDLE_UP ; break;
            case "down": currentAnimationState = IDLE_DOWN ; break;
            case "left": currentAnimationState = IDLE_LEFT ; break;
            case "right": currentAnimationState = IDLE_RIGHT; break;
        }
        keyInput();

        handlePosition();

        handleAnimationState();



        animator.update();
        currentFrames = animator.getCurrentFrames();
    }


    @Override
    public void render(Graphics2D g2) {

    }


    @Override
    public void render(Graphics2D g2, Camera camera)
    {
        drawX = camera.worldToScreenX(worldX);
        drawY = camera.worldToScreenY(worldY);
        camera.update((int)worldX, (int)worldY, currentMap.getMapWidth(), currentMap.getMapHeight());
        g2.drawImage(player_sprite[currentAnimationState][currentFrames] ,
                (int)drawX , (int)drawY , 48*2, 48*2, null);
    }

    private void keyInput()
    {
        up = keyHandler.getUpstate();
        down = keyHandler.getDownstate();
        left = keyHandler.getLeftstate();
        right = keyHandler.getRightstate();

        isRunning = up | down | left | right;
    }

    private void handleAnimationState()
    {
        if(up && isRunning)
        {
            currentAnimationState = RUNNING_UP;
            dir = "up";

        } else if(down && isRunning)
        {
            currentAnimationState = RUNNING_DOWN;
            dir = "down";
        } else if(left && isRunning)
        {
            currentAnimationState = RUNNING_LEFT;
            dir = "left";
        } else if(right && isRunning)
        {
            currentAnimationState = RUNNING_RIGHT;
            dir = "right";
        }
    }

    private void handlePosition()
    {
        if(up && isRunning)
        {
            if (worldY - speed >= -tileSize) worldY -= speed;
            else worldY = -tileSize;
        } else if(down && isRunning)
        {
            if (worldY + speed <= currentMap.getMapHeight()-tileSize*2) worldY += speed;
            else worldY = currentMap.getMapHeight()-tileSize*2;
        } else if(left && isRunning)
        {
            if (worldX - speed >= -tileSize/2) worldX -= speed;
            else worldX = -tileSize/2;
        } else if(right && isRunning)
        {
            if (worldX + speed <= currentMap.getMapWidth()-tileSize*1.5) worldX += speed;
            else worldX = (float) (currentMap.getMapWidth()-tileSize*1.5);
        }
    }


}
