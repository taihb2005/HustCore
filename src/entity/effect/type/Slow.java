package entity.effect.type;

import entity.effect.Effect;
import entity.player.Player;
import graphics.AssetPool;

public class Slow extends Effect {
    public Slow(Player player , int duration){
        super(player);
        setEffectDuration(duration);
        id = 1;
        name = "Slow";
        icon = AssetPool.getImage("slow.png");
    }

    public void affect(){
        if(!player.effectManager.containsKey("Speed Boost")) {
            player.speed = player.lastSpeed / 2;
        }
    }
    public void remove(){
        player.speed = player.lastSpeed;
    }

    public Slow clone(){
        return new Slow(player, effectDuration);
    }
}
