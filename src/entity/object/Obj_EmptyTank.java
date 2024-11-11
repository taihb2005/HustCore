package entity.object;

import entity.Entity;
import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_EmptyTank extends Entity {
    private final BufferedImage obj_emptyTank;

    public Obj_EmptyTank(){
        super();
        name = "Empty Tank";
        super.width = 64;
        super.height = 128;

        obj_emptyTank = new Sprite("/entity/object/emptytank.png" , width , height).getSprite(0 , 0);

        setDefault();
    }

    private void setDefault()
    {
        solidArea1 = new Rectangle(12 , 58 , 42 , 36);
        solidArea2 = new Rectangle(17 , 95 , 32 , 12 );
        super.setDefaultSolidArea();
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(obj_emptyTank , worldX - camera.getX() , worldY - camera.getY() , width ,height , null);
    }
}
