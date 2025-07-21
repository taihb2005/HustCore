package org.kat.app.entity.effect.type;

import org.kat.app.entity.effect.Effect;
import org.kat.app.entity.player.Player;
import org.kat.app.graphics.AssetPool;

public class Speed extends Effect {
    public Speed(Player player , int duration) {
        super(player);
        setEffectDuration(duration);
        id = 4;
        name = "Speed";
        isNegative = false;
        icon = AssetPool.getImage("speed_boost.png");
    }

    public void affect(){
        if(!player.effectManager.containsKey("Slow")) {
            player.effect.removeIf(e -> {
                if (e instanceof Slow) {
                    e.remove();
                    player.effectManager.remove(e.name);
                    return true;
                }
                return false;
            });
            player.speed = player.lastSpeed + 2;
        }
    }

    public void remove(){
        player.speed = player.lastSpeed;
    }

    public Speed clone(){
        return new Speed(player, effectDuration);
    }

}