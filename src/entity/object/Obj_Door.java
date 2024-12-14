package entity.object;

import entity.Actable;
import entity.Entity;
import graphics.Animation;
import graphics.Sprite;
import main.KeyHandler;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

import static main.GamePanel.camera;

public class Obj_Door extends Entity{
    private final static int INACTIVE = 0;
    private final static int ACTIVE = 1;

    private final static int CLOSE = 0;
    private final static int OPEN = 1;
    private BufferedImage[][][] obj_door = new BufferedImage[2][2][];
    private final BufferedImage obj_door_effect;
    private Animation obj_animator_door;

    private String size;
    private String state;

    public int STATE;
    public int PREVIOUS_STATE;

    public Obj_Door(String size , String state , String idName , int x , int y) throws Exception{
        super(x  , y);
        this.name = "Door";
        this.idName = idName;
        obj_animator_door = new Animation();
        if((!size.equals("small") && !size.equals("big")) || (!state.equals("inactive") && !state.equals("active"))){
            throw new Exception("Kiểm tra lại xâu của kích thước với trạng thái của cái cửa trong file JSON mau!");
        } else {
            height = 64;
            width = (size.equals("big")) ? 128 : 64;

            obj_door[INACTIVE][CLOSE] = new Sprite("/entity/object/door_" + size + "_inactive.png" , width , height).getSpriteArrayRow(0);
            obj_door[ACTIVE][CLOSE] = new Sprite("/entity/object/door_" + size + "_active.png" , width , height).getSpriteArrayRow(0);
            obj_door[ACTIVE][OPEN] = new Sprite("/entity/object/door_" + size + "_opening.png" , width , height).getSpriteArrayRow(0);
            obj_door_effect = new Sprite("/entity/object/door_" + size + "_glowingeffect.png" , width , height).getSpriteSheet();
        }

        if(size.equals("big")){
            solidArea1 = new Rectangle(0 , 17 , 128 , 47);
            solidArea2 = new Rectangle(0 , 0 , 0 , 0);
            interactionDetectionArea = new Rectangle(0, 11, 128, 64);
            setDefaultSolidArea();
        } else {
            solidArea1 = new Rectangle(0 , 17 , 64 , 47);
            solidArea2 = new Rectangle(0 , 0 , 0 , 0);
            interactionDetectionArea = new Rectangle(0, 11, 64, 57);
            setDefaultSolidArea();
        }

        STATE = (state.equals("inactive")) ? INACTIVE : ACTIVE;
        PREVIOUS_STATE = STATE;
        CURRENT_ACTION = CLOSE;
        PREVIOUS_ACTION = CLOSE;
        obj_animator_door.setAnimationState(obj_door[STATE][CURRENT_ACTION] , 20);

        dialogueSet = -1;
        dialogueIndex = 0;
        setDialogue();
    }

    private void open(){
        if (KeyHandler.enterPressed) {
            KeyHandler.enterPressed = false;
            if(STATE == ACTIVE) isOpening = true; else talk();
        }
    }

    private void changeState(){
        if(PREVIOUS_STATE == INACTIVE){
            if(canChangeState) STATE = ACTIVE;
            if(PREVIOUS_STATE != STATE){
                PREVIOUS_STATE = STATE;
                obj_animator_door.setAnimationState(obj_door[ACTIVE][CLOSE] , 20);
                canChangeState = false;
            }
        }
    }

    private void handleAnimationState(){
        isInteracting = false;
        if(PREVIOUS_STATE == ACTIVE){
            if(isOpening) CURRENT_ACTION = OPEN;

            if(PREVIOUS_ACTION != CURRENT_ACTION){
                PREVIOUS_ACTION = CURRENT_ACTION;
                if(isOpening){
                    obj_animator_door.setAnimationState(obj_door[STATE][OPEN] , 17);
                    obj_animator_door.playOnce();
                }
            }

            if(!obj_animator_door.isPlaying() && isOpening) {
                isOpening = false;
                canbeDestroyed = true;
            }
        }
    }

    private void setDialogue(){
        dialogues[0][0] = new StringBuilder("Hmm... Nó đã bị khóa!");
    }

    public void talk(){
        dialogueSet++;
        if(dialogues[dialogueSet][0] == null) {
            dialogueIndex = 0;
            dialogueSet--;
        }
        startDialogue(this , dialogueSet);
    }


    @Override
    public void update() throws  NullPointerException{
        changeState();
        if(isInteracting) open();
        handleAnimationState();
        obj_animator_door.update();
        CURRENT_FRAME = obj_animator_door.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) throws ArrayIndexOutOfBoundsException , NullPointerException {
        g2.drawImage(obj_door[STATE][CURRENT_ACTION][CURRENT_FRAME] , worldX - camera.getX() , worldY - camera.getY() , width , height , null);
        if(isInteracting && !isOpening){g2.drawImage(obj_door_effect , worldX - camera.getX() , worldY - camera.getY() , width , height , null);}
    }
    public void dispose(){
        obj_animator_door.dispose();
        obj_animator_door = null;
        for(int i = 0 ; i < obj_door.length ; i++) {
            for (int j = 0; j < obj_door[i].length; j++) {
                if(obj_door[i][j] == null) continue;
                for(int k = 0 ; k < obj_door[i][j].length ;k++){
                    obj_door[i][j][k].flush();
                    obj_door[i][j][k] = null;
                }
                obj_door[i][j] = null;
            }
            obj_door[i] = null;
        }
        obj_door = null;
    }

}
