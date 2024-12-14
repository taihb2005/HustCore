package entity.object;

import entity.Entity;
import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_Bin extends Entity {
    private final BufferedImage obj_bin;

    public Obj_Bin(int type, int x , int y) throws Exception{
        super(x , y);
        name = "Bin";
        width = 64;
        height = 64;

        if(type > 2 || type <= 0) throw new Exception("Xem lại type của nó mau, type <= 2");

        obj_bin = new Sprite("/entity/object/bin_id" + type + ".png" , width , height).getSpriteSheet();
        solidArea1 = new Rectangle(22 , 45 , 18 , 14);
        solidArea2 = new Rectangle(24 , 58 , 14 , 6);
        setDefaultSolidArea();
    }
    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2) throws NullPointerException , ArrayIndexOutOfBoundsException {
        g2.drawImage(obj_bin , worldX - camera.getX() , worldY - camera.getY() , width , height , null);
    }
}
