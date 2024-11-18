package entity.object;

import entity.Entity;
import graphics.Animation;
import graphics.Sprite;
import main.KeyHandler;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_Chest extends Entity {
    GameMap mp;
    public final static int CLOSED = 0;
    public final static int OPENED = 1;

    private final BufferedImage[][] obj_Chest;
    private final Animation obj_animator_Chest = new Animation();
    private int CURRENT_FRAME = 0;
    private int currentStates = CLOSED;
    private Obj_Battery battery;
    private Obj_Box box;

    public Obj_Chest(GameMap mp)
    {
        super();
        name = "Chest";
        this.mp = mp;
        super.width = 64;
        super.height = 64;

        obj_Chest = new Sprite("/entity/object/hpChest.png", width , height).getSpriteArray();
        obj_animator_Chest.setAnimationState(obj_Chest[currentStates] , 8);

        setDefault();
    }

    private void setDefault()
    {
        solidArea1 = new Rectangle(16 , 43 , 32 , 17);
        hitbox = new Rectangle(0 , 0 , 0 , 0);
        interactionDetectionArea = new Rectangle(17 , 64 , 29, 5);
        super.setDefaultSolidArea();

    }

    public void loot() {
        spawnBattery();
        spawnBox();
    }

    private void spawnBattery() {
        battery = new Obj_Battery();
        battery.worldX = worldX+10; battery.worldY = worldY+10;
        mp.addObject(battery , mp.activeObj);
    }

    private void spawnBox() {
        box = new Obj_Box();
        box.worldX = worldX+50; box.worldY = worldY+10;
        mp.addObject(box , mp.activeObj);
    }

    public void handleAnimationState() {
        if (isInteracting) {  // Kiểm tra nếu nhân vật đang tương tác với đối tượng
            if (KeyHandler.enterPressed) {// Đánh dấu là đã thu thập
                currentStates = OPENED;
                loot();  // Gọi hàm thu thập để hiển thị phần thưởng
            }
        }
        isInteracting = false;
    }

    @Override
    public void update() {
        handleAnimationState();
        obj_animator_Chest.update();
        CURRENT_FRAME = obj_animator_Chest.getCurrentFrames();
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(obj_Chest[currentStates][CURRENT_FRAME] , worldX - camera.getX(), worldY - camera.getY()
                 , null);
    }

}
