package entity.projectile;

import entity.Entity;
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

    public int manaCost;
    public boolean active;


    public int base_damage;
    public int slowDuration;

    public Projectile(GameMap mp)
    {
        this.mp = mp;
        this.direction = "right";
    }

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
        if(user == mp.player){
            int index = mp.cChecker.checkEntityForDamage(this , mp.enemy);
            mp.player.damageEnemy(index);
        } else{
            boolean contactPlayer = mp.cChecker.checkPlayerForDamage(this);
            if(!mp.player.isInvincible && contactPlayer){
                active = false;
                mp.player.receiveDamage(this , user);
                user.projectileCauseEffect();
                mp.player.isInvincible = true;
            }
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

        int i1 = mp.cChecker.checkCollisionWithEntity(this , mp.inactiveObj);
        if(i1 != -1)
        {
            if(mp.inactiveObj[i1].name.equals("Wall")) {
                active = false;
            }
        }
        int i2 = mp.cChecker.checkCollisionWithEntity(this , mp.activeObj);
        if(i2 != -1){
            if(mp.activeObj[i2].name.equals("Door")) {
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
//            System.out.println((projectile_sprite.length-1)+" "+(projectile_sprite[CURRENT_DIRECTION].length-1));
//            System.out.println(CURRENT_DIRECTION+" "+CURRENT_FRAME);
            g2.drawImage(projectile_sprite[CURRENT_DIRECTION][CURRENT_FRAME] , worldX - camera.getX() , worldY - camera.getY() ,
                         width , height , null);
        }
    }

    public void setProjectileSpeed(int speed){
        this.speed = speed;
    }

    public void dispose(){
        for(int i = 0 ; i < projectile_sprite.length ; i++){
            for(int j = 0 ; j < projectile_sprite[i].length ; j++) {
                if(projectile_sprite[i][j] != null)projectile_sprite[i][j].flush();
                projectile_sprite[i][j] = null;
            }
        }
    }
}
