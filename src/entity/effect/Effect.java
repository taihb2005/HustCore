package entity.effect;

import entity.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Effect {
    protected Player player;
    protected int id;
    public String name;
    protected int effectDuration;
    protected int effectDurationCounter = 0;
    public boolean effectFinished ;

    public BufferedImage icon;

    public Effect(Player player){
        this.player = player;
    }

    public void render(Graphics2D g2){

    };
    public void add(){
        setEffectDuration(effectDuration);
        affect();
        if(!player.effectManager.containsKey(name)){
            player.effectManager.put(name , effectDuration);
            setEffectAnimation();
            player.effect.add(this);
        } else{
            player.effectManager.put(name , effectDuration);
            for(Effect eff : player.effect){
                if(eff.name.equals(name)) {
                    eff.setEffectDuration(effectDuration);
                    break;
                }
            }
        }
    }
    public void update(){
        effectDurationCounter++;
        if(effectDurationCounter >= effectDuration){
            effectFinished = true;
            effectDurationCounter = 0;
        }
    };

    public void affect(){}
    public void remove(){};
    public void setEffectAnimation(){};
    public void setEffectDuration(int duration){
        effectFinished = false;
        effectDuration = duration;
    }
}
