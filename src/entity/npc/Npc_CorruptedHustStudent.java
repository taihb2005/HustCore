package entity.npc;

import entity.Actable;
import entity.Entity;
import graphics.Animation;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Currency;
import java.util.Random;

import static main.GamePanel.camera;

public class Npc_CorruptedHustStudent extends Entity implements Actable {
    GameMap mp;

    final int IDLE_TYPE1 = 1;
    final int IDLE_TYPE2 = 2;
    final int TALK = 3;

    final int RIGHT = 0;
    final int LEFT = 1;

    private int PREVIOUS_ACTION;
    private int CURRENT_ACTION;
    private int CURRENT_DIRECTION;

    private final BufferedImage[][][] npc_corruptedStudent_sprite = new BufferedImage[4][][];
    private final BufferedImage[][][] npc_corruptedStudentGlowing_sprite = new BufferedImage[4][][];
    private BufferedImage[][][] currentSprite;
    private final Animation npc_animator_corruptedStudent;

    private boolean isTalking;
    private boolean isIdling;
    private int CURRENT_FRAME;

    private final int DESIRED_RNG = 50;

    private int randomNumerFrames = 500;
    private int frameRandom = 0;
    private int rng = 0;
    private final Random generator = new Random();

    public Rectangle interactionDetectionArea;

    public Npc_CorruptedHustStudent(GameMap mp)
    {
        super();
        this.mp = mp;
        super.width = 64;
        super.height = 64;

        npc_animator_corruptedStudent = new Animation();

        getNpcImage();
        setDefault();

    }

    private void getNpcImage()
    {
        npc_corruptedStudent_sprite[IDLE_TYPE1] = new Sprite("/entity/npc/npc_corruptedstudent_idle1.png" , width , height).getSpriteArray();
        npc_corruptedStudent_sprite[IDLE_TYPE2] = new Sprite("/entity/npc/npc_corruptedstudent_idle2.png" , width , height).getSpriteArray();
        npc_corruptedStudent_sprite[TALK]       = new Sprite("/entity/npc/npc_corruptedstudent_talk.png"  , width , height).getSpriteArray();

        npc_corruptedStudentGlowing_sprite[IDLE_TYPE1] = new Sprite("/entity/npc/npc_corruptedstudent_glowing_idle1.png" , width , height).getSpriteArray();
        npc_corruptedStudentGlowing_sprite[IDLE_TYPE2] = new Sprite("/entity/npc/npc_corruptedstudent_glowing_idle2.png" , width , height).getSpriteArray();
        npc_corruptedStudentGlowing_sprite[TALK] = new Sprite("/entity/npc/npc_corruptedstudent_glowing_talk.png" , width , height).getSpriteArray();
    }

    private void setDefault()
    {
        CURRENT_DIRECTION = RIGHT;

        CURRENT_ACTION = IDLE_TYPE1;
        PREVIOUS_ACTION = IDLE_TYPE1;

        CURRENT_FRAME = 0;
        currentSprite = npc_corruptedStudent_sprite;
        npc_animator_corruptedStudent.setAnimationState(currentSprite[IDLE_TYPE1][CURRENT_DIRECTION] , 5 );

        solidArea1 = new Rectangle(20 , 40 , 30 , 14);
        solidArea2 = new Rectangle(27 , 54 , 18 , 7);
        super.setDefaultSolidArea();

        interactionDetectionArea = new Rectangle(8 , 11 , 56 , 56);
    }

    @Override
    public void setDialogue() {

    }

    private void handleRNG(){
        frameRandom++;
        if(frameRandom >= randomNumerFrames)
        {
            rng = generator.nextInt(100) + 1;
            frameRandom = 0;
        } else rng = 0;

        if(rng >= DESIRED_RNG)
        {
            isIdling = true;
            npc_animator_corruptedStudent.playOnce();
        }
    }

    private void changeEffect()
    {
        if(collisionOn) currentSprite = npc_corruptedStudentGlowing_sprite; else
            currentSprite = npc_corruptedStudent_sprite;
    }

    private void handleAnimationState() {
        if (isIdling && npc_animator_corruptedStudent.isPlaying()) {
            CURRENT_ACTION = IDLE_TYPE2;
        } else {
            CURRENT_ACTION = IDLE_TYPE1;
            PREVIOUS_ACTION = IDLE_TYPE1;
            npc_animator_corruptedStudent.setAnimationState(currentSprite[IDLE_TYPE1][CURRENT_ACTION] , 5);
        }

        if (PREVIOUS_ACTION != CURRENT_ACTION) {
            PREVIOUS_ACTION = CURRENT_ACTION;
            if (isIdling) {
                npc_animator_corruptedStudent.setAnimationState(currentSprite[IDLE_TYPE2][CURRENT_DIRECTION], 14);
            }
        }

        if (!npc_animator_corruptedStudent.isPlaying()) isIdling = false;

    }
    @Override
    public void move() {
    }

    @Override
    public void set() {

    }

    @Override
    public void talk() {

    }

    @Override
    public void loot() {

    }

    @Override
    public void pathFinding() {

    }

    @Override
    public void update() {
        changeEffect();
        handleRNG();
        handleAnimationState();
        npc_animator_corruptedStudent.update();
        CURRENT_FRAME = npc_animator_corruptedStudent.getCurrentFrames();
        collisionOn = false;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(currentSprite[CURRENT_ACTION][CURRENT_DIRECTION][CURRENT_FRAME] , worldX - camera.getX(), worldY - camera.getY(), null);
    }

    @Override
    public void dispose() {

    }
}
