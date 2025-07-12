package entity.items;

import entity.effect.Effect;
import entity.effect.type.EffectImmunity;
import entity.player.Player;
import graphics.AssetPool;
import graphics.Sprite;

public class Item_Potion extends Item{
    private static final Sprite potionSprite = new Sprite(AssetPool.getImage("ITEM_potion.png"), 32, 32);
    public Item_Potion() {
        super(3 , potionSprite.getSpriteSheet());
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
