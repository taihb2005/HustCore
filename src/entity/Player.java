package entity;

import graphics.Sprite;
import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.keyHandler;

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

    private String dir;


    private BufferedImage[][] player_sprite;

    private int currentAnimationState;
    private int currentFrames;


    public Player(Sprite entity_sprite, int x, int y) {
        super(entity_sprite, x, y);
        currentFrames = 0;

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

        handleAnimationState();
        animator.update();
        currentFrames = animator.getCurrentFrames();
    }


    @Override
    public void render(Graphics2D g2)
    {
        g2.drawImage(player_sprite[currentAnimationState][currentFrames] , (int)x , (int)y , 48 * 2 , 48 * 2, null);
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
}
