package entity.items;

import entity.Item;
import graphics.Sprite;
import main.GamePanel;
import main.GameState;

public class Item_Battery extends Item {
    public Item_Battery(){
        super(1, "Battery", new Sprite("/entity/object/ITEM_battery.png", 32, 32).getSprite(0,0));
    }
    public void collect() {
//        dialogues[0] = "Bạn đã nhận được 1 pin!";
//        GamePanel.gameState = GameState.DIALOGUE_STATE;
//        startDialogue(this);
    }
}
