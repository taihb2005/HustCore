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

public class Obj_Coin extends Entity {
    GameMap mp;
    private boolean isIdle;
    private int currentFrames = 0;
    final private int IDLE = 0;
    final private int TOUCHED = 1;

    private int CURRENT_ACTION;
    private int PREVIOUS_ACTION;
    final private BufferedImage[][] obj_coin = new BufferedImage[2][];
    final private Animation obj_animator_coin = new Animation();

    int coin = 1;

    public Obj_Coin(GameMap mp) {
        super();
        this.mp = mp;
        name = "Coin";
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
        obj_coin[IDLE] = new Sprite("/entity/object/coinNormal.png", width, height).getSpriteArrayRow(0);
        obj_coin[TOUCHED] = new Sprite("/entity/object/coinTouched.png", width, height).getSpriteArrayRow(0);
        obj_animator_coin.setAnimationState(obj_coin[IDLE] , 7);
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
            if(isInteracting) obj_animator_coin.setAnimationState(obj_coin[TOUCHED], 7 );
            if(isIdle)        obj_animator_coin.setAnimationState(obj_coin[IDLE] , 7 , false);
        }
        isInteracting = false;
    }

    private void collect() {
        dialogues[0] = "Bạn đã nhận được " + coin + " xu!";
        GamePanel.gameState = GameState.DIALOGUE_STATE;
        startDialogue(this);
    }

    @Override
    public void update() {
        // Chỉ cập nhật khi chưa thu thập
        handleAnimationState();
        obj_animator_coin.update();
        currentFrames = obj_animator_coin.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) {// Chỉ vẽ khi chưa thu thập
        g2.drawImage(obj_coin[CURRENT_ACTION][currentFrames],
                worldX - camera.getX() ,
                worldY - camera.getY() ,
                width, height, null);
    }

}
