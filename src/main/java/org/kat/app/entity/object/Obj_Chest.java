package org.kat.app.entity.object;

import org.kat.app.entity.Actable;
import org.kat.app.entity.Entity;
import org.kat.app.entity.items.Item;
import org.kat.app.graphics.Animation;
import org.kat.app.graphics.AssetPool;
import org.kat.app.graphics.Sprite;
import org.kat.app.level.LevelState;
import org.kat.app.main.KeyHandler;
import org.kat.app.map.GameMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.kat.app.main.GamePanel.currentLevel;

public class Obj_Chest extends Entity implements Actable {
    GameMap mp;

    private static final HashMap<ChestState, Sprite> chestSpritePool = new HashMap<>();
    private static final HashMap<ChestState, Animation> chestAnimation = new HashMap<>();
    public static void load(){
        for(ChestState state: ChestState.values()){
            chestSpritePool.put(state,
                    new Sprite(AssetPool.getImage("chest_" + state.name().toLowerCase() + ".png")));
            chestAnimation.put(state,
                    new Animation(chestSpritePool.get(state).getSpriteArrayRow(0), 8, true));
        }
    }

    private void setState(){
        if(currentState != lastState){
            lastState = currentState;
        }

        currentAnimation.reset();
        currentAnimation = chestAnimation.get(currentState).clone();
        currentAnimation.reset();
    }
    private ChestState currentState;
    private ChestState lastState;
    public ArrayList<Item> reward = new ArrayList<>();

    public Obj_Chest(GameMap mp, String idName, int x , int y)
    {
        super(x , y);
        name = "Chest";
        this.idName = idName;
        this.mp = mp;
        super.width = 64;
        super.height = 64;

        currentState = ChestState.CLOSED;
        currentAnimation = chestAnimation.get(currentState).clone();

        setDefault();
    }



    private void setDefault()
    {
        solidArea1 = new Rectangle(16 , 43 , 32 , 17);
        hitbox = new Rectangle(0 , 0 , 0 , 0);
        interactionDetectionArea = new Rectangle(17 , 64 , 29, 5);
        super.setDefaultSolidArea();

        dialogueSet = -1;

    }

    public void setDialogue() {
        HashMap<Item , Integer> map = new HashMap<>();
        for (Item value : reward) {
            if (!map.containsKey(value)) {
                map.put(value, 1);
            } else {
                int tmp = map.get(value);
                map.put(value, ++tmp);
            }
        }
        int dialogueIndex = 0;
        for(var item : map.entrySet()){
            setDialogueAt(0, dialogueIndex,
                    "Bạn nhận được " + item.getKey().getName() + " x" + item.getValue()
                            + item.getKey().getDescription());
            dialogueIndex++;
        }

        setDialogueAt(1, 0 , "Nó đã được mở rồi");
        buildDialogue();
    }

    public void talk(){
        dialogueSet++;
        if(dialogues[dialogueSet][0] == null){
            dialogueSet--;
        }
        submitDialogue(dialogueSet);
    }

    public void loot() {
        for (Item item : reward) {
            item.add(mp.player.inventory);
        }
    }

    public void setLoot(Item item , int quantity){
        for(int i = 0 ; i < quantity ; i++) reward.add(item);
    }

    public void handleAnimation() {
        if (isInteracting) {
            if (currentLevel.checkState(LevelState.RUNNING) && KeyHandler.enterPressed) {
                KeyHandler.enterPressed = false;
                talk();
                if(currentState == ChestState.CLOSED) {
                    currentState = ChestState.OPENED;
                    loot();
                }
            }
        }
        setState();
        isInteracting = false;
    }

    @Override
    public void update(){
        handleAnimation();
        currentAnimation.update();
    }

    @Override
    public void render(Graphics2D g2){
        super.render(g2);
    }

    @Override
    public void attack() {

    }

    @Override
    public void move() {

    }

    public boolean isOpened(){
        return currentState == ChestState.OPENED;
    }

    private enum ChestState{
        OPENED, CLOSED
    }
}
