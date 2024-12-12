package entity.effect.type;

import entity.effect.Effect;
import entity.player.Player;
import graphics.Sprite;

import java.awt.*;

public class SpeedBoost extends Effect {
    public SpeedBoost(Player player , int duration) {
        super(player);
        setEffectDuration(duration);
        id = 4;
        name = new StringBuilder("Speed Boost");
        icon = new Sprite("/effect/speed_boost.png" , 32  ,32).getSpriteSheet();
    }

    public void affect(){
        if(!player.effectManager.containsKey("Slow")) {
            player.speed = player.last_speed + 2;
        }
    }

    public void remove(){
        player.speed = player.last_speed;
    }

}
