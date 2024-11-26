package entity;

import entity.effect.EffectType;
import entity.projectile.Projectile;
import graphics.Sprite;
import main.GameState;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.*;

public abstract class Entity {
    public String name;
    //POSITION
    public int worldX, worldY;
    public int newWorldX, newWorldY;
    public String direction;
    public int speed;
    public int last_speed;
    //BOOLEAN
    public boolean collisionOn = false;
    public boolean isInteracting = false;
    public boolean isOpening = false;
    public boolean isInvincible = false;
    public boolean isDying = false;
    public boolean isCollected = false;
    public boolean canbeDestroyed = false;
    public boolean onPath = false;
    //SPRITE SIZE
    public int width;
    public int height;
    //SOLID AREA
    public Rectangle solidArea1;
    public Rectangle solidArea2;
    public Rectangle hitbox;
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
    public Projectile projectile;
    public int shootAvailableCounter = 0;
    public int invincibleCounter = 0;
    public int invincibleDuration;//Thời gian bất tử

    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    public String[][] dialogues = new String[5][30];

    public int dialogueIndex;
    public int dialogueSet = -1;

    public Entity() {
    }

    public Entity(int x, int y) {
        this.worldX = x;
        this.worldY = y;
        this.newWorldX = x;
        this.newWorldY = y;
    }

    public void setDefaultSolidArea() {
        if (solidArea1 != null) {
            solidAreaDefaultX1 = solidArea1.x;
            solidAreaDefaultY1 = solidArea1.y;
        }

        if (solidArea2 != null) {
            solidAreaDefaultX2 = solidArea2.x;
            solidAreaDefaultY2 = solidArea2.y;
        }
    }

    public void startDialogue(Entity entity, int dialogueSet) {
        gameState = GameState.DIALOGUE_STATE;
        ui.target = entity;
        ui.target.dialogueSet = dialogueSet;
    }

    public void talk() {
    }

    public void set() {
    }

    public void projectileCauseEffect(){

    }

    public void die() {
        isDying = true;
        hitbox = new Rectangle(0, 0, 0, 0);
    }

    public void updateInvincibility() {
        if (isInvincible) {
            invincibleCounter++;
            if (invincibleCounter >= invincibleDuration) {
                invincibleCounter = 0;
                isInvincible = false;
            }
        }
    }

    public void setHitbox() {
    }

    public void checkCollision(){
        collisionOn = false;
        currentMap.cChecker.checkCollisionWithEntity(this , currentMap.inactiveObj);
        currentMap.cChecker.checkCollisionWithEntity(this , currentMap.npc);
        currentMap.cChecker.checkCollisionWithEntity(this , currentMap.activeObj);
        currentMap.cChecker.checkCollisionWithEntity(this , currentMap.enemy);
    }

    public void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea1.x) / GameMap.childNodeSize;
        int startRow = (worldY + solidArea1.y) / GameMap.childNodeSize;
        pFinder.setNodes(startCol,startRow,goalCol,goalRow);
        if(pFinder.search())
        {
            //Next WorldX and WorldY
            if(pFinder.pathList.isEmpty()){
                onPath = false;
                up = down = left = right = false;
            } else {
                int nextX = pFinder.pathList.get(0).col * GameMap.childNodeSize;
                int nextY = pFinder.pathList.get(0).row * GameMap.childNodeSize;


                //Entity's solidArea position
                int enLeftX = worldX + solidArea1.x;
                int enRightX = worldX + solidArea1.x + solidArea1.width;
                int enTopY = worldY + solidArea1.y;
                int enBottomY = worldY + solidArea1.y + solidArea1.height;

                // TOP PATH
                if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + GameMap.childNodeSize) {
                    direction = "up";
                }
                // BOTTOM PATH
                else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + GameMap.childNodeSize) {
                    direction = "down";
                }
                // RIGHT - LEFT PATH
                else if (enTopY >= nextY && enBottomY < nextY + GameMap.childNodeSize) {
                    //either left or right
                    // LEFT PATH
                    if (enLeftX > nextX) {
                        direction = "left";
                    }
                    // RIGHT PATH
                    if (enLeftX < nextX) {
                        direction = "right";
                    }
                }
                //OTHER EXCEPTIONS
                else if (enTopY > nextY && enLeftX > nextX) {
                    // up or left
                    direction = "up";
                    newWorldY -= speed;
                    checkCollision();
                    //System.out.println(collisionOn);
                    if (collisionOn) {
                        direction = "left";
                    }
                    newWorldY += speed;
                } else if (enTopY > nextY && enLeftX < nextX) {
                    // up or right
                    direction = "up";
                    newWorldY -= speed;
                    checkCollision();
                    if (collisionOn) {
                        direction = "right";
                    }
                    newWorldY += speed;
                } else if (enTopY < nextY && enLeftX > nextX) {
                    // down or left
                    direction = "down";
                    newWorldY += speed;
                    checkCollision();
                    if (collisionOn) {
                        direction = "left";
                    }
                    newWorldY -= speed;
                } else if (enTopY < nextY && enLeftX < nextX) {
                    // down or right
                    direction = "down";
                    newWorldY += speed;
                    checkCollision();
                    if (collisionOn) {
                        direction = "right";
                    }
                    newWorldY -= speed;
                }
            }
        }
    }

    public abstract void update();

    public abstract void render(Graphics2D g2);

    public void dispose() {
        solidArea1 = null;
        solidArea2 = null;
        interactionDetectionArea = null;
        dialogues = null;
    }

    public String getOppositeDirection(String direction){
        return  switch (direction) {
            case "left" -> "right";
            case "right" -> "left";
            case "up" -> "down";
            case "down" -> "up";
            default -> "";
        };
    }

}