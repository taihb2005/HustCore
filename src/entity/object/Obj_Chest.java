package entity.object;

import entity.Actable;
import entity.Entity;
import entity.items.Item;
import entity.items.Item_Battery;
import entity.items.Item_Kit;
import graphics.Animation;
import graphics.Sprite;
import main.KeyHandler;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import static main.GamePanel.camera;
import static main.GamePanel.gameState;

public class Obj_Chest extends Entity implements Actable {
    GameMap mp;
    public final static int CLOSED = 0;
    public final static int OPENED = 1;

    private final BufferedImage[][] obj_Chest;
    private final Animation obj_animator_Chest = new Animation();
    private int CURRENT_FRAME = 0;
    private int currentStates = CLOSED;
    public ArrayList<Item> reward = new ArrayList<>();
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

    public Obj_Chest(GameMap mp , int x , int y)
    {
        super(x , y);
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

        dialogueSet = -1;

    }

    public void setDialogue() {
        HashMap<Item , Integer> map = new HashMap<>();
        for (int i = 0; i < reward.size(); i++) {
            if(!map.containsKey(reward.get(i))){
                map.put(reward.get(i) , 1);
            } else{
                int tmp = map.get(reward.get(i));
                map.put(reward.get(i) , ++tmp);
            }
        }
        int dialogueIndex = 0;
        for(var item : map.entrySet()){
            dialogues[0][dialogueIndex] = "Bạn nhận được " + item.getKey().getName() + " x" + item.getValue() +
                            "\n" + item.getKey().getDescription();
            dialogueIndex++;
        }

        dialogues[1][0] = "Nó đã được mở rồi!";
    }

    public void talk(){
        dialogueSet++;
        if(dialogues[dialogueSet][0] == null){
            dialogueSet--;
        }
        startDialogue(this , dialogueSet);
    }

    public void loot() {
        for (Item item : reward) {
            item.add(mp.player.inventory);
        }
    }

    public void setLoot(Item item){
        reward.add(item);
    }

    public void setLoot(Item item , int quantity){
        for(int i = 0 ; i < quantity ; i++) reward.add(item);
    }

    public void handleAnimationState() {
        if (isInteracting) {  // Kiểm tra nếu nhân vật đang tương tác với đối tượng
            if (KeyHandler.enterPressed) {
                KeyHandler.enterPressed = false;
                talk();
                if(currentStates == CLOSED) {
                    currentStates = OPENED;
                    loot();  // Gọi hàm thu thập để hiển thị phần thưởng
                }
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

    @Override
    public void attack() {

    }

    @Override
    public void move() {

    }
}
