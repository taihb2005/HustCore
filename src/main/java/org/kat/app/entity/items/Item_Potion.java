package org.kat.app.entity.items;

import org.kat.app.entity.effect.Effect;
import org.kat.app.entity.effect.type.EffectImmunity;
import org.kat.app.entity.player.Player;
import org.kat.app.graphics.AssetPool;
import org.kat.app.graphics.Sprite;

public class Item_Potion extends Item{
    private static final Sprite potionSprite = new Sprite(AssetPool.getImage("ITEM_potion.png"), 32, 32);
    public Item_Potion() {
        super(3 , potionSprite.getSpriteSheet());
        name = new StringBuilder("Thuốc giải");
        description = new StringBuilder("Sử dụng để hóa giải tất cả\nhiệu ứng trên người");
    }
    public void use(Player player){
        quantity--;
        for(Effect e : player.effect) e.remove();
        player.effect.clear();
        player.effectManager.clear();

        player.getEnvironmentManager().lighting.transit = true;
        player.getEnvironmentManager().lighting.fadeOut = true;

        EffectImmunity immunity = new EffectImmunity(player, 600);
        immunity.add();
    }
}
