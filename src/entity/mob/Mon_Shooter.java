package entity.mob;

import entity.Actable;
import entity.effect.type.Blind;
import entity.projectile.Proj_Plasma;
import entity.projectile.Projectile;
import entity.Direction;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import map.GameMap;
import util.KeyPair;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static main.GamePanel.camera;
/*
Type of monster only attack in one direction
Only attack at a certain of time, but deal massive damage when the projectile hits and blind player
This thing only take damage when the player shoots in front of it, and when the player is 2 tiles away.
Sleeps when no player is nearby, goes active when the player in its detection range
 */
public class Mon_Shooter extends Monster implements Actable {
    public final static int IDLE = 0;
    public final static int ACTIVE = 1;

    private static final HashMap<KeyPair<ShooterState, Direction>, Sprite> shooterSpritePool = new HashMap<>();
    private static final HashMap<KeyPair<ShooterState, Direction>, Animation> shooterAnimations = new HashMap<>();

    public static void load(){
        for(ShooterState state: ShooterState.values()){
            int speed = switch (state){
                case IDLE -> 100;
                case SHOOT -> 0;
                case ACTIVE -> 20;
                case DIE -> 10;

            };

            boolean loop = switch (state){
                case ACTIVE, IDLE -> true;
                case SHOOT, DIE -> false;
            };
            for(Direction direction: Direction.values()){
                int row = switch (direction){
                    case RIGHT -> 0;
                    case LEFT -> 1;
                    case DOWN -> 2;
                    case UP -> 3;
                };

                KeyPair<ShooterState, Direction> key = new KeyPair<>(state, direction);
                shooterSpritePool.put(key,
                        new Sprite(AssetPool.getImage("shooter_" + state.name().toLowerCase() + ".png")));
                shooterAnimations.put(key,
                        new Animation(shooterSpritePool.get(key).getSpriteArrayRow(row), speed, loop));
            }
        }
    }

    private final boolean isAlwaysUp;

    private final int maxActiveTime = 1800;
    private int activeTimeCounter = 0;
    private int shotInterval = 120;
    private int shootCounter = 0;
    private int shootSpeed = 10;

    private ShooterState currentState;
    private ShooterState lastState;
    private Direction currentDirection;
    private Animation currentAnimation;

    private void setState(){
        boolean change = false;

        if(lastState != currentState){
            lastState = currentState;
            change = true;
        }

        if(change){
            currentAnimation = shooterAnimations.get(new KeyPair<>(currentState, currentDirection)).clone();
            if(currentState == ShooterState.SHOOT){
                currentAnimation.setAnimationSpeed(shootSpeed);
            }
        }

    }

    public int type;

    private ArrayList<Projectile> proj;

    public Mon_Shooter(GameMap mp,
                       String direction,
                       int type,
                       boolean isAlwaysUp,
                       int shotInterval,
                       String idName,
                       int x , int y){
        super(mp , x , y);
        this.mp = mp;
        name = "Shooter";
        this.idName = idName;
        this.type = type;
        this.isAlwaysUp = isAlwaysUp;
        if(isAlwaysUp) this.type = ACTIVE;
        width = 64;
        height = 64;
        speed = 0;
        strength = 10;
        level = 1;

        //getImage();
        setDefault();
        this.direction = direction;
        changeDirection();
        this.shotInterval = shotInterval;
        shootSpeed = Math.max(shotInterval / 5 , 1);
    }
    public Mon_Shooter(GameMap mp,
                       String direction,
                       int type,
                       boolean isAlwaysUp,
                       int shotInterval,
                       Rectangle detectionArea,
                       String idName,
                       int x , int y){
        super(mp , x , y);
        this.mp = mp;
        name = "Shooter";
        this.type = type;
        this.idName = idName;
        this.isAlwaysUp = isAlwaysUp;
        if(isAlwaysUp) this.type = ACTIVE;

        width = 64;
        height = 64;
        speed = 0;
        strength = 10;
        level = 1;

        //getImage();
        setDefault();
        this.direction = direction;
        changeDirection();
        this.shotInterval = shotInterval;
        shootSpeed = Math.max(shotInterval / 5 , 1);
        interactionDetectionArea = detectionArea;
    }


