package entity.mob;

import ai.Node;
import entity.Entity;
import entity.effect.Effect;
import entity.projectile.Projectile;
import map.GameMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static main.GamePanel.currentMap;
import static main.GamePanel.pFinder;

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

    public int expDrop = 0;

    public Monster(GameMap mp){
        super();
        this.mp = mp;
    }
    public Monster(GameMap mp , int x , int y){
        super(x , y);
        this.mp = mp;
    }

    public void projectileCauseEffect(){
        effectDealByProjectile.add();
    }

    public void decideToMove() {
        up = down = left = right = false;
        switch (direction) {
            case "right":
                right = true;
                break;
            case "left":
                left = true;
                break;
            case "down":
                down = true;
                break;
            case "up":
                up = true;
                break;
        }
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
        if(contactPlayer && !mp.player.isInvincible){
            mp.player.isInvincible = true;
            mp.player.receiveDamage(this);
            effectDealOnTouch.add();
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
        // Lấy tọa độ bắt đầu
        int startCol = (worldX + solidArea1.x) / GameMap.childNodeSize;
        int startRow = (worldY + solidArea1.y) / GameMap.childNodeSize;

        // Khởi tạo Pathfinder (hoặc bạn có thể tái sử dụng)
        pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        // Nếu tìm thấy đường đi
        if (pFinder.search()) {
            // Lấy danh sách các node trong đường đi
            if (!pFinder.pathList.isEmpty()) {
                Node nextNode = pFinder.pathList.get(0);

                // Chuyển tọa độ node sang tọa độ thế giới
                int nextX = nextNode.col * GameMap.childNodeSize;
                int nextY = nextNode.row * GameMap.childNodeSize;

                // Xác định hướng di chuyển
                if (worldY + solidArea1.y > nextY) {
                    direction = "up";
                } else if (worldY + solidArea1.y < nextY) {
                    direction = "down";
                } else if (worldX + solidArea1.x > nextX) {
                    direction = "left";
                } else if (worldX + solidArea1.x < nextX) {
                    direction = "right";
                }
            }
        } else {
            // Nếu không tìm thấy đường đi
            onPath = false;
            up = down = right = left = false;
            isRunning = false;
            speed = last_speed;
        }
    }


    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2) {

    }
}
