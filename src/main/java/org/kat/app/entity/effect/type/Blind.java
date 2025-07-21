package org.kat.app.entity.effect.type;

import org.kat.app.entity.effect.Effect;
import org.kat.app.entity.player.Player;
import org.kat.app.graphics.AssetPool;

public class Blind extends Effect {
    public Blind(Player player , int duration) {
        super(player);
        setEffectDuration(duration);
        id = 2;
        name = "Blind";
        icon = AssetPool.getImage("blind.png");
        isNegative = true;
    }

    public void remove(){
        player.getEnvironmentManager().lighting.transit = true;
        player.getEnvironmentManager().lighting.fadeOut = true;
     }

   public void affect(){
        player.getEnvironmentManager().lighting.transit = true;
        player.getEnvironmentManager().lighting.fadeIn = true;
   }

   public Blind clone(){
        return new Blind(player, effectDuration);
   }
}
