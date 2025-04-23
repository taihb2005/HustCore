package entity.effect.type;

import entity.effect.Effect;
import entity.player.Player;

public class EffectImmunity extends Effect {
    public EffectImmunity(Player player, int duration){
        super(player);
        setEffectDuration(duration);
        id = 5;
        name = "Effect Immunity";
    }

    public void affect(){

    }

    public void remove(){

    }

    public EffectImmunity clone(){
        return new EffectImmunity(player, effectDuration);
    }
}
