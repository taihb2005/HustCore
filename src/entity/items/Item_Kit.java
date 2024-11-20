package entity.items;

import entity.Entity;
import graphics.Sprite;
import main.GamePanel;
import main.GameState;

public class Item_Kit extends Item {
    private final int hpReward = 60;
    public Item_Kit(){
        super(2, "Kit", new Sprite("/entity/object/ITEM_box.png", 32, 32).getSprite(0,0));
        name = "Bộ cứu thương";
        description = "Sử dụng để hồi máu";
        dialogues[0][0] = "Bạn được hồi " + hpReward + " máu!";
    }
    public void use(Entity entity){
        quantity--;
        entity.currentHP += hpReward;
        startDialogue(this , 0);
    }
}
