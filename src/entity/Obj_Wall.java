package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import entity.Entity;
import graphics.Sprite;
import util.Camera;

public class Obj_Wall extends Entity{

    private BufferedImage objectImage;

    public Obj_Wall(BufferedImage objectImage , Rectangle solidArea)
    {
        this.objectImage = objectImage;
        this.solidArea = solidArea;
        solidAreaDefaultX = solidArea.x ;
        solidAreaDefaultY = solidArea.y ;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2) {

    }

    @Override
    public void render(Graphics2D g2, Camera camera) {
        g2.drawImage(objectImage , worldX - camera.getX(), worldY - camera.getY(), 64 , 64  , null );
//        g2.setColor(Color.RED);
//        g2.fillRect(worldX + solidArea.x - camera.getX(), worldY + solidArea.y  - camera.getY(), solidArea.width  , solidArea.height );
    }

}