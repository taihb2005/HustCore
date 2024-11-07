package entity;

import java.awt.*;
import static main.GamePanel.ui;

public abstract class Entity {
    public String name;
    //POSITION
    public int worldX, worldY;
    public int newWorldX , newWorldY;
    public String direction;
    public int speed;
    //BOOLEAN
    public boolean collisionOn = false;
    public  boolean isInteracting = false;
    public boolean isOpening = false;
    public boolean canbeDestroyed;
    //SPRITE SIZE
    public int width;
    public int height;
    //SOLID AREA
    public Rectangle solidArea1;
    public Rectangle solidArea2;
    public Rectangle shootingArea;
    public Rectangle interactionDetectionArea;
    public int solidAreaDefaultX1 = 0;
    public int solidAreaDefaultY1 = 0;
    public int solidAreaDefaultX2 = 0;
    public int solidAreaDefaultY2 = 0;

    //CHARACTER STATUS
    public int maxHP;
    public int currentHP;
    public int maxMana;
    public int currentMana;
    public int invincibleCounter = 0;  //Thời gian bất tử

    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

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
        ui.target = entity;
    }


    public void talk(){};
    public abstract void update();
    public abstract void render(Graphics2D g2);
    public void dispose()
    {
        solidArea1 = null;
        solidArea2 = null;
        interactionDetectionArea = null;
        dialogues = null;
    }
}