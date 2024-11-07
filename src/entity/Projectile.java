package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class Projectile extends Entity{

    public int x,y;
    public int speed;
    public int direction;
    public boolean active;
    private BufferedImage Sprite;


    public Projectile(int x, int y, int speed, int direction, BufferedImage Sprite) {
        super(x, y, speed);
        this.direction = direction;
        this.Sprite = Sprite;
        this.active = true;
        shootingArea = new Rectangle(32, 32, 64, 128);
    }


    public void update() {
        if (direction == 0) {
            x -= speed;
        } else{
            x += speed;
        }
        if (x < 0 || x > shootingArea.width ){
            active = false;
        }
        System.out.println("Bullet Position: x = " + x + ", y = " + y);
    }
    public void render(Graphics2D g) {
        if (active) {
            g.drawImage(Sprite, x, y , null);
        }
    }
}
