package entity.projectile;

import graphics.Sprite;
import map.GameMap;

import java.awt.*;

public class Proj_GreenBullet extends Projectile{

    public Proj_GreenBullet(GameMap mp) {
        super(mp);
        name = "Green Bullet";
        width = 64;
        height = 64;
        maxHP = 80;
        speed = 9;
        hitbox = new Rectangle(28 , 29 , 12 , 7);
        solidArea1 = new Rectangle(0 , 0 , 0 , 0);
        direction = "right";

        getImage();
        projectile_animator.setAnimationState(projectile_sprite[CURRENT_DIRECTION] , 10);
    }

    private void getImage()
    {
        projectile_sprite = new Sprite("/entity/projectile/projectile_id1.png" , width , height).getSpriteArray();
    }

    public void setHitbox(){
        if(direction.equals("left") || direction.equals("right")){
            hitbox.setLocation(28 , 29);
            hitbox.setSize(12 , 7);
        } else
        {
            hitbox.setLocation(29 , 28);
            hitbox.setSize(6 , 12);
        }
    }
}
