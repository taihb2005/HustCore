package entity.mob;

import entity.Actable;
import entity.effect.type.EffectNone;
import entity.effect.type.Slow;
import entity.projectile.Proj_TrackingPlasma;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import map.GameMap;
import util.KeyPair;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;

import static main.GamePanel.camera;
import static map.GameMap.childNodeSize;

public class Mon_Cyborgon extends Monster implements Actable {
    private static final HashMap<KeyPair<CyborgonState, Direction>, Sprite> cyborgonSpritePool = new HashMap<>();
    private static final HashMap<KeyPair<CyborgonState, Direction>, Animation> cyborgonAnimations = new HashMap<>();

    public static void load(){
        for(CyborgonState state: CyborgonState.values()){
            int speed = switch (state){
                case IDLE, RUN, DIE -> 10;
                case ACTIVATING -> 14;
                case INACTIVE -> 120;
                case BLINK -> 17;
                case SHOOT -> 11;
            };

            boolean loop = switch (state){
                case IDLE, INACTIVE, RUN -> true;
                case ACTIVATING, BLINK, SHOOT, DIE -> false;
            };
            for(Direction direction: Direction.values()){
                int row = switch (direction){
                    case RIGHT -> 0;
                    case LEFT -> 1;
                };
                KeyPair<CyborgonState, Direction> key = new KeyPair<>(state, direction);

                cyborgonSpritePool.put(key,
                        new Sprite(AssetPool.getImage("cyborgon_" + state.name().toLowerCase() + ".png")));
                cyborgonAnimations.put(key,
                        new Animation(cyborgonSpritePool.get(key).getSpriteArrayRow(row), speed, loop));
            }
        }
    }

    private CyborgonState currentState;
    private CyborgonState lastState;
    private Direction currentDirection;
    private Direction lastDirection;
    private Animation currentAnimation;

    private BufferedImage radiationImage;
    private int lastRenderedDiameter = -1;

    private void setState(){
        boolean change1 = false;
        boolean change2 = false;
        if(lastState != currentState){
            lastState = currentState;
            change1 = true;
        }

        if(lastDirection != currentDirection){
            lastDirection = currentDirection;
            change2 = true;
        }

        if(change1 || change2){
            currentAnimation.reset();
            currentAnimation = cyborgonAnimations.get(new KeyPair<>(currentState, currentDirection));
        }
    }

    private boolean isActive;
    private boolean isBlinking;
    private boolean isActivating;

    private int diameter = 100;
    private int MAX_DIAMETER = 500;
    private int newDiameter = diameter;
    private float alpha = 0.1f;
    private boolean increasing = true;
    private int exposureTime = 0;

    private int actionLockCounter = 0;
    private final int changeDirCounter = 240;

    public Mon_Cyborgon(GameMap mp,  String idName, int x , int y){
        super(mp , x , y);
        name = "Cyborgon";
        width = 64;
        height = 64;

        setDefault();
        this.idName = idName;
    }

    private void setDefault(){
        hitbox = new Rectangle(22 , 24 , 22 , 36);
        solidArea1 = new Rectangle(18 , 40 , 28 , 22);
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        setDefaultSolidArea();

        isActive = false;
        invincibleDuration = 40;
        maxHP = 200;
        currentHP = maxHP;
        strength = 10;
        level = 1;
        defense = 0;
        projectile = new Proj_TrackingPlasma(mp);
        projectile.setProjectileSpeed(4);
        effectDealOnTouch = new EffectNone(mp.player);
        effectDealByProjectile = new Slow(mp.player , 90);
        speed = 2;
        lastSpeed = speed;

        expDrop = 20;

        SHOOT_INTERVAL = projectile.maxHP + 5;

        int dir = new Random().nextInt(2);
        if(dir == 0) {
            direction = "right";
            currentDirection = Direction.RIGHT;
        } else {
            direction = "left";
            currentDirection = Direction.LEFT;
        }

        currentState = CyborgonState.INACTIVE;
        lastState = CyborgonState.INACTIVE;

        currentAnimation = cyborgonAnimations.get(new KeyPair<>(currentState, currentDirection)).clone();
    }

    private void handleAnimation() {
        if (!isActive)
            handleAnimationWhenInactive();
        else handleAnimationWhenActive();
    }

    private void handleAnimationWhenActive() {
        if (isDying) {
            currentState = CyborgonState.DIE;
        } else if (isShooting) {
            currentState = CyborgonState.SHOOT;
        } else if (isBlinking){
            currentState = CyborgonState.BLINK;
        } else if (isRunning) {
            currentState = CyborgonState.RUN;
        } else {
            if(isActive) {
                currentState = CyborgonState.IDLE;
            } else {
                currentState = CyborgonState.INACTIVE;
            }
        }

        setState();

        if (currentAnimation.isFinished()) {
            if (isShooting) {
                isShooting = false;
            }
            if (isDying) {
                isDying = false;
                canbeDestroyed = true;
            }
            if(isActivating){
                isActive = true;
                isActivating = false;
                currentState = CyborgonState.IDLE;
            }
            if(isBlinking){
                isBlinking = false;
                currentState = CyborgonState.IDLE;
            }
        }
    }

    private void handleAnimationWhenInactive() {
        if(currentHP < maxHP){
            currentState = CyborgonState.ACTIVATING;
        }

        setState();

        if(currentAnimation.isFinished()){
            if(currentState == CyborgonState.ACTIVATING){
                isActive = true;
                currentState = CyborgonState.IDLE;
            }
        }
    }

    private void changeDirection() {
        switch (direction) {
            case "left" -> currentDirection = Direction.LEFT;
            case "right" -> currentDirection = Direction.RIGHT;
        }
    }

