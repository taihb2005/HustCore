package entity.projectile;

import graphics.Sprite;
import map.GameMap;

import java.awt.*;

public class Proj_Flame extends Projectile {
    public Proj_Flame(GameMap mp) {
        super(mp);
        name = "Flame";
        width = 64;
        height = 64;
        maxHP = 200;
        speed = 0;
        base_damage = 30;
        slowDuration = 180;
        manaCost = 20;
        direction = "right";
        hitbox = new Rectangle(4 , 29 , 56 , 35);
        solidArea1 = hitbox;
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        super.setDefaultSolidArea();
        getImage();
        projectile_animator.setAnimationState(projectile_sprite[CURRENT_DIRECTION] , 15);
    }

    private void getImage(){
        projectile_sprite = new Sprite("/entity/projectile/flame.png" , width , height).getSpriteArray();
    }

    public void update() {
        super.update();
        if (CURRENT_FRAME == 4) active = false;
    }
}
