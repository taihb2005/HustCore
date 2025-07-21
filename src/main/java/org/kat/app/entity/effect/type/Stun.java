package org.kat.app.entity.effect.type;

import org.kat.app.entity.effect.Effect;
import org.kat.app.entity.player.Player;
import org.kat.app.graphics.AssetPool;

public class Stun extends Effect {
    public Stun(Player player) {
        super(player);
        setEffectDuration(100);
        id = 3;
        name = "Stun";
        isNegative = true;
        icon = AssetPool.getImage("stun.png");
    }

    public void affect(){
        player.speed = 0;
    }

    public void remove(){
        player.speed = player.lastSpeed;
    }

    public Stun clone(){
        return new Stun(player);
    }
}
