package entity.effect.type;

import entity.effect.Effect;
import entity.player.Player;
import graphics.AssetPool;

public class Strength extends Effect {
    public Strength(Player player , int duration) {
        super(player);
        setEffectDuration(duration);
        id = 4;
        name = "Strength";
        icon = AssetPool.getImage("strength.png");
    }

    public void affect(){
        player.strength = player.lastStrength * 3;
    }

    public void remove(){
        player.strength = player.lastStrength;
    }

    public Strength clone(){
        return new Strength(player, effectDuration);
    }

}
