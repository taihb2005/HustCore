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

//                mp.inactiveObj.get(i).solidArea1.x = mp.inactiveObj.get(i).worldX + mp.inactiveObj.get(i).solidArea1.x;
//                mp.inactiveObj.get(i).solidArea1.y = mp.inactiveObj.get(i).worldY + mp.inactiveObj.get(i).solidArea1.y;

                int newSolidAreaX1 = entity.newWorldX + entity.solidArea1.x;
                int newSolidAreaY1 = entity.newWorldY + entity.solidArea1.y;
                Rectangle tmp1 = new Rectangle(mp.inactiveObj.get(i).worldX + mp.inactiveObj.get(i).solidArea1.x , mp.inactiveObj.get(i).worldY + mp.inactiveObj.get(i).solidArea1.y,
                        mp.inactiveObj.get(i).solidArea1.width , mp.inactiveObj.get(i).solidArea1.height);
                Rectangle tmp = new Rectangle(newSolidAreaX1 , newSolidAreaY1, entity.solidArea1.width , entity.solidArea1.height);

                if(tmp.intersects(tmp1)) entity.collisionOn = true;

//                mp.inactiveObj.get(i).solidArea1.x = mp.inactiveObj.get(i).solidAreaDefaultX1;
//                mp.inactiveObj.get(i).solidArea1.y = mp.inactiveObj.get(i).solidAreaDefaultY1;

                if(mp.inactiveObj.get(i).solidArea2 != null)
                {
//                    mp.inactiveObj.get(i).solidArea2.x = mp.inactiveObj.get(i).worldX + mp.inactiveObj.get(i).solidArea2.x;
//                    mp.inactiveObj.get(i).solidArea2.y = mp.inactiveObj.get(i).worldY + mp.inactiveObj.get(i).solidArea2.y;

                    Rectangle tmp2 = new Rectangle(mp.inactiveObj.get(i).worldX + mp.inactiveObj.get(i).solidArea2.x , mp.inactiveObj.get(i).worldY + mp.inactiveObj.get(i).solidArea2.y,
                            mp.inactiveObj.get(i).solidArea2.width , mp.inactiveObj.get(i).solidArea2.height);
                    if(tmp.intersects(tmp2)) entity.collisionOn = true;

//                    mp.inactiveObj.get(i).solidArea2.x = mp.inactiveObj.get(i).solidAreaDefaultX2;
//                    mp.inactiveObj.get(i).solidArea2.y = mp.inactiveObj.get(i).solidAreaDefaultY2;
                }
            }
        }
    }

    public void checkCollisionWithActiveObject(Entity entity , boolean isPlayer){};


}
