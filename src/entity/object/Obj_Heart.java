package entity.object;

import entity.Entity;
import graphics.Animation;
import graphics.Sprite;
import main.GamePanel;
import main.GameState;
import main.KeyHandler;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_Heart extends Entity {
    GameMap mp;
    private boolean isIdle;
    private int currentFrames = 0;
    final private int IDLE = 0;
    final private int TOUCHED = 1;

    private int CURRENT_ACTION;
    private int PREVIOUS_ACTION;
    final private BufferedImage[][] obj_heart = new BufferedImage[2][];
    final private Animation obj_animator_heart = new Animation();

    int hpReward = 10;

    public Obj_Heart(GameMap mp) {
        super();
        this.mp = mp;
        name = "Heart";
        super.width = 32;
        super.height = 32;

        CURRENT_ACTION = IDLE;
        PREVIOUS_ACTION = IDLE;
        isInteracting = false;
        isIdle = true;
        getImage();
        setDefault();
    }

    private void getImage(){
        obj_heart[IDLE] = new Sprite("/entity/object/heartNormal.png", width, height).getSpriteArrayRow(0);
        obj_heart[TOUCHED] = new Sprite("/entity/object/heartTouched.png", width, height).getSpriteArrayRow(0);
        obj_animator_heart.setAnimationState(obj_heart[IDLE] , 7);
    }

    private void setDefault()
    {
        solidArea1 = new Rectangle(0 , 0 , 0 , 0);
        hitbox = new Rectangle(9 , 12 , 14 , 12);
        interactionDetectionArea = new Rectangle(3 , 7 , 26 , 23);
        super.setDefaultSolidArea();
    }

    private void handleAnimationState() {
        // Chỉ xử lý khi trái tim chưa được thu thập
        if (isInteracting) {  // Kiểm tra nếu nhân vật đang tương tác với đối tượng
            CURRENT_ACTION = TOUCHED;
            isIdle = false;
            if (KeyHandler.enterPressed) {// Đánh dấu là đã thu thập
                collect();  // Gọi hàm thu thập để hiển thị phần thưởng
                canbeDestroyed = true;
            }
        } else {
            isIdle = true;
            CURRENT_ACTION = IDLE;
           // Trạng thái mặc định khi không chạm vào
        }

        if(PREVIOUS_ACTION != CURRENT_ACTION){
            PREVIOUS_ACTION = CURRENT_ACTION;
            if(isInteracting) obj_animator_heart.setAnimationState(obj_heart[TOUCHED], 7 );
            if(isIdle)        obj_animator_heart.setAnimationState(obj_heart[IDLE] , 7 , false);
        }
        isInteracting = false;
    }

    private void collect() {
        mp.player.currentHP += hpReward;
        dialogues[0] = "Bạn đã được hồi " + hpReward + " máu!";
        GamePanel.gameState = GameState.DIALOGUE_STATE;
        startDialogue(this);
    }

    @Override
    public void update() {
        // Chỉ cập nhật khi chưa thu thập
        handleAnimationState();
        obj_animator_heart.update();
        currentFrames = obj_animator_heart.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) {// Chỉ vẽ khi chưa thu thập
            g2.drawImage(obj_heart[CURRENT_ACTION][currentFrames],
                    worldX - camera.getX() ,
                    worldY - camera.getY() ,
                    width, height, null);
    }

}
