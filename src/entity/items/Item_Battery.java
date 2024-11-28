package entity.items;

import entity.Entity;
import entity.player.Player;
import graphics.Sprite;
import main.GamePanel;
import main.GameState;
import map.GameMap;

public class Item_Battery extends Item {
    private final int manaReward = 50;
    public Item_Battery(){
        super(1, new Sprite("/entity/object/ITEM_battery.png", 32, 32).getSprite(0,0));
        name = "Pin năng lương";
        description = "Sử dụng để hồi năng lượng";
        dialogues[0][0] = "Bạn được hồi " + manaReward + " năng lượng!";
    }

    public void use(Player player){
        quantity--;
        player.currentMana += manaReward;
        startDialogue(this , 0);
    }
}
