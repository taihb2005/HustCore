package entity.projectile;

import entity.Direction;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;
import java.util.HashMap;

import static map.GameMap.childNodeSize;

public class Proj_TrackingPlasma extends Projectile {
    private static final Sprite sprite = new Sprite(AssetPool.getImage("tracking_bullet.png"));
    private static final HashMap<Direction, Animation> animations = new HashMap<>();

    public static void load(){
        for(Direction direction: Direction.values()){
            int row = switch (direction){
                case RIGHT -> 0;
                case LEFT -> 1;
                case UP -> 2;
                case DOWN -> 3;
            };
            animations.put(direction,
                    new Animation(sprite.getSpriteArrayRow(row), 10, true));

        }
        System.out.println("hehe");
    }

    private void setState(){
        if(currentDirection != lastDirection){
            lastDirection = currentDirection;
            currentAnimation = animations.get(currentDirection).clone();
        }
    }

    private boolean onPath = false; // Kiểm tra xem viên đạn có đang di chuyển theo đường tìm được không
    private int timeCount = 0;
    public Proj_TrackingPlasma(GameMap mp) {
        super(mp);
        name = "TrackingPlasma";
        width = 64;
        height = 64;
        maxHP = 200;
        speed = 1;
        baseDamage = 10;
        active = false;
        slowDuration = 180;
        manaCost = 20;
        direction = "right";
        currentDirection = setDirection(direction);
        lastDirection = setDirection(direction);
        hitbox = new Rectangle(28 , 30 , 12 , 12);
        solidArea1 = hitbox;
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        super.setDefaultSolidArea();
        //getImage();
        currentAnimation = animations.get(currentDirection).clone();
    }

    @Override
    public void update() {
        if (active) {
            int playerCol = ((int)mp.player.position.x + mp.player.solidArea1.x) / childNodeSize;
            int playerRow = ((int)mp.player.position.y + mp.player.solidArea1.y) / childNodeSize;

            searchPath(playerCol, playerRow);
            move();
        }

        super.update();
        setState();
    }

    private void move() {
        switch (direction) {
            case "up":
                position.y -= speed;
                break;
            case "down":
                position.y += speed;
                break;
            case "left":
                position.x -= speed;
                break;
            case "right":
                position.x += speed;
                break;
        }
    }

    @Override
    public void searchPath(int goalCol, int goalRow) {
        executor.execute(() -> {
            float worldX = position.x;
            float worldY = position.y;

            int startCol = (int)((worldX + solidArea1.x) / childNodeSize);
            int startRow = (int)((worldY + solidArea1.y) / childNodeSize);

            synchronized (pFinder) {
                pFinder.setNodes(startCol, startRow, goalCol, goalRow);

                if (pFinder.search()) {
                    onPath = true;

                    if (!pFinder.pathList.isEmpty()) {
                        int nextX = pFinder.pathList.get(0).col * childNodeSize;
                        int nextY = pFinder.pathList.get(0).row * childNodeSize;

                        float enLeftX = worldX + solidArea1.x;
                        float enRightX = worldX + solidArea1.x + solidArea1.width;
                        float enTopY = worldY + solidArea1.y;
                        float enBottomY = worldY + solidArea1.y + solidArea1.height;

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
                            direction = "up";
                            position.y -= speed;
                            checkCollision();
                            if (collisionOn) {
                                direction = "left";
                            }
                            position.y += speed;
                        } else if (enTopY > nextY && enLeftX < nextX) {
                            direction = "up";
                            position.y -= speed;
                            checkCollision();
                            if (collisionOn) {
                                direction = "right";
                            }
                            position.y += speed;
                        } else if (enTopY < nextY && enLeftX > nextX) {
                            direction = "down";
                            position.y += speed;
                            checkCollision();
                            if (collisionOn) {
                                direction = "left";
                            }
                            position.y -= speed;
                        } else if (enTopY < nextY && enLeftX < nextX) {
                            direction = "down";
                            position.y += speed;
                            checkCollision();
                            if (collisionOn) {
                                direction = "right";
                            }
                            position.y -= speed;
                        }
                    }
                } else {
                    onPath = false;
                }
            }
        });
    }

}
