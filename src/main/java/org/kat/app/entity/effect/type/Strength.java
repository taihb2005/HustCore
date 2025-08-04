package org.kat.app.entity.effect.type;

import org.kat.app.entity.effect.Effect;
import org.kat.app.entity.player.Player;
import org.kat.app.graphics.AssetPool;

public class Strength extends Effect {
    private int damageMultiplier = 2;
    public Strength(Player player , int duration, int damageMultiplier) {
        super(player);
        setEffectDuration(duration);
        id = 4;
        name = "Strength";
        isNegative = false;
        icon = AssetPool.getImage("strength.png");

        this.damageMultiplier = damageMultiplier;
    }

    public void affect(){
        player.strengthScalar =  damageMultiplier;
    }

    public void remove(){
        player.strengthScalar = player.defaultStrengthScalar;
    }

    public Strength clone(){
        return new Strength(player, effectDuration, damageMultiplier);
    }

}
