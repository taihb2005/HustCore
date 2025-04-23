package util;

import entity.Entity;
import entity.mob.Monster;
import level.event.EventRectangle;
import map.GameMap;

import java.awt.*;

public class CollisionHandler{

    GameMap mp;
    public CollisionHandler(GameMap mp) {
        this.mp = mp;
    }

    public int checkInteractWithActiveObject(Entity entity , boolean isPlayer) throws NullPointerException , ArrayIndexOutOfBoundsException
    {
        int index = -1;
        for (int i = 0; i < mp.activeObj.length; i++) {
            if (mp.activeObj[i] != null) {

                int newSolidAreaX1 = entity.newWorldX + entity.solidArea1.x;
                int newSolidAreaY1 = entity.newWorldY + entity.solidArea1.y;
                Rectangle tmp1 = new Rectangle(mp.activeObj[i].worldX + mp.activeObj[i].interactionDetectionArea.x, mp.activeObj[i].worldY + mp.activeObj[i].interactionDetectionArea.y,
                        mp.activeObj[i].interactionDetectionArea.width, mp.activeObj[i].interactionDetectionArea.height);
                Rectangle tmp = new Rectangle(newSolidAreaX1, newSolidAreaY1, entity.solidArea1.width, entity.solidArea1.height);

                if (tmp.intersects(tmp1)) {
                    if(isPlayer) {
                        mp.activeObj[i].isInteracting = true;
                        index = i;
                    }
                    break;
                }
            }
        }
        return index;
    }

