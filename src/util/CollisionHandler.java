package util;

import entity.Entity;
import map.GameMap;

import java.awt.*;

public class CollisionHandler {

    GameMap mp;

    public CollisionHandler(GameMap mp) {
        this.mp = mp;
    }

    public void checkCollisionWithInactiveObject(Entity entity) {
        for (int i = 0; i < mp.inactiveObj.size(); i++) {
            if (mp.inactiveObj.get(i) != null) {

                int newSolidAreaX1 = entity.newWorldX + entity.solidArea1.x;
                int newSolidAreaY1 = entity.newWorldY + entity.solidArea1.y;
                Rectangle tmp1 = new Rectangle(mp.inactiveObj.get(i).worldX + mp.inactiveObj.get(i).solidArea1.x , mp.inactiveObj.get(i).worldY + mp.inactiveObj.get(i).solidArea1.y,
                        mp.inactiveObj.get(i).solidArea1.width , mp.inactiveObj.get(i).solidArea1.height);
                Rectangle tmp = new Rectangle(newSolidAreaX1 , newSolidAreaY1, entity.solidArea1.width , entity.solidArea1.height);

                if(tmp.intersects(tmp1)) entity.collisionOn = true;

                if(mp.inactiveObj.get(i).solidArea2 != null)
                {
                    Rectangle tmp2 = new Rectangle(mp.inactiveObj.get(i).worldX + mp.inactiveObj.get(i).solidArea2.x , mp.inactiveObj.get(i).worldY + mp.inactiveObj.get(i).solidArea2.y,
                            mp.inactiveObj.get(i).solidArea2.width , mp.inactiveObj.get(i).solidArea2.height);
                    if(tmp.intersects(tmp2)) entity.collisionOn = true;

                }
            }
        }
    }

    public void checkCollisionWithActiveObject(Entity entity)
    {
        for (int i = 0; i < mp.activeObj.size(); i++) {
            if (mp.inactiveObj.get(i) != null) {

                int newSolidAreaX1 = entity.newWorldX + entity.solidArea1.x;
                int newSolidAreaY1 = entity.newWorldY + entity.solidArea1.y;
                Rectangle tmp1 = new Rectangle(mp.activeObj.get(i).worldX + mp.activeObj.get(i).solidArea1.x , mp.activeObj.get(i).worldY + mp.activeObj.get(i).solidArea1.y,
                        mp.activeObj.get(i).solidArea1.width , mp.activeObj.get(i).solidArea1.height);
                Rectangle tmp = new Rectangle(newSolidAreaX1 , newSolidAreaY1, entity.solidArea1.width , entity.solidArea1.height);

                if(tmp.intersects(tmp1)) entity.collisionOn = true;

                if(mp.activeObj.get(i).solidArea2 != null)
                {
                    Rectangle tmp2 = new Rectangle(mp.activeObj.get(i).worldX + mp.activeObj.get(i).solidArea2.x , mp.activeObj.get(i).worldY + mp.activeObj.get(i).solidArea2.y,
                            mp.activeObj.get(i).solidArea2.width , mp.activeObj.get(i).solidArea2.height);
                    if(tmp.intersects(tmp2)) entity.collisionOn = true;
                }
            }
        }
    }

    public void checkCollisionWithOnGroundEnemy(Entity entity)
    {
        for (int i = 0; i < mp.onGroundEnemy.size(); i++) {
            if (mp.inactiveObj.get(i) != null) {

                int newSolidAreaX1 = entity.newWorldX + entity.solidArea1.x;
                int newSolidAreaY1 = entity.newWorldY + entity.solidArea1.y;
                Rectangle tmp1 = new Rectangle(mp.onGroundEnemy.get(i).worldX + mp.onGroundEnemy.get(i).solidArea1.x , mp.onGroundEnemy.get(i).worldY + mp.onGroundEnemy.get(i).solidArea1.y,
                        mp.onGroundEnemy.get(i).solidArea1.width , mp.onGroundEnemy.get(i).solidArea1.height);
                Rectangle tmp = new Rectangle(newSolidAreaX1 , newSolidAreaY1, entity.solidArea1.width , entity.solidArea1.height);

                if(tmp.intersects(tmp1)) entity.collisionOn = true;

                if(mp.onGroundEnemy.get(i).solidArea2 != null)
                {
                    Rectangle tmp2 = new Rectangle(mp.onGroundEnemy.get(i).worldX + mp.onGroundEnemy.get(i).solidArea2.x , mp.onGroundEnemy.get(i).worldY + mp.onGroundEnemy.get(i).solidArea2.y,
                            mp.onGroundEnemy.get(i).solidArea2.width , mp.onGroundEnemy.get(i).solidArea2.height);
                    if(tmp.intersects(tmp2)) entity.collisionOn = true;

                }
            }
        }
    }

