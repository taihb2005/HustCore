package entity.effect.type;

import entity.effect.Effect;
import entity.player.Player;
import graphics.Sprite;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Blind extends Effect {
    public Blind(Player player , int duration) {
        super(player);
        setEffectDuration(duration);
        id = 2;
        name = new StringBuilder("Blind");
        icon = new Sprite("/effect/blind.png").getSpriteSheet();
    }

   public void affect(){

   }
   public void remove(){
       GamePanel.environmentManager.lighting.transit = true;
       GamePanel.environmentManager.lighting.fadeOut = true;
   }

   public void setEffectAnimation(){
       GamePanel.environmentManager.lighting.transit = true;
       GamePanel.environmentManager.lighting.fadeIn = true;
   }
}
