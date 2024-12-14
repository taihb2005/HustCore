package entity.object;

import entity.Entity;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_Wall extends Entity {
    private BufferedImage objectImage;

    public Obj_Wall(BufferedImage objectImage , Rectangle[] solidAreaList)
    {
        super();
        name = "Wall";
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
    public void render(Graphics2D g2) throws ArrayIndexOutOfBoundsException{
        g2.drawImage(objectImage , worldX - camera.getX(), worldY - camera.getY(), 64 , 64  , null );
    }

    public void dispose(){
        solidArea1 = null;
        solidArea2 = null;
        objectImage.flush();
        objectImage = null;
    }

}