package org.kat.app.entity.object;

import org.kat.app.entity.Entity;
import org.kat.app.graphics.AssetPool;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.kat.app.main.GamePanel.camera;

public class Obj_Chair extends Entity {
    private final BufferedImage obj_chair;
    public int type;

    public Obj_Chair(String direction , int type , String idName, int x , int y)
    {
        super(x , y);
        name = "Chair";
        this.idName = idName;
        super.width = 64;
        super.height = 64;

        obj_chair = AssetPool.getImage("chair_" + direction + "id_" + type + ".png");

        setDefault();
    }

    public void setDefault()
    {
        solidArea1 = new Rectangle(24 , 36 , 16 , 21);
        super.setDefaultSolidArea();
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2){
        g2.drawImage(obj_chair , (int)position.x - camera.getX() , (int)position.y - camera.getY() , width , height ,  null);
    }
}
