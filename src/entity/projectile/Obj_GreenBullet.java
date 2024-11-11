package entity.projectile;

import graphics.Sprite;
import map.GameMap;

public class Obj_GreenBullet extends Projectile{

    public Obj_GreenBullet(GameMap mp) {
        super(mp);
        String name = "Green Bullet";
        width = 64;
        height = 64;
        maxHP = 80;
        speed = 9;

        getImage();
        projectile_animator.setAnimationState(projectile_sprite[CURRENT_DIRECTION] , 10);
    }

    private void getImage()
    {
        projectile_sprite = new Sprite("/entity/projectile/projectile_id1.png" , width , height).getSpriteArray();
    }
}
