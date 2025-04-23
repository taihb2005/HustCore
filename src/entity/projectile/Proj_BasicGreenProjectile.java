package entity.projectile;

import entity.Direction;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;
import java.util.HashMap;

public class Proj_BasicGreenProjectile extends Projectile{
    private static final Sprite sprite = new Sprite(AssetPool.getImage("basic_green_projectile.png"));
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

    public Proj_BasicGreenProjectile(GameMap mp)
    {
        super(mp);
        name = "Basic Green Projectile";
        width = 64;
        height = 64;
        maxHP = 80;
        speed = 10;
        baseDamage = 5;
        direction = "right";
        currentDirection = setDirection(direction);
        lastDirection = setDirection(direction);
        solidArea1 = new Rectangle(28 , 30 , 8 , 8);
        hitbox = new Rectangle(28 , 30, 8 , 8);
        solidArea2 = new Rectangle(0  ,0 , 0 , 0);
        setDefaultSolidArea();

        currentAnimation = animations.get(currentDirection).clone();
    }

    public void update(){
        super.update();
        setState();
    }

}
