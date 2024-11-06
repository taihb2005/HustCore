package entity;

import map.GameMap;

import java.awt.*;
import static main.GamePanel.ui;

public abstract class Entity {
    public String name;

    public int worldX, worldY;
    public int newWorldX , newWorldY;
    public String direction;
    public int speed;

    public boolean collisionOn = false;
    public  boolean isInteracting = false;

    public int width;
    public int height;

    public Rectangle solidArea1;
    public Rectangle solidArea2;
<<<<<<< HEAD
    public Rectangle shootingArea;
=======
    public Rectangle interactionDetectionArea;
>>>>>>> 17fb25b8173f6cae191ce757bab79a78717fdc8e
    public int solidAreaDefaultX1 = 0;
    public int solidAreaDefaultY1 = 0;
    public int solidAreaDefaultX2 = 0;
    public int solidAreaDefaultY2 = 0;

    public String[] dialogues = new String[8];

    public int dialogueIndex;
    public int dialogueSet;

    public Entity(){}

    public Entity(int x , int y , int speed)
    {
        this.worldX = x;
        this.worldY = y;
        this.speed = speed;
    }

    public void setDefaultSolidArea()
    {
        if(solidArea1 != null)
        {
            solidAreaDefaultX1 = solidArea1.x;
            solidAreaDefaultY1 = solidArea1.y;
        }

        if(solidArea2 != null)
        {
            solidAreaDefaultX2 = solidArea2.x;
            solidAreaDefaultY2 = solidArea2.y;
        }
    }

    public void startDialogue(Entity entity)
    {
        ui.npc = entity;
    }


    public void talk(){};
    public abstract void update();
    public abstract void render(Graphics2D g2);
}