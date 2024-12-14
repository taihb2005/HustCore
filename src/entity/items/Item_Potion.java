package entity.items;

import entity.Entity;
import entity.effect.Effect;
import entity.player.Player;
import graphics.Sprite;
import map.GameMap;

import java.awt.image.BufferedImage;

public class Item_Potion extends Item{
    public Item_Potion() {
        super(3 , new Sprite("/entity/object/ITEM_potion.png" , 32 , 32).getSpriteSheet());
        name = new StringBuilder("Thuốc giải");
        description = new StringBuilder("Sử dụng để hóa giải tất cả\nhiệu ứng trên người");
    }
    public void use(Player player){
        if(!player.effect.isEmpty()){
            quantity--;
            for(Effect e : player.effect) e.remove();
            player.effect.clear();
            player.effectManager.clear();
        }
    }
}
