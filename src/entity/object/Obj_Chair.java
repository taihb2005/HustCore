package entity.object;

import entity.Entity;
import graphics.AssetPool;
import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_Chair extends Entity {
    private final BufferedImage obj_chair;
    public int type;

    public Obj_Chair(String direction , int type , int x , int y)
    {
        super(x , y);
        name = "Chair";
        super.width = 64;
        super.height = 64;

        obj_chair = AssetPool.getImage("chair_" + direction + "id_" + type + ".png");

        setDefault();
    }

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
    public void render(Graphics2D g2) throws NullPointerException , ArrayIndexOutOfBoundsException{
        g2.drawImage(obj_chair , worldX - camera.getX() , worldY - camera.getY() , width , height ,  null);
    }
}
