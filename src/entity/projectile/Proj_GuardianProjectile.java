package entity.projectile;

import graphics.Sprite;
import map.GameMap;

import java.awt.*;

public class Proj_GuardianProjectile extends Projectile{
    public Proj_GuardianProjectile(GameMap mp) {
        super(mp);
        name = "Hust Guardian Projectile";
        width = 64 ;
        height = 64;
        maxHP = 80;
        speed = 7;
        base_damage = 30;
        direction = "right";
        hitbox = new Rectangle(24 , 32 , 16 , 2);
        solidArea1 = new Rectangle(0 , 0 , 0 , 0);
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        super.setDefaultSolidArea();
        getImage();
        projectile_animator.setAnimationState(projectile_sprite[CURRENT_DIRECTION] , 10);
    }

    private void getImage(){
        projectile_sprite = new Sprite("/entity/projectile/guardian_projectile.png" , width  , height).getSpriteArray();
    }

    public void setHitbox(){
        if(direction.equals("up") || direction.equals("down")){
            hitbox = new Rectangle(30 , 24 , 2 , 16);
        } else{
            hitbox = new Rectangle(24 , 32 , 16 , 2);
        }
    }
}
