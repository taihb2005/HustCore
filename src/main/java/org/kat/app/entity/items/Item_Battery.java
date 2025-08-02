package org.kat.app.entity.items;

import org.kat.app.entity.player.Player;
import org.kat.app.graphics.AssetPool;
import org.kat.app.graphics.Sprite;

public class Item_Battery extends Item {
    private static final Sprite batterySprite = new Sprite(AssetPool.getImage("ITEM_battery.png"),32 ,32);
    private final int manaReward = 50;
    public Item_Battery(){
        super(1, batterySprite.getSprite(0,0));
        name = new StringBuilder("Pin năng lượng");
        description = new StringBuilder("Sử dụng để hồi năng lượng");
        setDialogueAt(0, 0 ,
                "Bạn được hồi " + manaReward + " năng lượng!");
        buildDialogue();
    }

    public void use(Player player){
        quantity--;
        player.currentMana += manaReward;
        submitDialogue(0);
    }
}
