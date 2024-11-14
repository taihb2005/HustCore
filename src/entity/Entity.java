package entity;

import entity.projectile.Projectile;

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
    public boolean isInteracting = false;
    public boolean isOpening = false;
    public boolean isInvincible = false;
    public boolean isDying = false;
    public boolean isCollected = false;
    public boolean canbeDestroyed;
    //SPRITE SIZE
    public int width;
    public int height;
    //SOLID AREA
    public Rectangle solidArea1;
    public Rectangle solidArea2;
    public Rectangle hitbox;
    public Rectangle shootingArea;
    public Rectangle interactionDetectionArea;
    public int solidAreaDefaultX1 = 0;
    public int solidAreaDefaultY1 = 0;
    public int solidAreaDefaultX2 = 0;
    public int solidAreaDefaultY2 = 0;

    //CHARACTER STATUS
    public int level;
    public int maxHP;
    public int currentHP;
    public int maxMana;
    public int currentMana;
    public int exp;
    public int strength;
    public int defense;
    public int damage;
    public Projectile projectile;
    public int shootAvailableCounter = 0;
    public int invincibleCounter = 0;
    public int invincibleDuration;//Thời gian bất tử

    //PROJECTILE STATUS
    public boolean active;

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
    public void set(){};
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