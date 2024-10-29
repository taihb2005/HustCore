package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class projecttile extends Entity{

    public int x,y;
    public int speed;
    public int direction;
    public boolean active;
    private BufferedImage Sprite;


    public projecttile(int x, int y, int speed, int direction, BufferedImage Sprite) {
        this.x = x;
        this.y = y;
        this.speed = speed;
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
    }
    public void render(Graphics g) {
        g.drawImage(Sprite, x, y, null);
    }


}
