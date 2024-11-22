package entity.effect.type;

import entity.effect.Effect;
import entity.player.Player;
import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Slow extends Effect {
    public Slow(Player player , int duration){
        super(player);
        setEffectDuration(duration);
        id = 1;
        name = "Slow";
        icon = new Sprite("/effect/slow.png" , 32 , 32).getSpriteSheet();
    }

    public void affect(){
        if(player.effectManager.containsKey("Speed Boost")) {
            player.effectManager.remove("Speed Boost");
            remove();
            for(Effect e : player.effect){
                if(e.name.equals("Speed Boost")) {
                    e.effectFinished = true;
                    break;
                }
            }
        }
        player.speed = player.last_speed / 2;
    }
    public void remove(){
        player.speed = player.last_speed;
    }
}
