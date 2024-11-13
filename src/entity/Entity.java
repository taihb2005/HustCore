package entity;

import entity.projectile.Obj_BasicGreenProjectile;
import entity.projectile.Obj_BasicProjectile;
import entity.projectile.Projectile;
import graphics.Sprite;
import main.GamePanel;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.*;

public abstract class Entity {
    public static EffectManager effManager = new EffectManager();
    public String name;
    //POSITION
    public int worldX, worldY;
    public int newWorldX , newWorldY;
    public String direction;
    public int speed;
    public int last_speed;
    //BOOLEAN
    public boolean collisionOn = false;
    public boolean isInteracting = false;
    public boolean isOpening = false;
    public boolean isInvincible = false;
    public boolean isDying = false;
    public boolean canbeDestroyed = false;
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
    public String projectile_name;
    public Projectile projectile ;
    public int lightRadius;
    public int shootAvailableCounter = 0;
    public int invincibleCounter = 0;
    public int invincibleDuration;//Thời gian bất tử

    //PROJECTILE STATUS
    public int manaCost;
    public boolean active;

    //ENEMY STATUS
    public int expDrop = 0;

    //ENTITY EFFECT
    public Effect getEffect = Effect.NONE;
    public int effectDuration;

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
    public void die(){
        isDying = true;
        hitbox = new Rectangle(0 , 0 , 0 ,0);
    }
    public void updateInvincibility(){
        if(isInvincible){
            invincibleCounter++;
            if(invincibleCounter >= invincibleDuration){
                invincibleCounter = 0;
                isInvincible = false;
            }
        }
    }
    public void updateEffect(){
        if(getEffect != Effect.NONE){
            switch(getEffect){
                case SLOW :
                    if(effManager.slowEffect()) {
                        getEffect = Effect.NONE;
                        speed = last_speed;
                    }
                    break;
                case SPEED_BOOST:
                    if(effManager.speed_boostEffect()){
                        getEffect = Effect.NONE;
                        speed = last_speed;
                    }
                    break;
                case BLIND:
                    if(effManager.blindEffect()){
                        getEffect = Effect.NONE;
                        environmentManager.lighting.transit = true;
                        environmentManager.lighting.fadeOut = true;
                    }
                    break;
            }
        }
    }
    public void renderSlowEffect(Graphics2D g2){
        BufferedImage slow = new Sprite("/effect/slow.png" , 32 , 32).getSpriteSheet();
        g2.drawImage(slow , worldX - camera.getX() + 35 , worldY - camera.getY() + 20, null);
    }
    public void renderSpeedBoostEffect(Graphics2D g2){
        BufferedImage speed_boost = new Sprite("/effect/speed_boost.png").getSpriteSheet();
        g2.drawImage(speed_boost , worldX - camera.getX() + 35 , worldY - camera.getY() + 20, null);
    }
    public void renderBlindEffect(Graphics2D g2){
        BufferedImage blind = new Sprite("/effect/blind.png").getSpriteSheet();
        g2.drawImage(blind , worldX - camera.getX() + 35 , worldY - camera.getY() + 20, null);
    }
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