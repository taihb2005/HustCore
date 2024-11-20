package entity.mob;

import entity.Entity;
import entity.effect.Effect;
import map.GameMap;

import java.awt.*;

import static main.GamePanel.currentMap;
import static main.GamePanel.pFinder;

public class Monster extends Entity {
    protected int CURRENT_ACTION;
    protected int PREVIOUS_ACTION;
    protected int CURRENT_DIRECTION;
    protected int CURRENT_FRAME;

    protected boolean isIdle;
    protected boolean isShooting;
    protected boolean isRunning;

    public boolean onPath = false;
    public Effect effectDeal;

    public int expDrop = 0;

    public void checkCollision(){
        collisionOn = false;
        currentMap.cChecker.checkCollisionWithEntity(this , currentMap.inactiveObj);
        currentMap.cChecker.checkCollisionWithEntity(this , currentMap.npc);
        currentMap.cChecker.checkCollisionWithEntity(this , currentMap.activeObj);
    }

    public void damagePlayer(){

    }

    public void searchPath(int goalCol, int goalRow)
    {
        int startCol = (worldX + solidArea1.x) / GameMap.childNodeSize;
        int startRow = (worldY + solidArea1.y) / GameMap.childNodeSize;
        pFinder.setNodes(startCol,startRow,goalCol,goalRow);
        if(pFinder.search())
        {
            //Next WorldX and WorldY
            if(pFinder.pathList.isEmpty()){
                onPath = false;
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
            // for following player, disable this. It should be enabled when npc walking to specified location
//            int nextCol = gp.pFinder.pathList.get(0).col;
//            int nextRow = gp.pFinder.pathList.get(0).row;
//            if(nextCol == goalCol && nextRow == goalRow)
//            {
//                onPath = false;
//            }
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2) {

    }
}
