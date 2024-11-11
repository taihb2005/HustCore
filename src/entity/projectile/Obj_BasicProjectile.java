package entity.projectile;

import graphics.Sprite;
import map.GameMap;

import java.awt.*;

public class Obj_BasicProjectile extends Projectile{
    public Obj_BasicProjectile(GameMap mp) {
        super(mp);
        name = "Basic Projectile";
        width = 64;
        height = 64;
        maxHP = 30;
        speed = 10;
        base_damage = 10;
        manaCost = 10;
        solidArea1 = new Rectangle(24 , 32 + 12, 16 , 2);
        hitbox = new Rectangle(24 , 32 + 12, 16 , 2);
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        super.setDefaultSolidArea();
        getImage();
        projectile_animator.setAnimationState(projectile_sprite[CURRENT_DIRECTION] , 10);
    }

    private void getImage()
    {
        projectile_sprite = new Sprite("/entity/projectile/player_basic_projectile.png").getSpriteArray();
    }
}
