package entity.projectile;

import entity.Entity;
import entity.object.Obj_Wall;
import graphics.Animation;
import map.GameMap;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import static main.GamePanel.camera;

public class Projectile extends Entity {

    GameMap mp;
    Entity user;
    protected static int RIGHT = 0;
    protected static int LEFT = 1;
    protected static int UP = 2;
    protected static int DOWN = 3;

    protected BufferedImage [][] projectile_sprite;
    protected Animation projectile_animator = new Animation();
    protected int CURRENT_DIRECTION;
    protected int CURRENT_FRAME;

    public int base_damage;

    public Projectile(GameMap mp)
    {
        this.mp = mp;
        this.direction = "right";
    }

//    public Projectile(int x, int y, int speed, int direction, BufferedImage Sprite) {
//        super(x, y, speed);
//        this.direction = direction;
//        this.Sprite = Sprite;
//        this.active = true;
//        shootingArea = new Rectangle(32, 32, 64, 128);
//    }

    public void set(int worldX, int worldY, String direction, boolean active, Entity user)
    {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        CURRENT_DIRECTION = setDirection(direction);
        this.active = active;
        this.user = user;
        this.currentHP = this.maxHP;
    }

    private int setDirection(String direction){
        return switch (direction) {
            case "right" -> RIGHT;
            case "left" -> LEFT;
            case "up"   -> UP;
            case "down" -> DOWN;
            default -> -1;
        };
    }

    public void update() {
        if(user == mp.player){
            int index = mp.cChecker.checkEntityForDamage(this , mp.enemy);
           //System.out.println(index);
            mp.player.damageEnemy(index);
        } else{

        }
        switch (direction)
        {
            case "right" : worldX += speed; break;
            case "left"  : worldX -= speed ; break;
            case "up"    : worldY -= speed ; break;
            case "down"  : worldY += speed; break;
        }
        newWorldX = worldX;
        newWorldY = worldY;

        int i = mp.cChecker.checkCollisionWithEntity(this , mp.inactiveObj);
        if(i != -1)
        {
            if(mp.inactiveObj[i].name.equals("Wall")) {
                active = false;
            }
        }

        currentHP--;
        if(currentHP <= 0) active = false;

        projectile_animator.update();
        CURRENT_FRAME = projectile_animator.getCurrentFrames();
    }
    public void render(Graphics2D g2) {
        if (active) {
            g2.drawImage(projectile_sprite[CURRENT_DIRECTION][CURRENT_FRAME] , worldX - camera.getX()  , worldY - camera.getY() + 12  ,
                         width , height , null);
        }
    }
}