    private void setAction(){
        if(isActive){
            if(!getAggro){
                actionWhenNeutral();
            } else {
                actionWhenGetAggro();
            }
            isRunning = (up | down | right | left) && (!isShooting);
        }
        damagePlayer();
    }

    private void actionWhenGetAggro() {
        int playerCol = (mp.player.worldX + mp.player.solidArea1.x) / childNodeSize;
        int playerRow = (mp.player.worldY + mp.player.solidArea1.y) / childNodeSize;

        searchPath(playerCol , playerRow);
        decideToMove();
        attack();
        isRunning = !isShooting;
    }

    private void actionWhenNeutral() {
        actionLockCounter++;
        if (actionLockCounter >= changeDirCounter && !isDying && !isShooting) {
            up = down = left = right = false;
            Random random = new Random();
            int i = random.nextInt(100) + 1;  // pick up  a number from 1 to 100
            if(i <= 60){
                up = down = left = right = false;
            }
            if ( i > 60 && i <= 70) {
                direction = "up";
                up = true;
            }
            if (i > 70 && i <= 80) {
                direction = "down";
                down = true;
            }
            if (i > 80 && i <= 90) {
                direction = "left";
                left = true;
            }
            if (i > 90 && i < 100) {
                direction = "right";
                right = true;
            }
            actionLockCounter = 0;
            isRunning = !isShooting && (right | left | up | down);
        }
        if (currentHP <  maxHP) getAggro = true;
    }


    public void move() {
        collisionOn = false;
        if(up && isRunning && !isDying) newWorldY = worldY - speed;
        if(down && isRunning && !isDying) newWorldY = worldY + speed;
        if(left && isRunning && !isDying) newWorldX = worldX - speed;
        if(right && isRunning && !isDying) newWorldX = worldX + speed;

        mp.cChecker.checkCollisionWithEntity(this , mp.inactiveObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.activeObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.npc);
        mp.cChecker.checkCollisionWithEntity(this , mp.enemy);
        mp.cChecker.checkCollisionPlayer(this);

        if(!collisionOn)
        {
            worldX = newWorldX;
            worldY = newWorldY;
        } else {
            if(!onPath){
                if(checkForValidDirection()){
                    direction = getValidDirection();
                    decideToMove();
                    actionLockCounter = 0;
                }
            }
        }

        newWorldX = worldX;
        newWorldY = worldY;
    }

    public void setDialogue() {

    }

    public void attack() {
        if(!projectile.active &&shootAvailableCounter == SHOOT_INTERVAL) {
            projectile.set(worldX, worldY, direction, true, this);
            projectile.setHitbox();
            projectile.setSolidArea();
            mp.addObject(projectile, mp.projectiles);
            shootAvailableCounter = 0;
        }
    }

    public void loot() {

    }

    @Override
    public void update(){
        setAction();
        handleStatus();
        updateDiameter();
        changeDirection();
        move();
        handleAnimation();
        updateInvincibility();
        currentAnimation.update();
    }

    @Override
    public void render(Graphics2D g2) {
        super.renderHPBar(g2, 18, 0);

        if (getAggro && radiationImage != null) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            int x = (worldX + width / 2 - diameter / 2) - camera.getX() - 6;
            int y = (worldY + height / 2 - diameter / 2) - camera.getY() - 6;
            g2.drawImage(radiationImage, x, y, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            updateOpacity();
        }

        if (isInvincible && !isDying) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        currentAnimation.render(g2, worldX - camera.getX(), worldY - camera.getY());
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }


    private void updateOpacity() {
        if (isActive) {
            if (increasing) {
                alpha += 0.001f;
                if (alpha >= 0.08f) {
                    alpha = 0.08f;
                    increasing = false;
                }
            } else {
                alpha -= 0.001f;
                if (alpha <= 0f) {
                    alpha = 0f;
                    increasing = true;
                }
            }
        } else {
            if (alpha > 0f) {
                alpha -= 0.001f;
                if (alpha < 0f) alpha = 0f;
            }
        }
    }


    private void updateDiameter() {
        newDiameter = 100 + (maxHP - currentHP) * 5;
        if (newDiameter > MAX_DIAMETER) newDiameter = MAX_DIAMETER;

        if (diameter != newDiameter) {
            diameter++;
            if (diameter != lastRenderedDiameter) {
                updateRadiationImageAsync();
            }
        }

        if (Math.pow(worldX - mp.player.worldX, 2) + Math.pow(worldY - mp.player.worldY, 2) < diameter * diameter) {
            exposureTime++;
            if (exposureTime == 300) {
                mp.player.currentHP = (int) (0.9 * mp.player.currentHP);
                exposureTime = 0;
            }
        }
    }

    private void updateRadiationImageAsync() {
        int currentRenderDiameter = diameter;

        new Thread(() -> {
            BufferedImage tempImage = new BufferedImage(currentRenderDiameter + 12, currentRenderDiameter + 12, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = tempImage.createGraphics();

            //g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g.setColor(Color.GREEN);
            g.fillOval(6, 6, currentRenderDiameter, currentRenderDiameter);
            g.setColor(Color.GREEN);
            g.fillOval(0, 0, currentRenderDiameter + 12, currentRenderDiameter + 12);
            g.dispose();

            SwingUtilities.invokeLater(() -> {
                radiationImage = tempImage;
                lastRenderedDiameter = currentRenderDiameter;
            });
        }).start();
    }


    public void dispose(){
        solidArea1 = null;
        solidArea2 = null;
        hitbox = null;
        interactionDetectionArea = null;
        projectile = null;
        projectileName = null;
    }

    private enum CyborgonState{
        IDLE, ACTIVATING, INACTIVE, BLINK, RUN, SHOOT, DIE
    }
}