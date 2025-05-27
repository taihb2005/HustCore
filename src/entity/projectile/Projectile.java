package entity.projectile;

import ai.PathFinder;
import entity.Direction;
import entity.Entity;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import map.GameMap;
import util.Vector2D;

import java.awt.Graphics2D;
import java.util.HashMap;

import static main.GamePanel.camera;

public class Projectile extends Entity {

    GameMap mp;
    Entity user;

    protected static int RIGHT = 0;
    protected static int LEFT = 1;
    protected static int UP = 2;
    protected static int DOWN = 3;

    protected Direction currentDirection;
    protected Direction lastDirection;

    public int manaCost;
    public boolean active;

    public int baseDamage;
    public int slowDuration;

    public Projectile(GameMap mp)
    {
        this.mp = mp;
        this.direction = "right";

        this.pFinder = new PathFinder(mp);
    }

    public void set(Vector2D position, String direction, boolean active, Entity user)
    {
        this.position = position.copy();
        this.direction = direction;
        currentDirection = setDirection(direction);
        lastDirection = setDirection(direction);
        this.active = active;
        this.user = user;
        this.currentHP = this.maxHP;
    }

    public void set(float x, float y, String direction, boolean active, Entity user)
    {
        this.position = new Vector2D(x, y);
        this.direction = direction;
        currentDirection = setDirection(direction);
        lastDirection = setDirection(direction);
        this.active = active;
        this.user = user;
        this.currentHP = this.maxHP;
    }

    Direction setDirection(String direction){
        return switch (direction) {
            case "right" -> Direction.RIGHT;
            case "left" -> Direction.LEFT;
            case "up"   -> Direction.UP;
            case "down" -> Direction.DOWN;
            default -> null;
        };
    }

    public void setSolidArea(){
        solidArea1 = hitbox;
    }

    public boolean checkOppositeDirection(Entity e2){
        boolean check1 = direction.equals("right") & e2.direction.equals("left");
        boolean check2 = direction.equals("left") & e2.direction.equals("right");
        boolean check3 = direction.equals("up") & e2.direction.equals("down");
        boolean check4 = direction.equals("down") & e2.direction.equals("up");
        return check1 | check2 | check3 | check4;
    }

    public void update() {
        if (user == mp.player) {
            int index = mp.cChecker.checkEntityForDamage(this, mp.enemy);
            mp.player.damageEnemy(index);
        } else {
            boolean contactPlayer = mp.cChecker.checkPlayerForDamage(this);
            if (!mp.player.isInvincible && contactPlayer) {
                active = false;
                mp.player.receiveDamage(this, user);
                user.projectileCauseEffect();
                mp.player.isInvincible = true;
            }
        }

        switch (direction) {
            case "right": position.x += speed; break;
            case "left":  position.x -= speed; break;
            case "up":    position.y -= speed; break;
            case "down":  position.y += speed; break;
        }

        newPosition.x = position.x;
        newPosition.y = position.y;

        int i1 = mp.cChecker.checkCollisionWithEntity(this, mp.inactiveObj);
        if (i1 != -1 && "Wall".equals(mp.inactiveObj[i1].name)) {
            active = false;
        }

        int i2 = mp.cChecker.checkCollisionWithEntity(this, mp.activeObj);
        if (i2 != -1 && "Door".equals(mp.activeObj[i2].name)) {
            active = false;
        }

        currentHP--;
        if (currentHP <= 0) active = false;

        currentAnimation.update();
    }

    public void render(Graphics2D g2) {
        if (active) {
//            System.out.println((projectile_sprite.length-1)+" "+(projectile_sprite[CURRENT_DIRECTION].length-1));
//            System.out.println(CURRENT_DIRECTION+" "+CURRENT_FRAME);
            super.render(g2);
        }
    }

    public void setProjectileSpeed(int speed){
        this.speed = speed;
    }

    public void dispose(){

    }
}
