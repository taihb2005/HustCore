package entity.projectile;

import graphics.Sprite;
import map.GameMap;

import java.awt.*;

import static main.GamePanel.camera;

public class Proj_ExplosivePlasma extends Projectile{
    private int diameter = 0;
    private int timeCount = 0;
    private float hue = 0f;
    private int cFrame = 0;
    private float thickness = 1;
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
        hitbox = new Rectangle(0 , 0 , 6 , 8);
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
        timeCount++;
        if (Math.pow(mp.player.worldX-worldX,2) + Math.pow(mp.player.worldY-worldY,2) < diameter*diameter-10 && timeCount >= 100) {
            timeCount = 0;
            mp.player.currentHP -= 1;
        }
    }

    @Override
    public void update() {
        super.update();
        diameter = (maxHP-currentHP)*(maxHP-currentHP)/10;
        damagePlayer();
    }

    @Override
    public void render(Graphics2D g2) {
        if (active) {
            g2.drawImage(projectile_sprite[CURRENT_DIRECTION][cFrame/5] , worldX - camera.getX() , worldY - camera.getY() ,
                    width , height , null);
        }
        cFrame = (cFrame+1)%20;
        Color dynamicColor = Color.getHSBColor(hue, 1.0f, 1.0f);
        // Set màu sắc cho nét vẽ
        g2.setColor(dynamicColor);
        actionPerformed();
        int centerX = worldX + 32;
        int centerY = worldY + 32;
        int drawX = centerX - diameter / 2 - camera.getX();
        int drawY = centerY - diameter / 2 - camera.getY();

        // Vẽ đường tròn với tâm chính xác8
        g2.setStroke(new BasicStroke(thickness));
        thickness += 0.02F;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2.drawOval(drawX, drawY, diameter, diameter);
    }

    public void actionPerformed() {
        hue += 0.01f;
        if (hue > 1.0f) {
            hue = 0.0f;
        }
    }
}
