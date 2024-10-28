package entity.object;

import entity.Entity;
import graphics.Animation;
import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_FilledTank extends Entity {
    final private BufferedImage[] obj_filledTank;
    final private Animation obj_animator_filledTank;
    private int currentFrames = 0;

    public static String name = "filled_tank";

    public Obj_FilledTank()
    {
        super();
        super.width = 64;
        super.height = 96;
        obj_animator_filledTank = new Animation();
        obj_filledTank = new Sprite("/entity/object/filledtank_id1.png" , width , height).getSpriteArrayRow(0);
        obj_animator_filledTank.setAnimationState(obj_filledTank , 7);

        solidArea1 = new Rectangle(12 , 48 , 42 , 34);
        solidAreaDefaultX1 = 12;
        solidAreaDefaultY1 = 48;
        solidArea2 = new Rectangle(20 , 83 , 26 , 6 );
        solidAreaDefaultX2 = 20;
        solidAreaDefaultY2 = 83;
    }

    private void setDefault()
    {

    }

    @Override
    public void update() {
        obj_animator_filledTank.update();
        currentFrames = obj_animator_filledTank.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(obj_filledTank[currentFrames] , worldX - camera.getX(), worldY - camera.getY()
                , width , height , null);
    }

}
