package entity;

import graphics.Sprite;
import graphics.Animation;

import java.awt.*;

public abstract class Entity {

    protected float posX , posY;
    protected int speed;
    protected Sprite entity_sprite;

    final protected Animation animator;


    public Entity(Sprite entity_sprite , float x , float y , int speed)
    {
        this.posX = x;
        this.posY = y;
        this.speed = speed;
        this.entity_sprite = entity_sprite;

        animator = new Animation();

    }


    public abstract void update();
    public abstract void render(Graphics2D g2);

}
