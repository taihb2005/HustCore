package entity.player;

import ai.PathFinder;
import entity.Direction;
import entity.effect.Effect;
import entity.Entity;
import entity.items.Item;
import entity.mob.Monster;
import entity.projectile.Proj_BasicProjectile;
import entity.projectile.Projectile;
import graphics.AssetPool;
import graphics.Sprite;
import graphics.environment.EnvironmentManager;
import level.LevelState;
import main.GameState;
import main.KeyHandler;
import map.GameMap;
import graphics.Animation;
import util.KeyPair;
import util.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static main.GamePanel.*;
import static map.GameMap.childNodeSize;

public class Player extends Entity {

    GameMap mp;

    final static int RIGHT = 0;
    final static int LEFT = 1;
    final static int DOWN = 2;
    final static int UP = 3;

    private static final HashMap<PlayerState, Sprite> playerSpritePool = new HashMap<>();
    private static final HashMap<KeyPair<PlayerState, Direction>, Animation> playerAnimations = new HashMap<>();

    public static void loadPlayer(){
        playerSpritePool.put(PlayerState.IDLE, new Sprite(AssetPool.assetPool.get("idle_gun_blue.png")));
        playerSpritePool.put(PlayerState.RUN, new Sprite(AssetPool.assetPool.get("run_gun_blue.png")));
        playerSpritePool.put(PlayerState.TALK, new Sprite(AssetPool.assetPool.get("talk_gun_blue.png")));
        playerSpritePool.put(PlayerState.SHOOT, new Sprite(AssetPool.assetPool.get("shoot_gun_blue.png")));
        playerSpritePool.put(PlayerState.DIE, new Sprite(AssetPool.assetPool.get("die_blue.png")));

        for (PlayerState state : PlayerState.values()) {
            Sprite sprite = playerSpritePool.get(state);
            if (sprite == null) continue;
            int animationSpeed = switch (state){
                case IDLE -> 5;
                case RUN, DIE -> 10;
                case TALK, SHOOT -> 6;
                case RELOAD -> 0;
            };

            boolean loop = switch (state){
                case SHOOT, DIE ->  false;
                default -> true;
            };

            for (Direction dir : Direction.values()) {
                int row = switch (dir){
                    case RIGHT -> RIGHT;
                    case LEFT -> LEFT;
                    case DOWN -> DOWN;
                    case UP -> UP;
                };

                if(row < sprite.spriteRows) {
                    playerAnimations.put(
                            new KeyPair<>(state, dir),
                            new Animation(sprite.getSpriteArrayRow(row), animationSpeed, loop)
                    );
                }
            }
        }
    }
    private boolean isAutoMoving;
    private int goalCol, goalRow;
    private PlayerState currentState;
    private PlayerState lastState;
    private Direction currentDirection;
    private Direction lastDirection;

    //private Animation currentAnimation;
    private void setState(){
        boolean change1 = false;
        boolean change2 = false;
        if(lastState != currentState){
            this.lastState = currentState;
            change1 = true;
        }

        if(lastDirection != currentDirection){
            this.lastDirection = currentDirection;
            change2 = true;
        }

        if(change1 || change2) {
            currentAnimation.reset();
            currentAnimation = playerAnimations.get(new KeyPair<>(currentState, currentDirection));
            currentAnimation.reset();
        }
    }

    private boolean isRunning;
    private boolean isShooting;
    private boolean confused;
    public boolean isDying = false;

    public boolean attackCanceled;
    public final int screenX, screenY;

    public int SHOOT_INTERVAL ;
    public int nextLevelUp = 60;
    //PLAYER STATUS
    public int blindRadius = 200;
    private final int invincibleDuration = 60;
    private final int manaHealInterval = 180;
    private int manaHealCounter = 0;
    public HashMap<String , Integer> effectManager = new HashMap<>();
    public ArrayList<Effect> effect = new ArrayList<>();
    public Item [] inventory = new Item[5];
    public ItemHandler iHandler = new ItemHandler();

