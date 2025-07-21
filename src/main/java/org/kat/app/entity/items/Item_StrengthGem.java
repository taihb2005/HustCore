package org.kat.app.entity.items;

import org.kat.app.entity.effect.type.Strength;
import org.kat.app.entity.player.Player;
import org.kat.app.graphics.AssetPool;
import org.kat.app.graphics.Sprite;

public class Item_StrengthGem extends Item{
    private static final Sprite strengthGemSprite = new Sprite(AssetPool.getImage("ITEM_strengthgem.png"),32, 32);
    public Item_StrengthGem() {
        super( 4, strengthGemSprite.getSpriteSheet());
        name = new StringBuilder("Ngọc sức mạnh");
        description = new StringBuilder("Sử dụng để tăng sức mạnh");
    }

    public void use(Player player){
        quantity--;
        Strength strength = new Strength(player, 300);
        strength.add();
    }
}
