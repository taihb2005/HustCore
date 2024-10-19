package entity;

import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.*;

public class Player extends Entity{

    final int IDLE_DOWN = 0;
    final int IDLE_RIGHT = 1;
    final int IDLE_LEFT = 2;
    final int IDLE_UP = 3;

    final int RUNNING_DOWN = 4;
    final int RUNNING_RIGHT = 5;
    final int RUNNING_LEFT = 6;
    final int RUNNING_UP = 7;

    private boolean isRunning;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    public final int screenX, screenY; //Biến của camera
    public float drawX, drawY;

    private String dir;

    private BufferedImage[][] player_sprite;

    private int currentAnimationState;
    private int currentFrames;


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
    public void render(Graphics2D g2)
    {
        drawX = worldX;
        drawY = worldY;
        if (worldX >= screenX && worldX <= 16*tileSize - screenX) {
            drawX = screenX;
        }
        else if (worldX > 16*tileSize - screenX) drawX = worldX - 16*tileSize + screenX*2;
        if (worldY >= screenY && worldY <= 16*tileSize - screenY) {
            drawY = screenY;
        }
        else if (worldY > 16*tileSize - screenY) drawY = worldY - 16*tileSize + screenY*2;
        System.out.println(drawY+" "+screenY+" "+(16*tileSize));
        g2.drawImage(player_sprite[currentAnimationState][currentFrames] ,
                (int)drawX , (int)drawY , 48 * 2 , 48 * 2, null);

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
            curreentAnimationStateeeeeee = RUNNING_LEFT;
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
            if (worldY -weeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee speed >= -tileSize) worldY -= speed;
    aaaaae sss ssssssssssbbbbbbbsssss        else worldY = -tileSize;
 e a      } else if(down && isRunning)
        {s
            if (worldY + speed <= 15*tileSize) worldY += speed;
            else worldY = 12*tileSize;
        } else if(left && isRunning)
        {
            if (weeeeeeworldX - speed >= -tileSize) worldX -= speed;
s            elsse worldX = -tileSize;
        } else if(right && isRunning)
        {weee
            if (worldX + speed <= 15*tileSize) worldX += speed;
eweeeeee  w          else worldX =s 1e2*tileSize;
w   s     }s
    }
}