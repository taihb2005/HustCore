package org.kat.app.entity.effect.type;

import org.kat.app.entity.effect.Effect;
import org.kat.app.entity.player.Player;

public class EffectImmunity extends Effect {
    public EffectImmunity(Player player, int duration){
        super(player);
        setEffectDuration(duration);
        id = 5;
        name = "Effect Immunity";
        isNegative = false;
    }

    public void affect(){

    }

    public void remove(){

    }

    public EffectImmunity clone(){
        return new EffectImmunity(player, effectDuration);
    }
}
