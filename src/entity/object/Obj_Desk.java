package entity.object;

import entity.Entity;
import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_Desk extends Entity {
    private final BufferedImage obj_desk;
    public int type;

    public Obj_Desk(int type)
    {
        super();
        name = "Desk";
        super.width = 64;
        super.height = 64;

        this.type = type;
        obj_desk = new Sprite("/entity/object/desk_id" + type + ".png" , width , height).getSprite(0 , 0);

        setDefault();
    }

    public Obj_Desk(int type , int x , int y)
    {
        super(x , y);
        name = "Desk";
        super.width = 64;
        super.height = 64;

        this.type = type;
        obj_desk = new Sprite("/entity/object/desk_id" + type + ".png" , width , height).getSprite(0 , 0);

        setDefault();
    }

    private void setDefault()
    {
        solidArea1 = new Rectangle(8 , 34 , 48 , 26);
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        super.setDefaultSolidArea();

    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(obj_desk , worldX - camera.getX() , worldY - camera.getY() , null);
    }
}
