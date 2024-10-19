package entity;

import graphics.Sprite;
import graphics.Animation;

import java.awt.*;

public abstract class Entity {

    public float worldX, worldY;
    protected int speed;
    protected Sprite entity_sprite;

    public Entity(){};

    public Entity(Sprite entity_sprite , float x , float y , int speed)
    {
        this.worldX = x;
        this.worldY = y;
        this.speed = speed;
        this.entity_sprite = entity_sprite;

    }


    public abstract void update();
    public abstract void render(Graphics2D g2);

}
