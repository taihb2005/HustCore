package entity.items;

import entity.Item;
import graphics.Sprite;
import main.GamePanel;
import main.GameState;

public class Item_Kit extends Item {
    public Item_Kit(){
        super(2, "Kit", new Sprite("/entity/object/ITEM_box.png", 32, 32).getSprite(0,0));
    }
    public void collect() {
//        dialogues[0] = "Bạn đã nhận được 1 bộ cứu thương!";
//        GamePanel.gameState = GameState.DIALOGUE_STATE;
//        startDialogue(this);
    }
}
