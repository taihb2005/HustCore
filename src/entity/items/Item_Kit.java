package entity.items;

import entity.Entity;
import entity.player.Player;
import graphics.AssetPool;
import graphics.Sprite;
import main.GamePanel;
import main.GameState;
import map.GameMap;

public class Item_Kit extends Item {
    private static final Sprite kitSprite = new Sprite(AssetPool.getImage("ITEM_box.png"),32, 32);
    private final int hpReward = 60;
    public Item_Kit(){
        super(2, kitSprite.getSprite(0,0));
        name = new StringBuilder("Bộ cứu thương");
        description = new StringBuilder("Sử dụng để hồi máu");
        dialogues[0][0] = new StringBuilder("Bạn được hồi " + hpReward + " máu!");
    }
    public void use(Player player){
        quantity--;
        player.currentHP += hpReward;
        startDialogue(this , 0);
    }
}
