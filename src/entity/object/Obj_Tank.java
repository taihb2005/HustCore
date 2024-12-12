package entity.object;

import entity.Entity;
import graphics.Animation;
import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_Tank extends Entity {
    final private BufferedImage[] obj_filledTank;
    final private Animation obj_animator_filledTank;
    private int currentFrames = 0;
    public int type;


    public Obj_Tank(int type)
    {
        super();
        name = "Filled Tank";
        super.width = 64;
        super.height = 128;
        this.type = type;

        obj_animator_filledTank = new Animation();
        obj_filledTank = new Sprite("/entity/object/filledtank_id" + type + ".png", width , height).getSpriteArrayRow(0);
        obj_animator_filledTank.setAnimationState(obj_filledTank , 9);

        setDefault();
    }
    public Obj_Tank(int type , int x , int y)
    {
        super(x , y);
        name = "Filled Tank";
        super.width = 64;
        super.height = 128;
        this.type = type;

        obj_animator_filledTank = new Animation();
        obj_filledTank = new Sprite("/entity/object/filledtank_id" + type + ".png", width , height).getSpriteArrayRow(0);
        obj_animator_filledTank.setAnimationState(obj_filledTank , 9);

        setDefault();
    }

    public Obj_Tank(String state , int type , int x , int y) throws Exception
    {
        super(x , y);
        name = "Tank";
        super.width = 64;
        super.height = 128;
        this.type = type;

        if(state.equals("empty") && type != 1){
            throw new Exception("Cái Tank đã empty rồi thì để type = 1 nhé anh bạn!");
        }

        obj_animator_filledTank = new Animation();
        obj_filledTank = new Sprite("/entity/object/tank_" + state + "_id"+ type +".png", width , height).getSpriteArrayRow(0);
        obj_animator_filledTank.setAnimationState(obj_filledTank , 9);

        setDefault();
    }

    private void setDefault()
    {
        solidArea1 = new Rectangle(12 , 58 , 42 , 36);
        solidArea2 = new Rectangle(17 , 95 , 32 , 12 );
        super.setDefaultSolidArea();
    }

    @Override
    public void update() throws NullPointerException{
        obj_animator_filledTank.update();
        currentFrames = obj_animator_filledTank.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) throws ArrayIndexOutOfBoundsException , NullPointerException {
        g2.drawImage(obj_filledTank[currentFrames] , worldX - camera.getX(), worldY - camera.getY()
                , width , height , null);
    }

}
