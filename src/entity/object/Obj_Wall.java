package entity.object;

import entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_Wall extends Entity {

    private final BufferedImage objectImage;
    public final String name = "wall";

    public Obj_Wall(BufferedImage objectImage , Rectangle[] solidAreaList)
    {
        this.objectImage = objectImage;
        this.solidArea1 = solidAreaList[0];
        solidAreaDefaultX1 = solidArea1.x ;
        solidAreaDefaultY1 = solidArea1.y ;
        if(solidAreaList[1] != null)
        {
            this.solidArea2 = solidAreaList[1];
            this.solidAreaDefaultX2 = solidArea2.x;
            this.solidAreaDefaultY2 = solidArea2.y;
        }
    }


    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(objectImage , worldX - camera.getX(), worldY - camera.getY(), 64 , 64  , null );
    }

}