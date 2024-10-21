package entity;

import graphics.Sprite;
import map.GameMap;
import util.Camera;
import graphics.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.*;

public class Player extends Entity{

    GameMap mp;

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

    public int drawX, drawY;
    public final int screenX, screenY;


    private BufferedImage[][] player_sprite;

    private int currentAnimationState;
    private int currentFrames;

    final protected Animation animator = new Animation();

    public Player(GameMap mp) {
        super();
        this.mp = mp;
        width = 48 * 2;
        height = 48 * 2;

        solidArea = new Rectangle(42 , 72 , 14 , 9);
        solidAreaDefaultX = 42;
        solidAreaDefaultY = 72;

        screenX = windowWidth/2 - tileSize;
        screenY = windowHeight/2 - tileSize;

        getPlayerImages();
        setDefaultValue();
    }

    private void setDefaultValue()
    {
        worldX = 500;
        worldY = 500;
        speed = 5;

        up = down = left = right = false;
        direction = "down";

        currentFrames = 0;
        currentAnimationState = IDLE_DOWN; //Initial set to down animation
        animator.setAnimationState(player_sprite[IDLE_DOWN] , 5);
    }

    private void getPlayerImages()
    {
        player_sprite = new Sprite("/entity/player/player.png").getSpriteArray();
    }



    @Override
    public void update()
    {
        handleDirection();
        keyInput();
        handleCollsion();
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
        camera.update(worldX, worldY, currentMap.getMapWidth(), currentMap.getMapHeight());
        g2.drawImage(player_sprite[currentAnimationState][currentFrames] ,
                drawX , drawY, width, height, null);
//        g2.setColor(Color.BLUE);
//        g2.fillRect(drawX + solidArea.x, drawY + solidArea.y, solidArea.width, solidArea.height);
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
            direction = "up";

        } else if(down && isRunning)
        {
            currentAnimationState = RUNNING_DOWN;
            direction = "down";
        } else if(left && isRunning)
        {
            currentAnimationState = RUNNING_LEFT;
            direction = "left";
        } else if(right && isRunning)
        {
            currentAnimationState = RUNNING_RIGHT;
            direction = "right";
        }
    }

    private void handleDirection()
    {
        switch(direction)
        {
            case "up" : currentAnimationState = IDLE_UP ; break;
            case "down": currentAnimationState = IDLE_DOWN ; break;
            case "left": currentAnimationState = IDLE_LEFT ; break;
            case "right": currentAnimationState = IDLE_RIGHT; break;
        }
    }

    private void handleCollsion(){
        collisionOn = false;
        int objTndex = mp.cChecker.checkObjectCollision(this , true);
    }

    private void handlePosition()
    {

        if(!collisionOn) {
            if (up && isRunning) {
                if (worldY - speed >= -tileSize) worldY -= speed;
                else worldY = -tileSize;
            } else if (down && isRunning) {
                if (worldY + speed <= currentMap.getMapHeight() - tileSize * 2) worldY += speed;
                else worldY = currentMap.getMapHeight() - tileSize * 2;
            } else if (left && isRunning) {
                if (worldX - speed >= -tileSize / 2) worldX -= speed;
                else worldX = -tileSize / 2;
            } else if (right && isRunning) {
                if (worldX + speed <= currentMap.getMapWidth() - tileSize * 2) worldX += speed;
                else worldX = (int) (currentMap.getMapWidth() - tileSize * 2);
            }
        }
    }


}
