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
        isNegative = false;
        icon = AssetPool.getImage("strength.png");
    }

    public void affect(){
        player.strengthScalar =  2;
    }

    public void remove(){
        player.strengthScalar = player.defaultStrengthScalar;
    }

    public Strength clone(){
        return new Strength(player, effectDuration);
    }

}
