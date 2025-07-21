package org.kat.app.entity.effect.type;

import org.kat.app.entity.effect.Effect;
import org.kat.app.entity.player.Player;

public class EffectNone extends Effect {
    public EffectNone(Player player) {
        super(player);
        id = 0;
        name = "Effect None";
        isNegative = false;
    }
    
    public Effect clone(){
        return this;
    }
}
