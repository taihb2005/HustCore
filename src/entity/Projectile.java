package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile extends Entity{

    public int speed;
    public int direction;
    public boolean active;
    private BufferedImage Sprite;
    private int width, height;
    public Rectangle shootingArea;


    public Projectile(int x, int y, int speed, int direction, BufferedImage Sprite) {
        super(x, y, speed);
        this.direction = direction;
        this.Sprite = Sprite;
        this.active = true;
        shootingArea = new Rectangle(32, 32, 64, 128);
        this.width = 16;
        this.height = 16;
    }


    public void update() {
        if (direction == 0) {
            worldX-= speed;
        } else{
            worldX += speed;
        }
        shootingArea.setLocation(worldX, worldY);
        if (worldX < 0 || worldX > shootingArea.width ){
            active = false;
        }
    }
    public void render(Graphics2D g) {
        if (active) {
            g.drawImage(Sprite, worldX - GamePanel.camera.getX(), worldY - GamePanel.camera.getY(), width,height, null);
        }
    }
}
