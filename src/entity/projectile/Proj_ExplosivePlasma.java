package entity.projectile;

import graphics.Sprite;
import map.GameMap;

import java.awt.*;

import static main.GamePanel.camera;

public class Proj_ExplosivePlasma extends Projectile{
    private int diameter = 0;
    private float hue = 0f;
    public Proj_ExplosivePlasma(GameMap mp) {
        super(mp);
        name = "Explosive Plasma";
        width = 64;
        height = 64;
        maxHP = 40;
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
        projectile_sprite = new Sprite("/entity/projectile/explosive_plasma.png" , width , height).getSpriteArray();
    }

    //DOAN NAY CODE NOT DI NHA
    public void damagePlayer() {
        if (Math.pow(mp.player.worldX-worldX,2) + Math.pow(mp.player.worldY-worldY,2) < diameter*diameter) mp.player.currentHP -= 1;
    }

    @Override
    public void update() {
        super.update();
        diameter = (maxHP-currentHP)*(maxHP-currentHP)/10;
        damagePlayer();
    }

    @Override
    public void render(Graphics2D g2) {
        super.render(g2);

        Color dynamicColor = Color.getHSBColor(hue, 1.0f, 1.0f);
        // Set màu sắc cho nét vẽ
        g2.setColor(dynamicColor);
        actionPerformed();
        g2.drawOval((worldX + (int) (width*0.6) - diameter / 2) - camera.getX(), (worldY + (int) (height * 0.7) - diameter / 2) - camera.getY(), diameter, diameter);
    }

    public void actionPerformed() {
        hue += 0.01f;
        if (hue > 1.0f) {
            hue = 0.0f;
        }
    }
}
