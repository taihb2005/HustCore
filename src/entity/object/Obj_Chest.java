package entity.object;

import entity.Entity;
import entity.items.Item_Battery;
import entity.items.Item_Kit;
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
    private Item_Battery battery;
    private Item_Kit kit;

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
        battery = new Item_Battery();
        battery.add(mp.item);
    }

    private void spawnBox() {
        kit = new Item_Kit();
        kit.add(mp.item);
    }

    public void handleAnimationState() {
        if (isInteracting) {  // Kiểm tra nếu nhân vật đang tương tác với đối tượng
            if (KeyHandler.enterPressed) {// Đánh dấu là đã thu thập
                currentStates = OPENED;
                loot();  // Gọi hàm thu thập để hiển thị phần thưởng
                battery.collect();
                kit.collect();
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
