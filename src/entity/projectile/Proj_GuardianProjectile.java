package entity.projectile;

import entity.Direction;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;
import java.util.HashMap;

public class Proj_GuardianProjectile extends Projectile{
    private static final Sprite sprite = new Sprite(AssetPool.getImage("guardian_projectile.png"));
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

    public Proj_GuardianProjectile(GameMap mp) {
        super(mp);
        name = "Hust Guardian Projectile";
        width = 64 ;
        height = 64;
        maxHP = 80;
        speed = 7;
        baseDamage = 30;
        direction = "right";
        currentDirection = setDirection(direction);
        lastDirection = setDirection(direction);
        hitbox = new Rectangle(24 , 32 , 16 , 10);
        solidArea1 = new Rectangle(0 , 0 , 0 , 0);
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        super.setDefaultSolidArea();

        currentAnimation = animations.get(currentDirection).clone();
    }


    public void setHitbox(){
        if(direction.equals("up") || direction.equals("down")){
            hitbox = new Rectangle(25 , 17 , 12 , 24);
        } else{
            hitbox = new Rectangle(21 , 26 , 24 , 12);
        }
    }

    public void update(){
        super.update();
        setState();
    }
}
