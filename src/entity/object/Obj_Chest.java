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
    final private BufferedImage[] obj_Chest;
    final private Animation obj_animator_Chest;
    private int currentFrames = 0;
    private Obj_Battery battery;
    private Obj_Box box;

    public Obj_Chest(GameMap mp)
    {
        super();
        name = "Chest";
        this.mp = mp;
        super.width = 64;
        super.height = 64;

        obj_animator_Chest = new Animation();
        obj_Chest = new Sprite("/entity/object/hpChest.png", width , height).getSpriteArrayRow(0);
        obj_animator_Chest.setAnimationState(obj_Chest, 9);

        setDefault();
    }

    private void setDefault()
    {
        solidArea1 = new Rectangle(0 , 9 , 32 , 25);
        hitbox = new Rectangle(9 , 12 , 50 , 50);
        interactionDetectionArea = new Rectangle(3 , 7 , 50 , 50);
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
                loot();  // Gọi hàm thu thập để hiển thị phần thưởng
                canbeDestroyed = true;
            }
        }
    }

    @Override
    public void update() {
        obj_animator_Chest.update();
        handleAnimationState();
        currentFrames = obj_animator_Chest.getCurrentFrames();

    }

    @Override
    public void render(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(obj_Chest[0] , worldX - camera.getX(), worldY - camera.getY()
                , width , height+1 , null);
    }

}