    private void setDefault(){
        maxHP = 200;
        currentHP = maxHP;
        invincibleDuration = 60;
        projectileName = "Plasma";
        projectile = new Proj_Plasma(mp);
        proj = new ArrayList<>();
        effectDealOnTouch = new Blind(mp.player , 120);
        effectDealByProjectile = new Blind(mp.player , 600);

        expDrop = 20;

        hitbox = new Rectangle (18 , 37 , 24 , 24);
        solidArea1 = hitbox;
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        interactionDetectionArea = new Rectangle(-100 , -100 , width + 200 , height + 200);
        setDefaultSolidArea();

        direction = "right";
        currentDirection = Direction.RIGHT;
        if(type == IDLE){
            currentState = ShooterState.IDLE;
            lastState = ShooterState.IDLE;
            currentAnimation = shooterAnimations.get(new KeyPair<>(currentState, currentDirection))
                            .clone();
            currentAnimation.setAnimationSpeed(10);
        } else{
            currentState = ShooterState.ACTIVE;
            lastState = ShooterState.ACTIVE;
            currentAnimation = shooterAnimations.get(new KeyPair<>(currentState, currentDirection))
                    .clone();
            currentAnimation.setAnimationSpeed(100);
        }
    }

    public void move() {
        //No movement
    }

    public void setDialogue() {

    }

    public void loot() {

    }
    /*
    Phương thức tấn công: Tấn công theo chu kì
     */
    public void updateAttackCycle() {
        if(currentState == ShooterState.ACTIVE && !isDying){
            proj.removeIf(pr -> !pr.active);
            shootCounter++;
            if(shootCounter >= shotInterval){
                isShooting = true;
                shootCounter = 0;
            }
        }
    }

    public void attack(){
        if(!isDying) {
            Projectile bullet = new Proj_Plasma(mp);
            bullet.set(worldX , worldY , direction , true , this);
            bullet.setHitbox();
            proj.add(bullet);
            mp.addObject(bullet , mp.projectiles);
        }
    }

    private void changeDirection(){
        switch (direction){
            case "right" : currentDirection = Direction.RIGHT; break;
            case "left"  : currentDirection = Direction.LEFT; break;
            case "up"    : currentDirection = Direction.UP; break;
            case "down"  : currentDirection = Direction.DOWN ; break;
        }
    }

    private void checkForPlayer(){
        if(mp.cChecker.checkInteractPlayer(this)) isInteracting = true;
        damagePlayer();
        if(currentState == ShooterState.IDLE) currentState = ShooterState.ACTIVE;
        if(!isInteracting && currentState == ShooterState.ACTIVE){
            activeTimeCounter++;
            if(activeTimeCounter >= maxActiveTime){
                activeTimeCounter = 0;
                currentState = ShooterState.IDLE;
            }
        } else if(isInteracting) activeTimeCounter = 0;
        isInteracting = false;
    }

    private void handleAnimation(){
        if(isDying){
            currentState = ShooterState.DIE;
        }
        if(currentState == ShooterState.ACTIVE){
            if(isShooting){
                currentState = ShooterState.SHOOT;
            }
        }

        setState();

        if(currentAnimation.isFinished() && isDying){
            isDying = false;
            canbeDestroyed = true;
        }

        if(currentAnimation.isFinished() && isShooting){
            isShooting = false;
            attack();
            currentState = ShooterState.ACTIVE;
        }
    }


    private void updateHP(){
        if(currentHP <= 0) isDying = true;
    }

    @Override
    public void update() throws NullPointerException{
        if(!isAlwaysUp) checkForPlayer();
        updateAttackCycle();
        updateInvincibility();
        handleAnimation();
        updateHP();
        currentAnimation.update();
    }

    @Override
    public void render(Graphics2D g2) {
        super.renderHPBar(g2 , 18 , 9);
        if(isInvincible && !isDying){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 0.3f));
        }
        //g2.drawImage(mon_shooter[CURRENT_ACTION][CURRENT_DIRECTION][CURRENT_FRAME] , worldX - camera.getX() , worldY - camera.getY() , width , height , null);
        currentAnimation.render(g2, worldX - camera.getX(), worldY - camera.getY());
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1.0f));
    }

    public int getInterval(){return shotInterval;}
    public void setInterval(int dt){this.shotInterval = dt;}
    public void setDirection(String dir){
        direction = dir;
        changeDirection();
    }

    public void dispose(){
        solidArea1 = null;
        solidArea2 = null;
        hitbox = null;
        interactionDetectionArea = null;
        projectile = null;
        proj.clear();
        proj = null;
        projectileName = null;
        effectDealByProjectile = null;
        effectDealOnTouch = null;
    }

    private enum ShooterState{
        IDLE, SHOOT, ACTIVE, DIE
    }
}
