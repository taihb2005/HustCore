package entity.object;

import entity.Actable;
import entity.Entity;
import graphics.Animation;
import graphics.Sprite;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_Door extends Entity implements Actable {
    public static final int SMALL_DOOR = 0;
    public static final int BIG_DOOR = 1;

    private final BufferedImage[][] obj_door_sprite;
    private BufferedImage obj_door_effect;
    private final Animation obj_door;

    private int currentFrames;
    private int prev_state;
    private int state; // 0 for closing door , 1 for opened door
    public boolean isOpened;
    public int type;
    public int id;

    public Obj_Door(int type , int id) {
        super();
        name = "Door";
        this.type = type;
        this.id = id;
        this.state = 0;
        this.prev_state = 0;
        this.isOpening = false;
        this.canbeDestroyed = false;
        isOpened = false;
        obj_door_sprite = new BufferedImage[2][];
        obj_door = new Animation();

        getDoorImage();
        setDefault();
    }

    public Obj_Door(int type , int id , int state) {
        super();
        name = "Door";
        this.type = type;
        this.id = id;
        this.state = state;
        this.prev_state = state;
        this.canbeDestroyed = false;
        this.isOpening = false;
        isOpened = state != 0;
        obj_door_sprite = new BufferedImage[2][];
        obj_door = new Animation();

        getDoorImage();
        setDefault();
    }

    public Obj_Door(int type , int id , int state , int x , int y) {
        super(x , y);
        name = "Door";
        this.type = type;
        this.id = id;
        this.state = state;
        this.prev_state = state;
        this.isOpening = false;
        this.canbeDestroyed = false;
        isOpened = false;
        obj_door_sprite = new BufferedImage[2][];
        obj_door = new Animation();

        getDoorImage();
        setDefault();
    }

    private void getDoorImage()
    {
        switch(this.type)
        {
            case SMALL_DOOR:
                super.width = 64;
                super.height = 64;
                obj_door_sprite[0] = new Sprite("/entity/object/door_small_active_id" + id + ".png" , width , height).getSpriteArrayRow(0);
                obj_door_sprite[1] = new Sprite("/entity/object/door_small_opening_id" + id + ".png" , width  ,height).getSpriteArrayRow(0);
                obj_door_effect    = new Sprite("/entity/object/door_small_glowingeffect.png" , width , height).getSprite(0 , 0);
                break;
            case BIG_DOOR:
                super.width = 128;
                super.height = 64;
                obj_door_sprite[0] = new Sprite("/entity/object/door_big_active_id" + id + ".png" , width , height).getSpriteArrayRow(0);
                obj_door_sprite[1] = new Sprite("/entity/object/door_big_opening_id" + id + ".png" , width , height).getSpriteArrayRow(0);
                obj_door_effect    = new Sprite("/entity/object/door_big_glowingeffect.png" , width , height).getSprite(0 , 0);
                break;
        }
    }

    private void setDefault()
    {
        currentFrames = 0;
        isOpening = false;
        switch(type) {
            case SMALL_DOOR:
                solidArea1 = new Rectangle(0, 18, 65, 44);
                solidArea2 = new Rectangle(0, 0, 0, 0);
                interactionDetectionArea = new Rectangle(0, 13, 68, 55);
                super.setDefaultSolidArea();
                break;
            case BIG_DOOR:
                solidArea1 = new Rectangle(0 , 18 , 128 , 43);
                solidArea2 = new Rectangle(0, 0, 0, 0);
                interactionDetectionArea = new Rectangle(0, 0, 128, 64);
                super.setDefaultSolidArea();
                break;
        }

        obj_door.setAnimationState(obj_door_sprite[state] , 16);
        setDialogue();
    }

    public void move() {

    }

    public void set() {

    }

    public void setDialogue() {
        dialogues[0][0] = "Nó bị khóa!";
        dialogues[0][1] = "Bạn có muốn sử dụng một\n tấm thẻ để mở khóa không?";

        dialogues[1][0] = "Bạn không có chiếc thẻ nào!";
    }

    public void talk() {
        if(dialogues[dialogueIndex] == null)
        {
            dialogueIndex = 0;
        }
        startDialogue(this , 0);
    }

    @Override
    public void attack() {

    }

    public void loot() {

    }

    public void pathFinding() {

    }

    private void changeState()
    {
        isInteracting = false;
        if(isOpening) {
            state = 1;
        }

        if(prev_state != state)
        {
            prev_state = state;
            if(isOpening)
            {
                obj_door.setAnimationState(obj_door_sprite[state] , 17);
                obj_door.playOnce();
            }
        }

        if(!obj_door.isPlaying() && isOpening)
        {
            isOpening = false;
            canbeDestroyed = true;
        }
    }

    @Override
    public void update() {
        changeState();
        obj_door.update();
        currentFrames = obj_door.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(obj_door_sprite[state][currentFrames] , worldX - camera.getX() , worldY - camera.getY() , width , height , null);
        if(isInteracting && !isOpening){g2.drawImage(obj_door_effect , worldX - camera.getX() , worldY - camera.getY() , width , height , null);}
    }
}
