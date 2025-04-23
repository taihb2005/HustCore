package entity.effect.type;

import entity.effect.Effect;
import entity.player.Player;
import graphics.AssetPool;
import graphics.Sprite;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Blind extends Effect {
    public Blind(Player player , int duration) {
        super(player);
        setEffectDuration(duration);
        id = 2;
        name = "Blind";
        icon = AssetPool.getImage("blind.png");
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
