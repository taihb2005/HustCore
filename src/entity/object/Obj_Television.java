package entity.object;

import entity.Entity;
import graphics.Animation;
import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_Television extends Entity {
    private final BufferedImage[] obj_televisionOn;
    private final Animation obj_animator_television;
    private int currentFrames = 0;
    public int type;

    public Obj_Television(int type) {
        super();
        name = "Television";
        super.width = 128;
        super.height = 48;

        obj_televisionOn = new Sprite("/entity/object/television_on_id" + type + ".png" , 128 , 64).getSpriteArrayRow(0);
        obj_animator_television = new Animation();
        obj_animator_television.setAnimationState(obj_televisionOn , 20);

        setDefault();
    }

    private void setDefault()
    {
        solidArea1 = new Rectangle(0  , 0 , 0 , 0);
    }

    @Override
    public void update() {
        obj_animator_television.update();
        currentFrames = obj_animator_television.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(obj_televisionOn[currentFrames] , worldX - camera.getX() , worldY - camera.getY() , width ,height , null);
    }
}
