package entity.object;

import entity.Entity;
import graphics.Animation;
import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_PasswordAuth extends Entity {
    private final BufferedImage[] obj_pwAuth;
    private final Animation obj_animator_pwAuth;
    private int currentFrame = 0;

    public Obj_PasswordAuth(String state , int x , int y){
        super(x , y);
        name = "Password Authentication";
        width = 64;
        height = 64;

        obj_pwAuth = new Sprite("/entity/object/password_authentication_" + state + ".png" , width , height).getSpriteArrayRow(0);
        obj_animator_pwAuth = new Animation();
        obj_animator_pwAuth.setAnimationState(obj_pwAuth , 20);
        solidArea1 = new Rectangle(0 , 0 , 0 , 0);
    }
    @Override
    public void update() throws NullPointerException{
        obj_animator_pwAuth.update();
        currentFrame = obj_animator_pwAuth.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) throws NullPointerException , ArrayIndexOutOfBoundsException{
        g2.drawImage(obj_pwAuth[currentFrame] , worldX - camera.getX() , worldY - camera.getY() , width ,height , null);
    }
}
