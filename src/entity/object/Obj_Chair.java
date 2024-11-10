package entity.object;

import entity.Entity;
import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_Chair extends Entity {
    private final BufferedImage obj_chair;
    public int type;

    public Obj_Chair(int type)
    {
        super();
        name = "Chair";
        super.width = 64;
        super.height = 64;

        obj_chair = new Sprite("/entity/object/chair_id" + type + ".png" , width , height).getSprite(0 , 0);

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
    public void render(Graphics2D g2) {
        g2.drawImage(obj_chair , worldX - camera.getX() , worldY - camera.getY() , width , height ,  null);
    }
}
