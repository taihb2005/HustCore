package entity.object;

import entity.Entity;
import graphics.Animation;
import graphics.Sprite;
import main.GamePanel;
import main.GameState;
import main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_Battery extends Entity {
    final private BufferedImage[] obj_battery;

    public Obj_Battery()
    {
        super();
        name = "Battery";
        super.width = 32;
        super.height = 32;
        isInteracting = false;
        obj_battery = new Sprite("/entity/object/ITEM_battery_wShadow.png", width , height).getSpriteArrayRow(0);

        setDefault();
    }

    private void setDefault()
    {
        solidArea1 = new Rectangle(0 , 9 , 32 , 25);
        hitbox = new Rectangle(9 , 12 , 14 , 12);
        interactionDetectionArea = new Rectangle(3 , 7 , 50 , 50);
        super.setDefaultSolidArea();
    }

    public void handleAnimationState() {
        if (isInteracting) {  // Kiểm tra nếu nhân vật đang tương tác với đối tượng
            if (KeyHandler.enterPressed) {// Đánh dấu là đã thu thập
                collect();
                canbeDestroyed = true;
            }
        }
        isInteracting = false;
    }

    private void collect() {
        dialogues[0] = "Bạn đã nhận được 1 pin!";
        GamePanel.gameState = GameState.DIALOGUE_STATE;
        startDialogue(this);
    }

    @Override
    public void update() {
        handleAnimationState();
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(obj_battery[0] , worldX - camera.getX(), worldY - camera.getY()
                , width , height , null);
    }

}
