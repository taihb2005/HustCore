package entity.enemies;

import entity.Actable;
import entity.Entity;
import graphics.Animation;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Robot_Enemy extends Entity implements Actable {
    GameMap mp;
    
    //ENEMY STATS
    final int IDLE_TYPE1 = 1;
    final int IDLE_TYPE2 = 2;
    final int TALK = 3;

    //ENEMY IMAGE
    private final BufferedImage[][][] npc_corruptedStudent_sprite = new BufferedImage[4][][];
    private final BufferedImage[][][] npc_corruptedStudentGlowing_sprite = new BufferedImage[4][][];
    private BufferedImage[][][] currentSprite;

    final int RIGHT = 0;
    final int LEFT = 1;

    private int CURRENT_FRAME;
    private int PREVIOUS_ACTION;
    private int CURRENT_ACTION;
    private int CURRENT_DIRECTION;

    private void getImage()
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
        direction = "right";

        CURRENT_ACTION = IDLE_TYPE1;
        PREVIOUS_ACTION = IDLE_TYPE1;

        CURRENT_FRAME = 0;
        currentSprite = npc_corruptedStudent_sprite;

        solidArea1 = new Rectangle(20 , 40 , 30 , 14);
        solidArea2 = new Rectangle(27 , 54 , 18 , 7);
        interactionDetectionArea = new Rectangle(5 , 41 , 58 , 12);
        super.setDefaultSolidArea();

        dialogueIndex = 0;
    }

    @Override
    public void move() {

    }

    @Override
    public void set() {

    }

    @Override
    public void setDialogue() {

    }

    @Override
    public void loot() {

    }

    @Override
    public void pathFinding() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(currentSprite[CURRENT_ACTION][CURRENT_DIRECTION][CURRENT_FRAME] , worldX - camera.getX(), worldY - camera.getY(), null);
    }
}
