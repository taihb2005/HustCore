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

     }

   public void affect(){

   }
}
