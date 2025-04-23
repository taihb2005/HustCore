package entity.projectile;

import entity.Direction;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;
import java.util.HashMap;

public class Proj_BasicProjectile extends Projectile{
    private static final Sprite sprite = new Sprite(AssetPool.getImage("player_basic_projectile.png"));
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

    public Proj_BasicProjectile(GameMap mp) {
        super(mp);
        name = "Basic Projectile";
        width = 64;
        height = 64;
        maxHP = 30;
        speed = 10;
        baseDamage = 10;
        manaCost = 10;
        direction = "right";
        currentDirection = setDirection(direction);
        lastDirection = setDirection(direction);
        hitbox = new Rectangle(24 , 44 , 16 , 2);
        solidArea1 = new Rectangle(24 , 44 , 16 , 2);
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        super.setDefaultSolidArea();

        currentAnimation = animations.get(currentDirection).clone();

    }

    public void update(){
        super.update();
        setState();
    }

    public void setHitbox(){
        switch(direction){
            case "up"   : hitbox = new Rectangle(23 , 15, 2 , 16) ; break;
            case "down" : hitbox = new Rectangle(43 , 27,2 , 16) ; break;
            default     : hitbox = new Rectangle(24 , 44, 16 , 2); break;
        }
    }
}
