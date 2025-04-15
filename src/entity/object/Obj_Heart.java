package entity.object;

import entity.Entity;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import main.KeyHandler;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static main.GamePanel.camera;

public class Obj_Heart extends Entity {
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
    private Animation currentAnimation;

    private void setState(){
        if(lastState != currentState){
            lastState = currentState;
            currentAnimation = heartAnimation.get(currentState).clone();
        }

    }

    int hpReward = 60;

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

        dialogues[0][0] = new StringBuilder("Bạn đã được hồi " + hpReward + " máu!");
    }

    private void handleAnimation() {
        // Chỉ xử lý khi trái tim chưa được thu thập
        if (isInteracting) {  // Kiểm tra nếu nhân vật đang tương tác với đối tượng
            currentState = HeartState.TOUCHED;
            if (KeyHandler.enterPressed) {// Đánh dấu là đã thu thập
                collect();  // Gọi hàm thu thập để hiển thị phần thưởng
                canbeDestroyed = true;
            }
        } else {
            currentState = HeartState.IDLE;
        }

        setState();

        isInteracting = false;
    }

    private void collect() {
        mp.player.currentHP += hpReward;
        dialogueSet++;
        if(dialogues[dialogueSet][0] == null) {
            dialogueIndex = 0;
            dialogueSet--;
        }
        startDialogue(this , dialogueSet);
    }

    @Override
    public void update() throws NullPointerException{
        handleAnimation();
        currentAnimation.update();
    }

    @Override
    public void render(Graphics2D g2){// Chỉ vẽ khi chưa thu thập
        currentAnimation.render(g2, worldX - camera.getX(), worldY - camera.getY());
    }

    private enum HeartState{
        IDLE, TOUCHED, ACTIVATED
    }
}
