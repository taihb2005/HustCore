package entity.projectile;

import entity.effect.EffectType;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;

public class Proj_Plasma extends Projectile{
    public Proj_Plasma(GameMap mp) {
        super(mp);
        name = "Plasma";
        width = 64;
        height = 64;
        maxHP = 80;
        speed = 7;
        base_damage = 30;
        slowDuration = 180;
        manaCost = 20;
        direction = "right";
        hitbox = new Rectangle(28 , 30 , 6 , 8);
        solidArea1 = hitbox;
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        super.setDefaultSolidArea();
        getImage();
        projectile_animator.setAnimationState(projectile_sprite[CURRENT_DIRECTION] , 20);
    }

    private void getImage(){
        projectile_sprite = new Sprite("/entity/projectile/plasma.png" , width , height).getSpriteArray();
    }

    public void setHitbox(){
        switch(direction){
            case "right":
                hitbox.setLocation(37 , 39);
                break;
            case "left":
                hitbox.setLocation(20 , 39);
                break;
            case "up":
                hitbox.setLocation(27 , 30);
                break;
            case "down":
                hitbox.setLocation(27 , 45);
        }
    }

}