    public int checkInteractWithActiveObject(Entity entity , boolean isPlayer)
    {
        int index = -1;
        if(isPlayer) {
            for (int i = 0; i < mp.activeObj.size(); i++) {
                if (mp.activeObj.get(i) != null) {

                    int newSolidAreaX1 = entity.newWorldX + entity.solidArea1.x;
                    int newSolidAreaY1 = entity.newWorldY + entity.solidArea1.y;
                    Rectangle tmp1 = new Rectangle(mp.activeObj.get(i).worldX + mp.activeObj.get(i).interactionDetectionArea.x, mp.activeObj.get(i).worldY + mp.activeObj.get(i).interactionDetectionArea.y,
                            mp.activeObj.get(i).interactionDetectionArea.width, mp.activeObj.get(i).interactionDetectionArea.height);
                    Rectangle tmp = new Rectangle(newSolidAreaX1, newSolidAreaY1, entity.solidArea1.width, entity.solidArea1.height);

                    if (tmp.intersects(tmp1)) {
                        mp.activeObj.get(i).isInteracting = true;
                        index = i;
                        break;
                    }
                }
            }
        }
        return index;
    }

    public int checkInteractWithNpc(Entity entity , boolean isPlayer){
        int index = -1;
        if(isPlayer) {
            for (int i = 0; i < mp.npc.size(); i++) {
                if (mp.npc.get(i) != null) {

                    int newSolidAreaX1 = entity.newWorldX + entity.solidArea1.x;
                    int newSolidAreaY1 = entity.newWorldY + entity.solidArea1.y;
                    Rectangle tmp1 = new Rectangle(mp.npc.get(i).worldX + mp.npc.get(i).interactionDetectionArea.x, mp.npc.get(i).worldY + mp.npc.get(i).interactionDetectionArea.y,
                            mp.npc.get(i).interactionDetectionArea.width, mp.npc.get(i).interactionDetectionArea.height);
                    Rectangle tmp = new Rectangle(newSolidAreaX1, newSolidAreaY1, entity.solidArea1.width, entity.solidArea1.height);

                    if (tmp.intersects(tmp1)) {
                        mp.npc.get(i).isInteracting = true;
                        index = i;
                        break;
                    }
                }
            }
        }
        return index;
    };

    public void checkCollisionWithNpc(Entity entity , boolean isPlayer)
    {
        if(isPlayer) {
            for (int i = 0; i < mp.npc.size(); i++) {
                if (mp.npc.get(i) != null) {

                    int newSolidAreaX1 = entity.newWorldX + entity.solidArea1.x;
                    int newSolidAreaY1 = entity.newWorldY + entity.solidArea1.y;
                    Rectangle tmp1 = new Rectangle(mp.npc.get(i).worldX + mp.npc.get(i).solidArea1.x, mp.npc.get(i).worldY + mp.npc.get(i).solidArea1.y,
                            mp.npc.get(i).solidArea1.width, mp.npc.get(i).solidArea1.height);
                    Rectangle tmp = new Rectangle(newSolidAreaX1, newSolidAreaY1, entity.solidArea1.width, entity.solidArea1.height);

                    if (tmp.intersects(tmp1)) {
                        entity.collisionOn = true;
                        mp.npc.get(i).collisionOn = true;
                    }

                    if (mp.npc.get(i).solidArea2 != null) {
                        Rectangle tmp2 = new Rectangle(mp.npc.get(i).worldX + mp.npc.get(i).solidArea2.x, mp.npc.get(i).worldY + mp.npc.get(i).solidArea2.y,
                                mp.npc.get(i).solidArea2.width, mp.npc.get(i).solidArea2.height);
                        if (tmp.intersects(tmp2)) {
                            entity.collisionOn = true;
                            mp.npc.get(i).collisionOn = true;
                        }
                    }
                }
            }
        }
    }


}
