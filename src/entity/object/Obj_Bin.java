package entity.object;

import entity.Entity;
import graphics.AssetPool;
import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_Bin extends Entity {
    private final BufferedImage obj_bin;

    public Obj_Bin(int type, String idName, int x , int y) throws Exception{
        super(x , y);
        name = "Bin";
        this.idName = idName;
        width = 64;
        height = 64;


        if(type > 2 || type <= 0) throw new Exception("Xem lại id của nó mau, id <= 2");

        obj_bin = AssetPool.getImage("bin_id" + type + ".png");
        solidArea1 = new Rectangle(22 , 45 , 18 , 14);
        solidArea2 = new Rectangle(24 , 58 , 14 , 6);
        setDefaultSolidArea();
    }
    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2){
        g2.drawImage(obj_bin , (int)position.x - camera.getX() , (int)position.y - camera.getY() , width , height , null);
    }
}
