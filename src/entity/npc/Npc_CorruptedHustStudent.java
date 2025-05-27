package entity.npc;

import entity.Actable;
import entity.Entity;
import entity.mob.Direction;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import map.GameMap;
import util.GameTimer;
import util.KeyPair;

import java.awt.*;
import java.util.*;

import static main.GamePanel.camera;

public class Npc_CorruptedHustStudent extends Entity implements Actable {
    GameMap mp;

    private static final HashMap<KeyPair<StudentState, Direction>, Sprite> studentSpritePool = new HashMap<>();
    private static final HashMap<KeyPair<StudentState, Direction>, Sprite> studentEffectsSpritePool = new HashMap<>();
    private static final HashMap<KeyPair<StudentState, Direction>, Animation> studentAnimations = new HashMap<>();
    private static final HashMap<KeyPair<StudentState, Direction>, Animation> studentEffectsAnimations = new HashMap<>();

    public static void load(){
        for(StudentState state: StudentState.values()){
            int speed = switch (state){
                case IDLE1 -> 5;
                case IDLE2 -> 14;
                case TALK -> 30;
            };

            boolean loop = switch (state){
                case IDLE1, TALK -> true;
                case IDLE2 -> false;
            };

            for(Direction direction: Direction.values()) {
                int row = switch (direction){
                    case RIGHT -> 0;
                    case LEFT -> 1;
                };
                KeyPair<StudentState, Direction> key = new KeyPair<>(state, direction);

                studentSpritePool.put(key,
                        new Sprite(AssetPool.getImage("npc_corruptedstudent_" + state.name().toLowerCase() + ".png")));
                studentEffectsSpritePool.put(key,
                        new Sprite(AssetPool.getImage("npc_corruptedstudent_glowing_" + state.name().toLowerCase() + ".png")));
                studentAnimations.put(key,
                        new Animation(studentSpritePool.get(key).getSpriteArrayRow(row), speed, loop));
                studentEffectsAnimations.put(key,
                        new Animation(studentEffectsSpritePool.get(key).getSpriteArrayRow(row), speed, loop));
            }
        }
    }
    private boolean isIdle1;
    private boolean isIdle2;

    private StudentState currentState;
    private StudentState lastState;
    private Direction currentDirection;
    private Direction lastDirection;

    private Animation currentEffectAnimation;

    private GameTimer changeIdleTypeTimer;

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
            currentAnimation = studentAnimations.get(new KeyPair<>(currentState, currentDirection)).clone();
            if(isInteracting){
                currentEffectAnimation = studentEffectsAnimations.get(new KeyPair<>(currentState, currentDirection)).clone();
            }
        }
    }

    private boolean talkOnce;
    private boolean isTalking;

    //NPC RNG
    private final int DESIRED_RNG = 50;
    private int rng = 0;
    private final Random generator = new Random();

    public Npc_CorruptedHustStudent(GameMap mp ,String name ,String direction , StringBuilder[][] dialogue , int x , int y)
    {
        super(x , y);
        this.mp = mp;
        this.idName = name;
        super.width = 64;
        super.height = 64;

        for(int i = 0 ; i < dialogue.length ;i++){
            System.arraycopy(dialogue[i], 0, this.dialogues[i], 0, dialogue[i].length);
        }

        setDefault();
    }

    private void setDefault()
    {
        direction = "right";
        currentDirection = Direction.RIGHT;
        lastDirection = Direction.RIGHT;

        currentState = StudentState.IDLE1;
        lastState = StudentState.IDLE1;

        isIdle1 = true;
        currentAnimation = studentAnimations.get(new KeyPair<>(currentState, currentDirection)).clone();
        currentEffectAnimation = studentEffectsAnimations.get(new KeyPair<>(currentState, currentDirection)).clone();

        solidArea1 = new Rectangle(20 , 40 , 30 , 14);
        solidArea2 = new Rectangle(27 , 54 , 18 , 7);
        interactionDetectionArea = new Rectangle(5 , 41 , 58 , 12);
        setDefaultSolidArea();

        talkOnce = false;
        dialogueIndex = 0;
        dialogueSet = -1;
    }

    private void setAction(){
        if(changeIdleTypeTimer == null){
            changeIdleTypeTimer = new GameTimer( ()-> {
                rng = generator.nextInt(100) + 1;
                isIdle2 = (rng >= DESIRED_RNG);
                isIdle1 = !isIdle2;
            },500);
        } else {
            changeIdleTypeTimer.update();
        }
    }

    private void handleAnimation() {
        if(isIdle1){
            currentState = StudentState.IDLE1;
        } else if(isIdle2){
            currentState = StudentState.IDLE2;
        } else if(isTalking){
            currentState = StudentState.TALK;
        }

        setState();

        if(currentAnimation.isFinished() && isIdle2){
            isIdle2 = false;
            isIdle1 = true;
        }
    }

    private void changeDirection()
    {
        switch (direction){
            case "left" : currentDirection = Direction.LEFT; break;
            case "right": currentDirection = Direction.RIGHT; break;
        }
    }

    private void facePlayer()
    {
        switch(mp.player.direction)
        {
            case "right" : direction = "left"; break;
            case "left"  : direction = "right"; break;
        }
    }

    @Override
    public void move() {

    }

    @Override
    public void set() {

    }

    @Override
    public void talk() {
        talkOnce = true;
        facePlayer();
        dialogueSet++;
        isTalking = true;
        if(dialogues[dialogueSet][0] == null) {
            dialogueIndex = 0;
            dialogueSet--;
        }
        submitDialogue(this , dialogueSet);
    }

    @Override
    public void attack() {

    }

    @Override
    public void loot() {

    }

    @Override
    public void update(){
        setAction();
        changeDirection();
        handleAnimation();
        currentAnimation.update();
        if(isInteracting) currentEffectAnimation.update();
        isInteracting = false;
    }

    @Override
    public void render(Graphics2D g2) {
        super.render(g2);
        if(isInteracting){
            currentEffectAnimation.render(g2, (int)position.x - camera.getX(), (int)position.y - camera.getY());
        }
    }

    public boolean hasTalkYet(){return talkOnce;}

    private enum StudentState{
        IDLE1, IDLE2, TALK
    }
}

