package entity.projectile;

import entity.Direction;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;
import java.util.HashMap;

public class Proj_Flame extends Projectile {
    private static final Sprite sprite = new Sprite(AssetPool.getImage("flame.png"));
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

    public Proj_Flame(GameMap mp) {
        super(mp);
        name = "Flame";
        width = 64;
        height = 64;
        maxHP = 200;
        speed = 0;
        baseDamage = 30;
        slowDuration = 180;
        manaCost = 20;
        direction = "right";
        currentDirection = setDirection(direction);
        lastDirection = setDirection(direction);
        hitbox = new Rectangle(4 , 29 , 56 , 35);
        solidArea1 = hitbox;
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        super.setDefaultSolidArea();

        currentAnimation = animations.get(currentDirection).clone();
    }


    public void update() {
        super.update();
        setState();
        if (currentAnimation.isFinished()) active = false;
    }
}
