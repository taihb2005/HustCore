package entity;

import graphics.Sprite;
import graphics.Animation;
import util.Camera;

import java.awt.*;

public abstract class Entity {

    public int worldX, worldY;
    public String direction;
    public int speed;

    public boolean collisionOn = false;

    protected int width;
    protected int height;

    public Rectangle solidArea;
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    public Entity(){};

    public Entity(int x , int y , int speed)
    {
        this.worldX = x;
        this.worldY = y;
        this.speed = speed;
    }


    public abstract void update();
    public abstract void render(Graphics2D g2);
    public abstract void render(Graphics2D g2, Camera camera);
}