    public int checkInteractEntity(Entity entity , boolean isPlayer , Entity [] list) throws NullPointerException , ArrayIndexOutOfBoundsException{
        int index = -1;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null && list[i].interactionDetectionArea != null) {

                int newSolidAreaX1 = entity.newWorldX + entity.solidArea1.x;
                int newSolidAreaY1 = entity.newWorldY + entity.solidArea1.y;
                Rectangle tmp1 = new Rectangle(list[i].worldX + list[i].interactionDetectionArea.x, list[i].worldY + list[i].interactionDetectionArea.y,
                        list[i].interactionDetectionArea.width, list[i].interactionDetectionArea.height);
                Rectangle tmp = new Rectangle(newSolidAreaX1, newSolidAreaY1, entity.solidArea1.width, entity.solidArea1.height);

                if (tmp.intersects(tmp1)) {
                    if(isPlayer) {
                        index = i;
                    }
                    break;
                }
            }
        }
        return index;
    }

    public int checkEntityForDamage(Entity entity , Monster[] list) throws NullPointerException , ArrayIndexOutOfBoundsException{
        int index = -1;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null) {
                int newSolidAreaX1 = entity.worldX + entity.hitbox.x;
                int newSolidAreaY1 = entity.worldY + entity.hitbox.y;
                Rectangle tmp1 = new Rectangle(list[i].worldX + list[i].hitbox.x , list[i].worldY + list[i].hitbox.y,
                        list[i].hitbox.width , list[i].hitbox.height);
                Rectangle tmp = new Rectangle(newSolidAreaX1 , newSolidAreaY1, entity.hitbox.width , entity.hitbox.height);

                if(tmp.intersects(tmp1)) {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }


    public int checkCollisionWithEntity(Entity entity , Entity [] list) throws NullPointerException , ArrayIndexOutOfBoundsException
    {
        int index = -1;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null) {

                int newSolidAreaX1 = entity.newWorldX + entity.solidArea1.x;
                int newSolidAreaY1 = entity.newWorldY + entity.solidArea1.y;
                Rectangle tmp1 = new Rectangle(list[i].worldX + list[i].solidArea1.x , list[i].worldY + list[i].solidArea1.y,
                        list[i].solidArea1.width , list[i].solidArea1.height);
                Rectangle tmp = new Rectangle(newSolidAreaX1 , newSolidAreaY1, entity.solidArea1.width , entity.solidArea1.height);

                if(tmp.intersects(tmp1) && list[i] != entity) {
                    entity.collisionOn = true;
                    index = i;
                    if(list[i].solidArea2 == null) break;
                }

                if(list[i].solidArea2 != null && list[i] != entity)
                {
                    Rectangle tmp2 = new Rectangle(list[i].worldX + list[i].solidArea2.x , list[i].worldY + list[i].solidArea2.y,
                            list[i].solidArea2.width , list[i].solidArea2.height);
                    if(tmp.intersects(tmp2)) {
                        entity.collisionOn = true;
                        index = i;
                        break;
                    }
                }
            }
        }
        return index;
    }

    public boolean checkPlayer(Entity entity) throws NullPointerException , ArrayIndexOutOfBoundsException{
        int newHitBoxX = entity.hitbox.x + entity.worldX;
        int newHitBoxY = entity.hitbox.y + entity.worldY;
        Rectangle tmp1 = new Rectangle(newHitBoxX , newHitBoxY , entity.hitbox.width , entity.hitbox.height);
        Rectangle tmp2 = new Rectangle(mp.player.solidArea1.x + mp.player.worldX , mp.player.solidArea1.y + mp.player.worldY , mp.player.solidArea1.width , mp.player.solidArea1.height);
        return tmp2.intersects(tmp1);
    }

    public boolean checkPlayerForDamage(Entity entity) throws NullPointerException ,ArrayIndexOutOfBoundsException{
        int newHitBoxX = entity.hitbox.x + entity.worldX;
        int newHitBoxY = entity.hitbox.y + entity.worldY;
        Rectangle tmp1 = new Rectangle(newHitBoxX , newHitBoxY , entity.hitbox.width , entity.hitbox.height);
        Rectangle tmp2 = new Rectangle(mp.player.hitbox.x + mp.player.worldX , mp.player.hitbox.y + mp.player.worldY , mp.player.hitbox.width , mp.player.hitbox.height);
        return tmp2.intersects(tmp1);
    }

    public boolean checkInteractPlayer(Entity entity) throws NullPointerException , ArrayIndexOutOfBoundsException{
        int newHitBoxX = entity.interactionDetectionArea.x + entity.worldX;
        int newHitBoxY = entity.interactionDetectionArea.y + entity.worldY;
        Rectangle tmp1 = new Rectangle(newHitBoxX , newHitBoxY , entity.interactionDetectionArea.width , entity.interactionDetectionArea.height);
        Rectangle tmp2 = new Rectangle(mp.player.hitbox.x + mp.player.worldX , mp.player.hitbox.y + mp.player.worldY , mp.player.hitbox.width , mp.player.hitbox.height);
        return tmp2.intersects(tmp1);
    }

    public void checkCollisionPlayer(Entity entity) throws NullPointerException , ArrayIndexOutOfBoundsException{
        int newSolidAreaX = entity.solidArea1.x + entity.newWorldX;
        int newSolidAreaY = entity.solidArea1.y + entity.newWorldY;
        Rectangle tmp1 = new Rectangle(newSolidAreaX , newSolidAreaY, entity.solidArea1.width , entity.solidArea1.height);
        Rectangle tmp2 = new Rectangle(mp.player.solidArea1.x + mp.player.worldX , mp.player.solidArea1.y + mp.player.worldY , mp.player.solidArea1.width , mp.player.solidArea1.height);
        if(tmp2.intersects(tmp1)){
            entity.collisionOn = true;
        }

        if(entity.solidArea2 != null){
            Rectangle tmp3 = new Rectangle(entity.worldX + entity.solidArea2.x , entity.worldY + entity.solidArea2.y,
                    entity.solidArea2.width , entity.solidArea2.height);
            if(tmp3.intersects(tmp2)) {
                entity.collisionOn = true;
            }
        }
    }

    public void checkEvent(EventRectangle eventRect){
        int newSolidAreaX1 = mp.player.worldX + mp.player.hitbox.x;
        int newSolidAreaY1 = mp.player.worldY + mp.player.hitbox.y;

        Rectangle tmp1 = new Rectangle(newSolidAreaX1 , newSolidAreaY1 , mp.player.solidArea1.width , mp.player.solidArea1.height);

        if(tmp1.intersects(eventRect)){

        }
    }

}