    public Player(GameMap mp) {
        super();
        name = "Player";
        this.mp = mp;

        pFinder = new PathFinder(mp);
        hitbox = new Rectangle(25 , 40 , 15 , 20);
        solidArea1 = new Rectangle(26 , 52 , 18 , 6);
        setDefaultSolidArea();

        screenX = windowWidth/2 - 32;
        screenY = windowHeight/2 - 32;

        setDefaultValue();
    }

    public void setDefaultValue()
    {
        speed = 2;
        lastSpeed = speed;

        projectileName = "Basic Projectile";
        projectile = new Proj_BasicProjectile(mp);
        SHOOT_INTERVAL = projectile.maxHP + 5;

        attackCanceled = false;
        up = down = left = right = false;
        direction = "right";
        confused = false;

        currentState = PlayerState.IDLE;
        lastState = PlayerState.IDLE;
        currentDirection = Direction.RIGHT;
        lastDirection = Direction.RIGHT;

        currentAnimation = playerAnimations.get(new KeyPair<>(currentState, currentDirection));

        Arrays.fill(inventory , null);
        resetValue();
    }

    public void storeValue(){
        sManager.setPos(position);
        sManager.setSavedHP(maxHP);
        sManager.setSavedMana(maxMana);
        sManager.setLevel(level);
        sManager.setExp(exp);
        sManager.setInventory(inventory);
        sManager.setDirection(direction);
    }

    public void resetValue(){
        effectManager.clear();
        effect.clear();
        level = sManager.getSavedLevel();
        exp = sManager.getSavedExp();
        position = sManager.getPosition();
        newPosition = sManager.getPosition();
        sManager.getSavedInventory(inventory);
        set();
    }

    private void autoMoving(){
        searchPath(goalCol, goalRow);
        decideToMove();

        int currentCol = ((int)position.x + solidArea1.x) / childNodeSize;
        int currentRow = ((int)position.y + solidArea1.y) / childNodeSize;

        if(currentRow == goalRow && currentCol == goalCol){
            isAutoMoving = false;
            currentLevel.setLevelState(LevelState.RUNNING);
            currentLevel.onBeginLevel.run();
        }
    }

    private void keyInput()
    {
        //GOD MODE
        if(KeyHandler.godModeOn){
            hitbox = new Rectangle(0 , 0 , 0 , 0);
            solidArea1 = new Rectangle(0 , 0 , 0 , 0);
        }

        if(isDying) KeyHandler.disableKey();
        up    = KeyHandler.upPressed;
        down  = KeyHandler.downPressed;
        left  = KeyHandler.leftPressed;
        right = KeyHandler.rightPressed;

        //RUN
        isRunning = up | down | left | right;

        if(gameState == GameState.PLAY) attackCanceled = false;
        if(currentLevel.checkState(LevelState.DIALOGUE)) attackCanceled = true;
        //SHOOT
        if (KeyHandler.enterPressed) {
            if (!attackCanceled && !isRunning && shootAvailableCounter == SHOOT_INTERVAL) {
                isShooting = true;
                shootProjectile();
            }
        }
        iHandler.useItem(this);

        //isShooting = shoot;
    }

    private void handleAnimation() {
        if(isShooting && !isRunning  && !attackCanceled ) {
            currentState = PlayerState.SHOOT;
        }else if(isRunning)
        {
            currentState = PlayerState.RUN;
            switchDirection();
        } else if(isDying){
            currentState = PlayerState.DIE;
        } else
        {
            currentState = PlayerState.IDLE;
        }

        setState();

        if ((currentAnimation.isFinished() && isShooting)) {
            isShooting = false;
        }

        if(isRunning){
            isShooting = false;
        }

        if (currentAnimation.isFinished() && isDying){
            isDying = false;
            gameState = GameState.LOSE;
            stopMusic();
            playMusic(4);
        }
    }

    private void changeAnimationDirection() {
        switch(direction)
        {
            case "left" : currentDirection = Direction.LEFT; break;
            case "right": currentDirection = Direction.RIGHT; break;
            case "up"   : currentDirection = Direction.UP; break;
            case "down" : currentDirection = Direction.DOWN ; break;
        }
    }

