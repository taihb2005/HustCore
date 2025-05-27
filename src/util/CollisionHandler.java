package util;

import entity.Entity;
import entity.mob.Monster;
import level.event.EventRectangle;
import map.GameMap;

import java.awt.*;

public class CollisionHandler {

    GameMap mp;

    public CollisionHandler(GameMap mp) {
        this.mp = mp;
    }

    public int checkInteractWithActiveObject(Entity entity, boolean isPlayer) {
        int index = -1;
        for (int i = 0; i < mp.activeObj.length; i++) {
            if (mp.activeObj[i] != null) {
                float newX = entity.newPosition.x + entity.solidArea1.x;
                float newY = entity.newPosition.y + entity.solidArea1.y;

                Rectangle tmp = new Rectangle((int)newX, (int)newY,
                        entity.solidArea1.width, entity.solidArea1.height);
                Rectangle objRect = new Rectangle(
                        (int)(mp.activeObj[i].position.x + mp.activeObj[i].interactionDetectionArea.x),
                        (int)(mp.activeObj[i].position.y + mp.activeObj[i].interactionDetectionArea.y),
                        mp.activeObj[i].interactionDetectionArea.width,
                        mp.activeObj[i].interactionDetectionArea.height);

                if (tmp.intersects(objRect)) {
                    if (isPlayer) {
                        mp.activeObj[i].isInteracting = true;
                        index = i;
                    }
                    break;
                }
            }
        }
        return index;
    }

    public int checkInteractEntity(Entity entity, boolean isPlayer, Entity[] list) {
        int index = -1;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null && list[i].interactionDetectionArea != null) {
                float newX = entity.newPosition.x + entity.solidArea1.x;
                float newY = entity.newPosition.y + entity.solidArea1.y;

                Rectangle tmp = new Rectangle((int)newX, (int)newY,
                        entity.solidArea1.width, entity.solidArea1.height);
                Rectangle target = new Rectangle(
                        (int)(list[i].position.x + list[i].interactionDetectionArea.x),
                        (int)(list[i].position.y + list[i].interactionDetectionArea.y),
                        list[i].interactionDetectionArea.width,
                        list[i].interactionDetectionArea.height);

                if (tmp.intersects(target)) {
                    if (isPlayer) {
                        index = i;
                    }
                    break;
                }
            }
        }
        return index;
    }

    public int checkEntityForDamage(Entity entity, Monster[] list) {
        int index = -1;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null) {
                Rectangle a = new Rectangle(
                        (int)(entity.position.x + entity.hitbox.x),
                        (int)(entity.position.y + entity.hitbox.y),
                        entity.hitbox.width, entity.hitbox.height);
                Rectangle b = new Rectangle(
                        (int)(list[i].position.x + list[i].hitbox.x),
                        (int)(list[i].position.y + list[i].hitbox.y),
                        list[i].hitbox.width, list[i].hitbox.height);

                if (a.intersects(b)) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public int checkCollisionWithEntity(Entity entity, Entity[] list) {
        int index = -1;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null) {
                int newX = (int)entity.newPosition.x + entity.solidArea1.x;
                int newY = (int)entity.newPosition.y + entity.solidArea1.y;

                Rectangle tmp = new Rectangle(newX, newY,
                        entity.solidArea1.width, entity.solidArea1.height);

                Rectangle target1 = new Rectangle(
                        (int)(list[i].position.x + list[i].solidArea1.x),
                        (int)(list[i].position.y + list[i].solidArea1.y),
                        list[i].solidArea1.width, list[i].solidArea1.height);

                if (tmp.intersects(target1) && list[i] != entity) {
                    entity.collisionOn = true;
                    index = i;
                    if (list[i].solidArea2 == null) break;
                }

                if (list[i].solidArea2 != null && list[i] != entity) {
                    Rectangle target2 = new Rectangle(
                            (int)(list[i].position.x + list[i].solidArea2.x),
                            (int)(list[i].position.y + list[i].solidArea2.y),
                            list[i].solidArea2.width, list[i].solidArea2.height);
                    if (tmp.intersects(target2)) {
                        entity.collisionOn = true;
                        index = i;
                        break;
                    }
                }
            }
        }
        return index;
    }

    public boolean checkPlayer(Entity entity) {
        Rectangle a = new Rectangle(
                (int)(entity.position.x + entity.hitbox.x),
                (int)(entity.position.y + entity.hitbox.y),
                entity.hitbox.width, entity.hitbox.height);
        Rectangle b = new Rectangle(
                (int)(mp.player.position.x + mp.player.solidArea1.x),
                (int)(mp.player.position.y + mp.player.solidArea1.y),
                mp.player.solidArea1.width, mp.player.solidArea1.height);
        return a.intersects(b);
    }

    public boolean checkPlayerForDamage(Entity entity) {
        Rectangle a = new Rectangle(
                (int)(entity.position.x + entity.hitbox.x),
                (int)(entity.position.y + entity.hitbox.y),
                entity.hitbox.width, entity.hitbox.height);
        Rectangle b = new Rectangle(
                (int)(mp.player.position.x + mp.player.hitbox.x),
                (int)(mp.player.position.y + mp.player.hitbox.y),
                mp.player.hitbox.width, mp.player.hitbox.height);
        return a.intersects(b);
    }

    public boolean checkInteractPlayer(Entity entity) {
        Rectangle a = new Rectangle(
                (int)(entity.position.x + entity.interactionDetectionArea.x),
                (int)(entity.position.y + entity.interactionDetectionArea.y),
                entity.interactionDetectionArea.width, entity.interactionDetectionArea.height);
        Rectangle b = new Rectangle(
                (int)(mp.player.position.x + mp.player.hitbox.x),
                (int)(mp.player.position.y + mp.player.hitbox.y),
                mp.player.hitbox.width, mp.player.hitbox.height);
        return a.intersects(b);
    }

    public void checkCollisionPlayer(Entity entity) {
        Rectangle tmp1 = new Rectangle(
                (int)(entity.newPosition.x + entity.solidArea1.x),
                (int)(entity.newPosition.y + entity.solidArea1.y),
                entity.solidArea1.width, entity.solidArea1.height);
        Rectangle tmp2 = new Rectangle(
                (int)(mp.player.position.x + mp.player.solidArea1.x),
                (int)(mp.player.position.y + mp.player.solidArea1.y),
                mp.player.solidArea1.width, mp.player.solidArea1.height);

        if (tmp1.intersects(tmp2)) {
            entity.collisionOn = true;
        }

        if (entity.solidArea2 != null) {
            Rectangle tmp3 = new Rectangle(
                    (int)(entity.position.x + entity.solidArea2.x),
                    (int)(entity.position.y + entity.solidArea2.y),
                    entity.solidArea2.width, entity.solidArea2.height);
            if (tmp3.intersects(tmp2)) {
                entity.collisionOn = true;
            }
        }
    }

    public void checkEvent(EventRectangle eventRect) {
        Rectangle tmp1 = new Rectangle(
                (int)(mp.player.position.x + mp.player.hitbox.x),
                (int)(mp.player.position.y + mp.player.hitbox.y),
                mp.player.solidArea1.width, mp.player.solidArea1.height);
        if (tmp1.intersects(eventRect)) {

        }
    }
}
