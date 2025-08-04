package org.kat.app.entity.items;

import org.kat.app.entity.player.Player;
import org.kat.app.graphics.AssetPool;
import org.kat.app.graphics.Sprite;

public class Item_Kit extends Item {
    private static final Sprite kitSprite = new Sprite(AssetPool.getImage("ITEM_box.png"),32, 32);
    private final static int HP_REWARD = 60;
    public Item_Kit(){
        super(2, kitSprite.getSprite(0,0));
        name = new StringBuilder("Bộ cứu thương");
        description = new StringBuilder("Sử dụng để hồi máu");
        setDialogueAt(0, 0 ,
                "Bạn được hồi " + HP_REWARD + " máu!");
        buildDialogue();
    }
    public void use(Player player){
        quantity--;
        player.currentHP += HP_REWARD;
        submitDialogue(0);
    }
}
