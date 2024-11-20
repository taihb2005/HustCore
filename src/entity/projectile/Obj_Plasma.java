package entity.projectile;

import entity.effect.EffectType;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;

public class Obj_Plasma extends Projectile{
    public Obj_Plasma(GameMap mp) {
        super(mp);
        name = "Plasma";
        width = 64;
        height = 64;
        maxHP = 40;
        speed = 7;
        base_damage = 30;
        effectType = EffectType.SLOW;
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
        }
    }

}
