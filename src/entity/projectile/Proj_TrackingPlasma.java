package entity.projectile;

import graphics.Sprite;
import map.GameMap;

import java.awt.*;

import static map.GameMap.childNodeSize;

public class Proj_TrackingPlasma extends Projectile {
    private boolean onPath = false; // Kiểm tra xem viên đạn có đang di chuyển theo đường tìm được không
    private int timeCount = 0;
    public Proj_TrackingPlasma(GameMap mp) {
        super(mp);
        name = "TrackingPlasma";
        width = 64;
        height = 64;
        maxHP = 200;
        speed = 1;
        base_damage = 10;
        active = false;
        slowDuration = 180;
        manaCost = 20;
        direction = "right";
        hitbox = new Rectangle(28 , 30 , 12 , 12);
        solidArea1 = hitbox;
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        super.setDefaultSolidArea();
        getImage();
        projectile_animator.setAnimationState(projectile_sprite[CURRENT_DIRECTION] , 20);
    }

    private void getImage(){
        projectile_sprite = new Sprite("/entity/projectile/tracking_bullet.png" , width , height).getSpriteArray();
    }

    @Override
    public void update() {
        if (active) {
            // Lấy vị trí của người chơi (mục tiêu)
            int playerCol = (mp.player.worldX + mp.player.solidArea1.x) / childNodeSize;
            int playerRow = (mp.player.worldY + mp.player.solidArea1.y) / childNodeSize;

            // Tìm đường và di chuyển đến người chơi
            searchPath(playerCol, playerRow);
            move();
        }

        // Các logic kiểm tra khác (nếu cần, như va chạm hoặc hủy viên đạn)
        super.update();
    }

    private void move() {
        switch (direction) {
            case "up":
                worldY -= speed;
                break;
            case "down":
                worldY += speed;
                break;
            case "left":
                worldX -= speed;
                break;
            case "right":
                worldX += speed;
                break;
        }
    }

    @Override
    public void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea1.x) / childNodeSize;
        int startRow = (worldY + solidArea1.y) / childNodeSize;

        // Thiết lập các node và tìm đường
        pFinder.setNodes(startCol, startRow, goalCol, goalRow);
        if (pFinder.search()) {
            onPath = true;
            if (!pFinder.pathList.isEmpty()) {
                // Lấy vị trí tiếp theo trong pathList
                int nextX = pFinder.pathList.get(0).col * childNodeSize;
                int nextY = pFinder.pathList.get(0).row * childNodeSize;

                // Vị trí hiện tại của viên đạn
                int enLeftX = worldX + solidArea1.x;
                int enRightX = worldX + solidArea1.x + solidArea1.width;
                int enTopY = worldY + solidArea1.y;
                int enBottomY = worldY + solidArea1.y + solidArea1.height;

                // Xác định hướng di chuyển dựa trên tọa độ
                if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + childNodeSize) {
                    direction = "up";
                } else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + childNodeSize) {
                    direction = "down";
                } else if (enTopY >= nextY && enBottomY < nextY + childNodeSize) {
                    if (enLeftX > nextX) {
                        direction = "left";
                    } else if (enLeftX < nextX) {
                        direction = "right";
                    }
                } else if (enTopY > nextY && enLeftX > nextX) {
                    // Lên trên hoặc sang trái
                    direction = "up";
                    worldY -= speed;
                    checkCollision();
                    if (collisionOn) {
                        direction = "left";
                    }
                    worldY += speed;
                } else if (enTopY > nextY && enLeftX < nextX) {
                    // Lên trên hoặc sang phải
                    direction = "up";
                    worldY -= speed;
                    checkCollision();
                    if (collisionOn) {
                        direction = "right";
                    }
                    worldY += speed;
                } else if (enTopY < nextY && enLeftX > nextX) {
                    // Xuống dưới hoặc sang trái
                    direction = "down";
                    worldY += speed;
                    checkCollision();
                    if (collisionOn) {
                        direction = "left";
                    }
                    worldY -= speed;
                } else if (enTopY < nextY && enLeftX < nextX) {
                    // Xuống dưới hoặc sang phải
                    direction = "down";
                    worldY += speed;
                    checkCollision();
                    if (collisionOn) {
                        direction = "right";
                    }
                    worldY -= speed;
                }
            }
        } else {
            // Không tìm được đường đi
            onPath = false;
        }
    }
}
