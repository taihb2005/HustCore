package util;

import entity.Entity;
import map.GameMap;

public class CollisionHandler {

    GameMap mp;
    public CollisionHandler(GameMap mp){this.mp = mp;};

    public int checkObjectCollision(Entity entity , boolean isPlayer)
    {
        int index = -1;

        for(int i = 0 ; i < mp.obj.size() ; i++)
        {
            if(mp.obj.get(i) != null)
            {
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                mp.obj.get(i).solidArea.x = mp.obj.get(i).worldX + mp.obj.get(i).solidArea.x;
                mp.obj.get(i).solidArea.y = mp.obj.get(i).worldY + mp.obj.get(i).solidArea.y;

                switch(entity.direction)
                {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if(entity.solidArea.intersects(mp.obj.get(i).solidArea)) entity.collisionOn = true;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if(entity.solidArea.intersects(mp.obj.get(i).solidArea)) entity.collisionOn = true;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if(entity.solidArea.intersects(mp.obj.get(i).solidArea)) entity.collisionOn = true;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if(entity.solidArea.intersects(mp.obj.get(i).solidArea)) entity.collisionOn = true;
                        break;
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                mp.obj.get(i).solidArea.x = mp.obj.get(i).solidAreaDefaultX;
                mp.obj.get(i).solidArea.y = mp.obj.get(i).solidAreaDefaultY;

            }
        }

        return -1;
    }



}
