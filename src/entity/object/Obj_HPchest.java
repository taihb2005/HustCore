package entity.object;

import entity.Entity;
import graphics.Animation;
import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_HPchest extends Entity {
    final private BufferedImage[] obj_hpChest;
    final private Animation obj_animator_hpChest;
    private int currentFrames = 0;

    public Obj_HPchest()
    {
        super();
        super.width = 32;
        super.height = 34;

        obj_animator_hpChest = new Animation();
        obj_hpChest = new Sprite("/entity/object/hpChest.png", width , height).getSpriteArrayRow(0);
        obj_animator_hpChest.setAnimationState(obj_hpChest , 9);

        setDefault();
    }

    private void setDefault()
    {
        solidArea1 = new Rectangle(0 , 9 , 32 , 25);
        super.setDefaultSolidArea();
    }

    @Override
    public void update() {
        obj_animator_hpChest.update();
        currentFrames = obj_animator_hpChest.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(obj_hpChest[currentFrames] , worldX - camera.getX(), worldY - camera.getY()
                , width , height , null);
    }

}
