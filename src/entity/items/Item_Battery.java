package entity.items;

import entity.player.Player;
import graphics.AssetPool;
import graphics.Sprite;

public class Item_Battery extends Item {
    private static final Sprite batterySprite = new Sprite(AssetPool.getImage("ITEM_battery.png"),32 ,32);
    private final int manaReward = 50;
    public Item_Battery(){
        super(1, batterySprite.getSprite(0,0));
        name = new StringBuilder("Pin năng lượng");
        description = new StringBuilder("Sử dụng để hồi năng lượng");
        dialogues[0][0] = new StringBuilder("Bạn được hồi " + manaReward + " năng lượng!");
    }

    public void use(Player player){
        quantity--;
        player.currentMana += manaReward;
        submitDialogue(this , 0);
    }
}
