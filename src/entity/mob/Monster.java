package entity.mob;

import ai.PathFinder;
import entity.Entity;
import entity.effect.Effect;
import entity.object.Obj_Heart;
import map.GameMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static main.GamePanel.*;

public class Monster extends Entity {
    public GameMap mp;

    protected boolean isIdle;
    protected boolean isShooting;
    protected boolean isRunning;
    protected boolean isDetectPlayer;
    public boolean canChangeState = false;

    public boolean onPath = false;
    public boolean getAggro = false;
    public Effect effectDealOnTouch;
    public Effect effectDealByProjectile;

    public final ArrayList<String> validDirection = new ArrayList<>();

    protected int SHOOT_INTERVAL;
    int counter = 0;
    public boolean drawHPBar;

    public int expDrop = 0;

    public Monster(GameMap mp){
        super();
        this.mp = mp;
    }
    public Monster(GameMap mp , int x , int y){
        super(x , y);
        this.mp = mp;

        this.pFinder = new PathFinder(mp);
    }

    public void projectileCauseEffect(){
        effectDealByProjectile.clone().add();
    }
    
    public void reactForDamage(){
        direction = getOppositeDirection(mp.player.projectile.direction);
        decideToMove();
    }

    public void handleStatus(){
        if(shootAvailableCounter < SHOOT_INTERVAL){
            shootAvailableCounter++;
        }
        if(shootAvailableCounter > SHOOT_INTERVAL) shootAvailableCounter = SHOOT_INTERVAL;
        updateInvincibility();
    }

    public void facePlayer() {
        int playerCol = (mp.player.worldX + mp.player.solidArea1.x) / 32;
        int posCol = (worldX + solidArea1.x) / 32;

        if(playerCol > posCol) direction = "right"; else
            direction = "left";

    }

    public void damagePlayer(){
        boolean contactPlayer = mp.cChecker.checkPlayer(this);
        if(name.equals("Effect Dealer") && contactPlayer) effectDealOnTouch.add(); else
        if(contactPlayer && !mp.player.isInvincible){
            mp.player.isInvincible = true;
            mp.player.receiveDamage(this);
            effectDealOnTouch.clone().add();
        }
    }

    public boolean checkForValidDirection(){
        validDirection.clear();
        newWorldX = worldX;
        newWorldY = worldY;
        //UP
        newWorldY -= speed;
        checkCollision();
        if(!collisionOn) validDirection.add("up");
        newWorldY += speed;

        //DOWN
        newWorldY += speed;
        checkCollision();
        if(!collisionOn) validDirection.add("down");
        newWorldY -= speed;

        //LEFT
        newWorldX -=speed;
        checkCollision();
        if(!collisionOn) validDirection.add("left");
        newWorldX += speed;

        //RIGHT
        newWorldX += speed;
        checkCollision();
        if(!collisionOn) validDirection.add("right");
        newWorldX -= speed;

        return !validDirection.isEmpty();
    }

    public String getValidDirection(){
        int n = validDirection.size();
        Random gen = new Random();
        int i = gen.nextInt(n);
        return validDirection.get(i);
    }

    @Override
    public void searchPath(int goalCol, int goalRow) {
        executor.execute( () -> {
            int startCol = (worldX + solidArea1.x) / GameMap.childNodeSize;
            int startRow = (worldY + solidArea1.y) / GameMap.childNodeSize;
            synchronized (pFinder) {
                pFinder.setNodes(startCol, startRow, goalCol, goalRow);
                if (pFinder.search()) {
                    if (pFinder.pathList.isEmpty()) {
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
                } else {
                    getAggro = false;
                    onPath = false;
                    up = down = right = left = false;
                    isRunning = false;
                    speed = lastSpeed;
                }
            }
        });
    }


    public void spawnHeart() {
        Obj_Heart heart = new Obj_Heart(mp);
        heart.worldX = worldX + 16; heart.worldY = worldY + 16;
        mp.addObject(heart, mp.activeObj);
    }

    public boolean counterReached(int target)
    {
        boolean counterReached = false;
        counter++;
        if(counter > target)
        {
            counterReached = true;
            counter = 0;
        }
        return counterReached;
    }

    public void resetCounter(){
        drawHPBar = true;
        counter = 0;
    }

    public void kill(){
        currentHP = 0;
    }

    public void renderHPBar(Graphics2D g2 , int offsetX , int offsetY){
        if(drawHPBar) {
            int maxHpWidth = 30;
            int currentHpWidth = (int) ((double) currentHP / maxHP * maxHpWidth);
            g2.setColor(Color.WHITE);
            g2.fillRect(worldX - camera.getX() + offsetX, worldY - camera.getY() + offsetY, maxHpWidth, 5);
            g2.setColor(Color.RED);
            g2.fillRect(worldX - camera.getX() + offsetX, worldY - camera.getY() + offsetY, currentHpWidth, 5);
            g2.setColor(Color.BLACK);
            g2.drawRect(worldX - camera.getX() + offsetX, worldY - camera.getY() + offsetY, maxHpWidth, 5);
            if(counterReached(600)) drawHPBar = false;
        }
    }

    public void render(Graphics2D g2) {
        int maxHpWidth = 30;
        int currentHpWidth = (int) ((double) currentHP/maxHP * maxHpWidth);
        g2.setColor(Color.RED);
        g2.fillRect(worldX-camera.getX()+17, worldY - camera.getY() , currentHpWidth, 5);
        g2.setColor(Color.BLACK);
        g2.drawRect(worldX-camera.getX()+17, worldY - camera.getY() , maxHpWidth, 5);
    }
}