    private void switchDirection(){
        if(!confused) {
            if (left) direction = "left";
            else if (right) direction = "right";
            else if (up) direction = "up";
            else if (down) direction = "down";
        } else {
            if (left) direction = "right";
            else if (right) direction = "left";
            else if (up) direction = "down";
            else if (down) direction = "up";
        }
    }

    private void handlePosition() {
        isRunning = up | down | left | right;
        isInteracting = false;
        interactNpc(mp.cChecker.checkInteractEntity(this , true , mp.npc));
        interactObject(mp.cChecker.checkInteractWithActiveObject(this , true));
        collisionOn = false;

        velocity = new Vector2D(0, 0);

        if (up) velocity = velocity.add(new Vector2D(0, -1));
        if (down) velocity = velocity.add(new Vector2D(0, 1));
        if (left) velocity = velocity.add(new Vector2D(-1, 0));
        if (right) velocity = velocity.add(new Vector2D(1, 0));

        if(confused) velocity = velocity.scale(-1);

        if (velocity.length() != 0) {
            velocity = velocity.normalize().scale(speed);
        }

        newPosition = position.add(velocity);

        mp.cChecker.checkCollisionWithEntity(this , mp.inactiveObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.activeObj);
        mp.cChecker.checkCollisionWithEntity(this , mp.npc);
        mp.cChecker.checkCollisionWithEntity(this, mp.enemy);

        if(!collisionOn) {
            position = newPosition.copy();
        }
        newPosition = position.copy();

        if(isShooting && !attackCanceled){
            camera.cameraShake(position);
        } else camera.centerOn(position);
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

    private void interactNpc(int index) {
        if(index != -1)
        {
            mp.npc[index].isInteracting = true;
            attackCanceled = true;
            isInteracting = true;
            if(currentLevel.checkState(LevelState.RUNNING) && KeyHandler.enterPressed) {
                KeyHandler.enterPressed = false;
                mp.npc[index].talk();
            }
        }
    }

    private void interactObject(int index) {
        if(index != -1){
            attackCanceled = true;
            isInteracting = true;
        }
    }

    private void shootProjectile() {
        checkForMana();
        if(!projectile.active && !isInteracting && shootAvailableCounter == SHOOT_INTERVAL && hasResource()){
            playSE(2);
            projectile.set(position, direction, true, this);
            projectile.setHitbox();
            projectile.setSolidArea();
            currentMana -= projectile.manaCost;
            updateMana();
            mp.addObject(projectile , mp.projectiles);
            shootAvailableCounter = 0;
        }
    }

    public void damageEnemy(int index){
        if(index != -1){
            switch (mp.enemy[index].name){
                case "Spectron": mp.playerAttack.damageEnemy(index); projectile.active = false ; break;
                case "Shooter": mp.playerAttack.damageShooter(index); projectile.active = false; break;
                case "Hust Guardian": mp.playerAttack.damageGuardian(index); projectile.active = false; break;
                case "Cyborgon"   : mp.playerAttack.damageCyborgon(index); projectile.active = false; break;
                case "Effect Dealer": mp.playerAttack.damageEffectDealer(index); break;
                case "Boss": mp.playerAttack.damageEnemy(index); projectile.active = false; break;
            }
            if(mp.enemy[index].currentHP <= 0){
                exp += mp.enemy[index].expDrop;
                System.out.println("Current exp: " + exp);
                mp.enemy[index].currentHP = 0;
                mp.enemy[index].die();

                checkForLevelUp();

            }
        }
    }

    public void receiveDamage(Projectile proj , Entity attacker){
        currentHP = currentHP - (proj.baseDamage + attacker.strength) ;
        System.out.println("Receive " + ((proj.baseDamage + attacker.strength)) + " damage");
    }

    public void receiveDamage(Monster attacker){
        currentHP = currentHP - (attacker.strength);
        System.out.println("Receive " + ((attacker.strength)) + " damage");
    }

    public void updateInventory(){
        for(int i = 0 ; i < inventory.length ; i++){
            if (inventory[i] != null) {
                if(inventory[i].getQuantity() == 0){
                    inventory[i] = null;
                }
            }
        }
    }
    //DEMO
    private void updateHP() {
        if(currentHP > maxHP) currentHP = maxHP; else
            if(currentHP < 0) currentHP = 0;
        if (currentHP == 0) {
            isRunning = false;
            isDying = true;
        }
    }

    private void updateMana(){
        if(currentMana > maxMana) currentMana = maxMana;
        if(currentMana < 0) currentMana = 0;
    }

    private void updateEffect(){
        if(!effect.isEmpty()){
            for(Effect e : effect) {
                e.update();
                if(e.effectFinished){
                    e.remove();
                    effectManager.remove(e.name);
                }
            }
        }
        effect.removeIf(e -> e.effectFinished);
    }

    private void checkForMana(){
        if(!hasResource() && isShooting){
            isShooting = false;
            dialogues[0][0] = new StringBuilder("Không đủ mana!\nBạn cần " + projectile.manaCost + " mana(s) để bắn");
            submitDialogue(this , 0);
            KeyHandler.enterPressed = false;
        }
    }

    private void healMana(){
        manaHealCounter++;
        if(manaHealCounter >= manaHealInterval){
            manaHealCounter = 0;
            currentMana += 20;
        }
        updateMana();
    }

    public boolean hasResource(){
        return currentMana >= projectile.manaCost;
    }

    public void set(){
        setDamage();
        setDefense();
        setMaxHP();
        setMaxMana();
    }

    public void kill(){
        currentHP = 0;
    }

    private void setDamage(){
        strength = 10;
        lastStrength = strength;
        damage = projectile.baseDamage + strength * level ;
    }
    private void setDefense(){
        defense = level * 10;
    }
    private void setMaxHP(){
        maxHP = 150 + (level - 1) * 40;
        currentHP = maxHP;
    }
    private void setMaxMana(){
        maxMana = 100 + (level - 1) * 15;
        currentMana = maxMana;
    }
    public void checkForLevelUp(){
        if(exp >= nextLevelUp)
        {
            level++;
            set();
            exp = nextLevelUp;
            if(level == 1) nextLevelUp = 30; else
            if(level == 2) nextLevelUp = 150; else
            if(level == 3) nextLevelUp = 300; else
            if(level == 4) nextLevelUp = 700; else
            if(level == 5) nextLevelUp = 999999999;
            playSE(3);
            dialogues[0][0] = new StringBuilder("Lên cấp!\nBạn lên cấp " + level + "\nChỉ số của bạn đều được tăng!");
            submitDialogue(this , 0);
        }
    }

    @Override
    public void update()
    {
        if(isAutoMoving){
            autoMoving();
        } else {
            keyInput();
        }
        handlePosition();
        handleStatus();
        changeAnimationDirection();
        updateInventory();
        updateHP();
        healMana();
        updateEffect();
        handleAnimation();
        currentAnimation.update();
    }


    @Override
    public void render(Graphics2D g2)
    {
        if(isInvincible && !isDying){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 0.6f));
        }
        super.render(g2);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1.0f));
        int positionY = (int)position.y - camera.getY() + 20;
        if(!effect.isEmpty()){
            for(int i = 0 ; i < effect.size() ; i++){
                int positionX = (int)position.x - camera.getX() + 35 + 20 * i;
                g2.drawImage(effect.get(i).icon , positionX , positionY , null);
            }
        }
    }

    public void setPosition(int x , int y){
        position = new Vector2D(x , y);
        newPosition = new Vector2D(x, y);
    }

    public void setConfusedMode(boolean confused){
        this.confused = confused;
    }

    public void toggleConfusedMode(){
        this.confused = !this.confused;
    }

    public void setGoal(int goalX, int goalY){
        isAutoMoving = true;
        isRunning = true;
        this.goalCol = (goalX + solidArea1.x) / childNodeSize;
        this.goalRow = (goalY + solidArea1.y) / childNodeSize;
    }

    public EnvironmentManager getEnvironmentManager(){
        return mp.getEnvironmentManager();
    }

}


