package entity.player;

import entity.Entity;
import entity.projectile.Obj_BasicProjectile;
import entity.projectile.Obj_GreenBullet;
import entity.projectile.Projectile;
import graphics.Sprite;
import main.GamePanel;
import main.GameState;
import main.KeyHandler;
import map.GameMap;
import graphics.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity {

    GameMap mp;
    final int IDLE = 0;
    final int RUN = 1;
    final int TALK = 2;
    final int SHOOT = 3;
    final int RELOAD = 4;
    final int DEATH = 5;

    final int RIGHT = 0;
    final int LEFT = 1;

    private int PREVIOUS_ACTION;
    private int CURRENT_ACTION;
    private int CURRENT_DIRECTION;

    private boolean isRunning;
    private boolean isShooting;
    private boolean isReloading;
    private boolean isDying;

    private boolean attackCanceled;

    private boolean shoot;
    private boolean die;
    
    public final int screenX, screenY;

    private final BufferedImage[][][] player_gun = new BufferedImage[7][][];;
    private final BufferedImage[][][] player_nogun = new BufferedImage[7][][];

    private int CURRENT_FRAME;
    private int SHOOT_INTERVAL ;

    final protected Animation animator = new Animation();

    //PLAYER STATUS
    private final int invincibleDuration = 90;

    public Player(GameMap mp) {
        super();
        this.mp = mp;
        width = 64;
        height = 64;

        solidArea1 = new Rectangle(27 , 53 , 13 , 6);
        solidAreaDefaultX1 = 27;
        solidAreaDefaultY1 = 53;

        screenX = GamePanel.windowWidth/2 - 32;
        screenY = GamePanel.windowHeight/2 - 32;

        getPlayerImages();
        setDefaultValue();
    }

    private void setDefaultValue()
    {
        projectile = new Obj_BasicProjectile(mp);
        SHOOT_INTERVAL = projectile.maxHP + 10;
        level = 1;
        set();

        worldX = 1400;
        worldY = 1700;
        newWorldX = worldX;
        newWorldY = worldY;
        speed = 3;


        attackCanceled = false;
        up = down = left = right = false;
        direction = "right";
        CURRENT_DIRECTION = RIGHT;
        PREVIOUS_ACTION = IDLE;
        CURRENT_ACTION = IDLE;
        CURRENT_FRAME = 0;
        animator.setAnimationState(player_gun[IDLE][RIGHT] , 5);
    }

    private void getPlayerImages()
    {
        player_gun[IDLE]   = new Sprite("/entity/player/idle_gun_blue.png"   , width , height).getSpriteArray();
        player_gun[TALK]   = new Sprite("/entity/player/talk_gun_blue.png"   , width , height).getSpriteArray();
        player_gun[RUN]    = new Sprite("/entity/player/run_gun_blue.png"    , width , height).getSpriteArray();
        player_gun[SHOOT]  = new Sprite("/entity/player/shoot_gun_blue.png"  , width , height).getSpriteArray();
        player_gun[RELOAD] = new Sprite("/entity/player/reload_gun_blue.png" , width , height).getSpriteArray();
        player_gun[DEATH]  = new Sprite("/entity/player/death_blue.png"      , width , height).getSpriteArray();
    }



    private void keyInput()
    {
        up    = KeyHandler.upPressed;
        down  = KeyHandler.downPressed;
        left  = KeyHandler.leftPressed;
        right = KeyHandler.rightPressed;

        //RUN
        isRunning = up | down | left | right;

        if(GamePanel.gameState == GameState.PLAY_STATE) attackCanceled = false; else
            if(GamePanel.gameState == GameState.DIALOGUE_STATE) attackCanceled = true;
        //SHOOT
        if (KeyHandler.enterPressed) {
            if (!attackCanceled && !isRunning && shootAvailableCounter == SHOOT_INTERVAL) {
                isShooting = true;
                shootProjectile();
            }
        }
        //isShooting = shoot;
    }

    private void shootProjectile() {
        if(!projectile.active && !isInteracting && shootAvailableCounter == SHOOT_INTERVAL){
            mp.gp.playSoundEffect(2);
            projectile.set(worldX , worldY , direction , true , this);
            for(int i = 0; i < mp.projectiles.length; i++)
            {
                if(mp.projectiles[i] == null)
                {
                    mp.projectiles[i] = projectile;
                    break;
                }
            }
            shootAvailableCounter = 0;
        }
    }

    private void handleAnimationState()
    {
        if(isShooting && !isRunning  && !attackCanceled ) {
            CURRENT_ACTION = SHOOT;

        }else if(isRunning && !animator.isPlaying())
        {
            CURRENT_ACTION = RUN;
            if(left) direction = "left";
            else if(right) direction = "right";
        } else
        {
            animator.setAnimationState(player_gun[IDLE][CURRENT_DIRECTION] , 5);
            CURRENT_ACTION = IDLE;
            PREVIOUS_ACTION = IDLE;
        }

        if(PREVIOUS_ACTION != CURRENT_ACTION)
        {
            PREVIOUS_ACTION = CURRENT_ACTION;
            if(isRunning) animator.setAnimationState(player_gun[CURRENT_ACTION][CURRENT_DIRECTION] , 10);
            if(isShooting && !isRunning)
            {
                animator.setAnimationState(player_gun[SHOOT][CURRENT_DIRECTION] , 6);
                animator.playOnce();
            }
        }
        if (!animator.isPlaying() && isShooting) isShooting = false;
    }

    private void changeDirection()
    {
        switch(direction)
        {
            case "left" : CURRENT_DIRECTION = LEFT; break;
            case "right": CURRENT_DIRECTION = RIGHT; break;
        }
    }

//    private void handleCollision(){
//        collisionOn = false;
//        onlyChangeDirection = false;
//        mp.cChecker.checkObjectCollsion(this);
//    }

    private void handlePosition()
    {
        isInteracting = false;
        interactNpc(mp.cChecker.checkInteractWithNpc(this , true));
        updateHP(mp.cChecker.checkInteractWithNpc(this , true));
        interactObject(mp.cChecker.checkInteractWithActiveObject(this , true));
        collisionOn = false;
        if (up && isRunning && !isShooting) {
            if(right){newWorldX += 1; newWorldY -= 1;} else
            if(left){newWorldX -= 1 ; newWorldY -= 1;} else
                if(!down) newWorldY -= speed;
        }
        if (down && isRunning && !isShooting) {
            if(right){newWorldX += 1; newWorldY += 1;} else
            if(left){newWorldX -= 1 ; newWorldY += 1;} else
            if(!up) newWorldY += speed;
        }
        if (left && isRunning && !isShooting) {
            if(up){newWorldX -= 1; newWorldY -= 1;} else
            if(down){newWorldX -= 1 ; newWorldY +=1;} else
                if(!right) newWorldX -= speed;
        }
        if (right && isRunning && !isShooting) {
            if(up){newWorldX += 1; newWorldY -= 1;} else
            if(down){newWorldX += 1 ; newWorldY += 1;} else
            if(!left) newWorldX += speed;
        }

        mp.cChecker.checkCollisionWithEntity(this , mp.inactiveObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.activeObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.npc);

        if(!collisionOn)
        {
            worldX = newWorldX;
            worldY = newWorldY;
        }
        newWorldX = worldX;
        newWorldY = worldY;

        GamePanel.camera.centerOn(worldX , worldY);
    }

    private void handleStatus(){
        if(shootAvailableCounter < SHOOT_INTERVAL){
            shootAvailableCounter++;
        }
        if(shootAvailableCounter > SHOOT_INTERVAL) shootAvailableCounter = SHOOT_INTERVAL;

        if(isInvincible){
            invincibleCounter++;
            if(invincibleCounter >= invincibleDuration){
                invincibleCounter = 0;
                isInvincible = false;
            }
        }
    }

    private void interactNpc(int index)
    {
        if(index != -1)
        {
            attackCanceled = true;
            isInteracting = true;
            if(GamePanel.gameState == GameState.PLAY_STATE && KeyHandler.enterPressed) {
                KeyHandler.enterPressed = false;
                GamePanel.gameState = GameState.DIALOGUE_STATE;
                mp.npc[index].talk();
            }
        }
    }

    private void interactObject(int index)
    {
        if(index != -1){
            attackCanceled = true;
            isInteracting = true;
            if(KeyHandler.enterPressed)
            {
                KeyHandler.enterPressed = false;
                mp.activeObj[index].isOpening = true;
            }
        }
    }

    public void damageEnemy(int index){
        if(index != -1){
            projectile.active = false;
            if(!isInvincible) {
                mp.enemy[index].currentHP -= damage;
                mp.enemy[index].isInvincible = true;
                System.out.println("Hit! Deal " + damage + " damage to the enemy!");
            }
            if(mp.enemy[index].currentHP <= 0){
                mp.enemy[index].currentHP = 0;
                mp.enemy[index].isDying = true;
            }
        }
    }

    //DEMO
    private void updateHP(int index) {
        if (index != -1)
            if(!isInvincible) {
                isInvincible = true;
                currentHP = Math.max(0, currentHP - 1);
            }
        if (currentHP == 0) GamePanel.gameState = GameState.LOSE_STATE;
    }

    @Override
    public void set(){
        setDamage();
        setDefense();
        setMaxHP();
        setMaxMana();
    }

    private void setDamage(){
        damage = projectile.base_damage + projectile.base_damage * level;
    }
    private void setDefense(){
        defense = level * 10;
    }
    private void setMaxHP(){
        maxHP = 100 + (level - 1) * 20;
        currentHP = maxHP;
    }
    private void setMaxMana(){
        maxMana = 50 + (level - 1) * 10;
        currentMana = maxMana;
    }
    public void checkForLevelUp(){

    }

    @Override
    public void update()
    {
        keyInput();
        handlePosition();
        handleStatus();
        changeDirection();
        //handleCollision();
        handleAnimationState();
        animator.update();
        CURRENT_FRAME = animator.getCurrentFrames();

    }


    @Override
    public void render(Graphics2D g2)
    {
        if(isInvincible && !isDying){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 0.6f));
        }
        g2.drawImage(player_gun[CURRENT_ACTION][CURRENT_DIRECTION][CURRENT_FRAME] ,
                worldX - GamePanel.camera.getX() , worldY - GamePanel.camera.getY(),
                width, height, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1.0f));
        g2.setFont(GamePanel.ui.joystix.deriveFont(Font.PLAIN, 19));
        g2.setColor(Color.WHITE);
        g2.drawString("Shoot interval: " + shootAvailableCounter , 10 , 30);
    }
}
