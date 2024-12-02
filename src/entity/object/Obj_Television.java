package entity.object;

import entity.Entity;
import graphics.Animation;
import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_Television extends Entity {
    public static int SMALL = 0;
    public static int BIG = 1;
    private final BufferedImage[] obj_television;
    private final Animation obj_animator_television;
    private int currentFrames = 0;
    public String state;
    public int type;

    public Obj_Television(int type) {
        super();
        name = "Television";
        super.width = 128;
        super.height = 48;

        obj_television = new Sprite("/entity/object/television_on_id" + type + ".png" , 128 , 64).getSpriteArrayRow(0);
        obj_animator_television = new Animation();
        obj_animator_television.setAnimationState(obj_television, 20);

        setDefault();
    }

    public Obj_Television(String state , String size , int type , int x , int y) {
        super(x , y);
        name = "Television";
        int multiplier = (size.equals("big")) ? 1 : 0;
        super.width = 64 + multiplier * 64;
        super.height = 64;
        this.state = state;

        obj_television = new Sprite("/entity/object/television_" + state + "_id" + type + ".png" , width , height).getSpriteArrayRow(0);
        obj_animator_television = new Animation();
        obj_animator_television.setAnimationState(obj_television, 20);

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
        g2.drawImage(obj_television[currentFrames] , worldX - camera.getX() , worldY - camera.getY() , width ,height , null);
    }
}
