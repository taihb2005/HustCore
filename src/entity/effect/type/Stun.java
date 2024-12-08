package entity.effect.type;

import entity.effect.Effect;
import entity.player.Player;
import graphics.Sprite;

import java.awt.*;

public class Stun extends Effect {
    public Stun(Player player) {
        super(player);
        setEffectDuration(100);
        id = 3;
        name = "Stun";
        icon = new Sprite("/effect/stun.png" , 32 , 32).getSpriteSheet();
    }

    public void affect(){
        player.speed = 0;
    }

    public void remove(){
        player.speed = player.last_speed;
    }
}
