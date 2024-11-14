package entity.object;

import entity.Entity;
import graphics.Animation;
import graphics.Sprite;
import main.GamePanel;
import main.KeyHandler;
import main.UI;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_Heart extends Entity {
    final private BufferedImage[][] obj_heart = new BufferedImage[3][];
    final private Animation obj_animator_heart[] = new Animation[3];
    private int currentFrames = 0;
    final private int IDLE = 0;
    final private int TOUCHED = 1;
    final private int ACTIVATED = 2;
    private int currentAction;
    int hpReward = 10;

    public Obj_Heart() {
        super();
        super.width = 32;
        super.height = 32;

        // Trạng thái 1: Bình thường
        obj_animator_heart[IDLE] = new Animation();
        obj_heart[IDLE] = new Sprite("/entity/object/heartNormal.png", width, height).getSpriteArrayRow(0);
        obj_animator_heart[IDLE].setAnimationState(obj_heart[IDLE], 9);

        // Trạng thái 2: Khi chạm vào
        obj_animator_heart[TOUCHED] = new Animation();
        obj_heart[TOUCHED] = new Sprite("/entity/object/heartTouched.png", width, height).getSpriteArrayRow(0);
        obj_animator_heart[TOUCHED].setAnimationState(obj_heart[TOUCHED], 9);

        // Trạng thái 3: Khi chạm vào và ấn enter
        obj_animator_heart[ACTIVATED] = new Animation();
        obj_heart[ACTIVATED] = new Sprite("/entity/object/heartActivated.png", width, height).getSpriteArrayRow(0);
        obj_animator_heart[ACTIVATED].setAnimationState(obj_heart[ACTIVATED], 9);

        currentAction = IDLE;
        setDefault();
    }

    private void setDefault()
    {
        solidArea1 = new Rectangle(20 , 19 , 26 , 15);
        hitbox = new Rectangle(20 , 8 , 27 , 32);
        interactionDetectionArea = new Rectangle(5 , 41 , 58 , 12);
        super.setDefaultSolidArea();
    }

    private void handleAnimationState() {
        if (!isCollected) {  // Chỉ xử lý khi trái tim chưa được thu thập
            if (isInteracting) {  // Kiểm tra nếu nhân vật đang tương tác với đối tượng
                currentAction = TOUCHED;
                if (currentAction == TOUCHED && KeyHandler.enterPressed) {
                    isCollected = true;  // Đánh dấu là đã thu thập
                    currentAction = ACTIVATED;
                    collect();  // Gọi hàm thu thập để hiển thị phần thưởng
                }
            } else {
                currentAction = IDLE; // Trạng thái mặc định khi không chạm vào
            }
        }
    }

    private void collect() {
        GamePanel.ui.drawRewardWindow(); // Vẽ cửa sổ nhận thưởng
        if (KeyHandler.enterPressed) {
        }
    }

    @Override
    public void update() {
        if (!isCollected) {  // Chỉ cập nhật khi chưa thu thập
            handleAnimationState();
            obj_animator_heart[currentAction].update();
            currentFrames = obj_animator_heart[currentAction].getCurrentFrames();
        }
    }

    @Override
    public void render(Graphics2D g2) {
        if (!isCollected) {  // Chỉ vẽ khi chưa thu thập
            g2.drawImage(obj_heart[currentAction][currentFrames],
                    worldX - camera.getX() + 17,
                    worldY - camera.getY() + 10,
                    width, height, null);
        }
    }

}
