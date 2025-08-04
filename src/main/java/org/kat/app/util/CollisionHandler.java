package org.kat.app.util;

import org.kat.app.entity.Entity;
import org.kat.app.entity.mob.Monster;
import org.kat.app.level.event.EventRectangle;
import org.kat.app.map.GameMap;

import java.awt.*;

public class CollisionHandler {

    GameMap mp;
    private final Rectangle tmpRect1 = new Rectangle();
    private final Rectangle tmpRect2 = new Rectangle();
    private final Rectangle tmpRect3 = new Rectangle();

    public CollisionHandler(GameMap mp) {
        this.mp = mp;
    }

    public int checkInteractWithActiveObject(Entity entity, boolean isPlayer) {
        int index = -1;
        if(mp == null) return index;
        for (int i = 0; i < mp.activeObj.length; i++) {
            if (mp.activeObj[i] != null) {
                float newX = entity.newPosition.x + entity.solidArea1.x;
                float newY = entity.newPosition.y + entity.solidArea1.y;

                tmpRect1.setBounds((int)newX, (int)newY,
                        entity.solidArea1.width, entity.solidArea1.height);
                tmpRect2.setBounds(
                        (int)(mp.activeObj[i].position.x + mp.activeObj[i].interactionDetectionArea.x),
                        (int)(mp.activeObj[i].position.y + mp.activeObj[i].interactionDetectionArea.y),
                        mp.activeObj[i].interactionDetectionArea.width,
                        mp.activeObj[i].interactionDetectionArea.height
                );
                if (tmpRect1.intersects(tmpRect2)) {
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
        if(mp == null) return index;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null && list[i].interactionDetectionArea != null) {
                float newX = entity.newPosition.x + entity.solidArea1.x;
                float newY = entity.newPosition.y + entity.solidArea1.y;

                tmpRect1.setBounds(
                        (int)newX, (int)newY,
                        entity.solidArea1.width, entity.solidArea1.height
                );
                tmpRect2.setBounds(
                        (int)(list[i].position.x + list[i].interactionDetectionArea.x),
                        (int)(list[i].position.y + list[i].interactionDetectionArea.y),
                        list[i].interactionDetectionArea.width,
                        list[i].interactionDetectionArea.height
                );

                if (tmpRect1.intersects(tmpRect2)) {
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
        if (mp == null) return index;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null) {
                tmpRect1.setBounds(
                        (int)(entity.position.x + entity.hitbox.x),
                        (int)(entity.position.y + entity.hitbox.y),
                        entity.hitbox.width, entity.hitbox.height
                );
                tmpRect2.setBounds(
                        (int)(list[i].position.x + list[i].hitbox.x),
                        (int)(list[i].position.y + list[i].hitbox.y),
                        list[i].hitbox.width, list[i].hitbox.height
                );
                if (tmpRect1.intersects(tmpRect2)) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public int checkCollisionWithEntity(Entity entity, Entity[] list) {
        int index = -1;
        if (mp == null) return index;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null) {
                int newX = (int)entity.newPosition.x + entity.solidArea1.x;
                int newY = (int)entity.newPosition.y + entity.solidArea1.y;

                tmpRect1.setBounds(
                        newX, newY,
                        entity.solidArea1.width, entity.solidArea1.height
                );
                tmpRect2.setBounds(
                        (int)(list[i].position.x + list[i].solidArea1.x),
                        (int)(list[i].position.y + list[i].solidArea1.y),
                        list[i].solidArea1.width, list[i].solidArea1.height
                );

                if (tmpRect1.intersects(tmpRect2) && list[i] != entity) {
                    entity.collisionOn = true;
                    index = i;
                    if (list[i].solidArea2 == null) break;
                }

                if (list[i].solidArea2 != null && list[i] != entity) {
                    tmpRect3.setBounds(
                            (int)(list[i].position.x + list[i].solidArea2.x),
                            (int)(list[i].position.y + list[i].solidArea2.y),
                            list[i].solidArea2.width, list[i].solidArea2.height
                    );
                    if (tmpRect1.intersects(tmpRect3)) {
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
        if (mp == null) return false;
        tmpRect1.setBounds(
                (int)(entity.position.x + entity.hitbox.x),
                (int)(entity.position.y + entity.hitbox.y),
                entity.hitbox.width, entity.hitbox.height
        );
        tmpRect2.setBounds(
                (int)(mp.player.position.x + mp.player.solidArea1.x),
                (int)(mp.player.position.y + mp.player.solidArea1.y),
                mp.player.solidArea1.width, mp.player.solidArea1.height
        );
        return tmpRect1.intersects(tmpRect2);
    }

    public boolean checkPlayerForDamage(Entity entity) {
        if (mp == null) return false;
        tmpRect1.setBounds(
                (int)(entity.position.x + entity.hitbox.x),
                (int)(entity.position.y + entity.hitbox.y),
                entity.hitbox.width, entity.hitbox.height
        );
        tmpRect2.setBounds(
                (int)(mp.player.position.x + mp.player.hitbox.x),
                (int)(mp.player.position.y + mp.player.hitbox.y),
                mp.player.hitbox.width, mp.player.hitbox.height
        );
        return tmpRect1.intersects(tmpRect2);
    }

    public boolean checkInteractPlayer(Entity entity) {
        if (mp == null) return false;
        tmpRect1.setBounds(
                (int)(entity.position.x + entity.interactionDetectionArea.x),
                (int)(entity.position.y + entity.interactionDetectionArea.y),
                entity.interactionDetectionArea.width, entity.interactionDetectionArea.height
        );
        tmpRect2.setBounds(
                (int)(mp.player.position.x + mp.player.hitbox.x),
                (int)(mp.player.position.y + mp.player.hitbox.y),
                mp.player.hitbox.width, mp.player.hitbox.height
        );
        return tmpRect1.intersects(tmpRect2);
    }

    public void checkCollisionPlayer(Entity entity) {
        if (mp == null) return;
        tmpRect1.setBounds(
                (int)(entity.newPosition.x + entity.solidArea1.x),
                (int)(entity.newPosition.y + entity.solidArea1.y),
                entity.solidArea1.width, entity.solidArea1.height
        );
        tmpRect2.setBounds(
                (int)(mp.player.position.x + mp.player.solidArea1.x),
                (int)(mp.player.position.y + mp.player.solidArea1.y),
                mp.player.solidArea1.width, mp.player.solidArea1.height
        );

        if (tmpRect1.intersects(tmpRect2)) {
            entity.collisionOn = true;
        }

        if (entity.solidArea2 != null) {
            tmpRect3.setBounds(
                    (int)(entity.position.x + entity.solidArea2.x),
                    (int)(entity.position.y + entity.solidArea2.y),
                    entity.solidArea2.width, entity.solidArea2.height
            );
            if (tmpRect3.intersects(tmpRect2)) {
                entity.collisionOn = true;
            }
        }
    }

    public void dispose(){
        mp = null;
    }
}
