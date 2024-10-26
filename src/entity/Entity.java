package entity;

import java.awt.*;

public abstract class Entity {

    public int worldX, worldY;
    public int newWorldX , newWorldY;
    public String direction;
    public int speed;

    public boolean collisionOn = false;

    public int width;
    public int height;

    public Rectangle solidArea1;
    public Rectangle solidArea2;
    public int solidAreaDefaultX1 = 0;
    public int solidAreaDefaultY1 = 0;
    public int solidAreaDefaultX2 = 0;
    public int solidAreaDefaultY2 = 0;

    public Entity(){}

    public Entity(int x , int y , int speed)
    {
        this.worldX = x;
        this.worldY = y;
        this.speed = speed;
    }


    public abstract void update();
    public abstract void render(Graphics2D g2);
}