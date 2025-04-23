package entity.effect.type;

import entity.effect.Effect;
import entity.player.Player;
import graphics.AssetPool;

public class Stun extends Effect {
    public Stun(Player player) {
        super(player);
        setEffectDuration(100);
        id = 3;
        name = "Stun";
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
