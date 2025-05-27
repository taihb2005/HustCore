package entity.projectile;

import entity.Direction;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;
import java.util.HashMap;

import static main.GamePanel.camera;

public class Proj_ExplosivePlasma extends Projectile{
    private static final Sprite sprite = new Sprite(AssetPool.getImage("explosive_plasma.png"));
    private static final HashMap<Direction, Animation> animations = new HashMap<>();

    public static void load(){
        for(Direction direction : Direction.values()){
            int row = switch (direction){
                case RIGHT -> 0;
                case LEFT -> 1;
                case UP -> 2;
                case DOWN -> 3;
            };
            animations.put(direction,
                    new Animation(sprite.getSpriteArrayRow(row), 10, true));
        }
    }

    Direction lastDirection = null;

    private void setState(){
        if(currentDirection != lastDirection){
            lastDirection = currentDirection;
            currentAnimation = animations.get(currentDirection).clone();
        }
    }

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
        baseDamage = 30;
        slowDuration = 180;
        manaCost = 20;
        direction = "right";
        currentDirection = setDirection(direction);
        lastDirection = setDirection(direction);
        hitbox = new Rectangle(0 , 0 , 6 , 8);
        solidArea1 = hitbox;
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        super.setDefaultSolidArea();

        currentAnimation = animations.get(currentDirection).clone();
    }


    public void damagePlayer() {
        timeCount++;

        float dx = mp.player.position.x - position.x;
        float dy = mp.player.position.y - position.y;

        if ((dx * dx + dy * dy) < diameter * diameter - 10 && timeCount >= 100) {
            timeCount = 0;
            mp.player.currentHP -= 1;
        }
    }


    @Override
    public void update() {
        super.update();
        setState();
        diameter = (maxHP-currentHP)*(maxHP-currentHP)/10;
        damagePlayer();
    }

    @Override
    public void render(Graphics2D g2) {
        if (active) {
            super.render(g2);
        }
        cFrame = (cFrame+1)%20;
        Color dynamicColor = Color.getHSBColor(hue, 1.0f, 1.0f);
        // Set màu sắc cho nét vẽ
        g2.setColor(dynamicColor);
        actionPerformed();
        int centerX = (int)position.x + 32;
        int centerY = (int)position.y + 32;
        int drawX = centerX - diameter / 2 - camera.getX();
        int drawY = centerY - diameter / 2 - camera.getY();

        // Vẽ đường tròn với tâm chính xác8
        g2.setStroke(new BasicStroke(thickness));
        thickness += 0.02F;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2.drawOval(drawX, drawY, diameter, diameter);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void actionPerformed() {
        hue += 0.01f;
        if (hue > 1.0f) {
            hue = 0.0f;
        }
    }
}
