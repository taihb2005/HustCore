package org.kat.app.entity.object;

import org.kat.app.entity.Entity;
import org.kat.app.graphics.Animation;
import org.kat.app.graphics.AssetPool;
import org.kat.app.graphics.Sprite;
import org.kat.app.main.KeyHandler;
import org.kat.app.map.GameMap;

import java.awt.*;
import java.util.HashMap;

public class Obj_Heart extends Entity {
    private static final int HP_REWARD = 30;
    GameMap mp;

    private static final HashMap<HeartState, Sprite> heartSpritePool = new HashMap<>();
    private static final HashMap<HeartState, Animation> heartAnimation = new HashMap<>();

    public static void load(){
        for(HeartState state: HeartState.values()){
            heartSpritePool.put(state,
                    new Sprite(AssetPool.getImage("heart_" + state.name().toLowerCase() + ".png"), 32, 32));
            heartAnimation.put(state,
                    new Animation(heartSpritePool.get(state).getSpriteArrayRow(0), 7, true));
        }
    }

    private HeartState currentState;
    private HeartState lastState;

    private void setState(){
        if(lastState != currentState){
            lastState = currentState;
            currentAnimation = heartAnimation.get(currentState).clone();
        }

    }

    public Obj_Heart(GameMap mp) {
        super();
        this.mp = mp;
        name = "Heart";
        super.width = 32;
        super.height = 32;

        currentState = HeartState.IDLE;
        lastState = HeartState.IDLE;
        currentAnimation = heartAnimation.get(currentState).clone();
        isInteracting = false;
        setDefault();
    }


    private void setDefault()
    {
        solidArea1 = new Rectangle(0 , 0 , 0 , 0);
        hitbox = new Rectangle(9 , 12 , 14 , 12);
        interactionDetectionArea = new Rectangle(3 , 7 , 26 , 23);
        super.setDefaultSolidArea();

        setDialogueAt(0, 0, "Bạn đã được hồi " + HP_REWARD + " máu!");
        buildDialogue();
    }

    private void handleAnimation() {
        if (isInteracting) {
            currentState = HeartState.TOUCHED;
            if (KeyHandler.enterPressed) {
                collect();
                canbeDestroyed = true;
            }
        } else {
            currentState = HeartState.IDLE;
        }

        setState();

        isInteracting = false;
    }

    private void collect() {
        mp.player.currentHP += HP_REWARD;
        dialogueSet++;
        if(dialogues[dialogueSet][0] == null) {
            dialogueIndex = 0;
            dialogueSet--;
        }
        submitDialogue(dialogueSet);
    }

    @Override
    public void update(){
        handleAnimation();
        currentAnimation.update();
    }

    @Override
    public void render(Graphics2D g2){// Chỉ vẽ khi chưa thu thập
        super.render(g2);
    }

    @Override
    public void dispose(){
        super.dispose();
        mp = null;
    }

    private enum HeartState{
        IDLE, TOUCHED, ACTIVATED
    }
}
