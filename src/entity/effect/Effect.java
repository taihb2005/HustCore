package entity.effect;

import entity.player.Player;
import util.GameTimer;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Effect implements Affectable {
    protected Player player;
    protected int id;
    public String name;
    protected int effectDuration;
    protected GameTimer effectCounter;
    public boolean effectFinished ;

    public BufferedImage icon;

    public Effect(Player player){
        this.player = player;
        effectCounter = new GameTimer(null, false);
    }

    public void render(Graphics2D g2){

    };
    public void add() {
        setEffectDuration(effectDuration);
        affect();

        if (player.effectManager.containsKey("Effect Immunity")) return;

        int currentDuration = player.effectManager.getOrDefault(name, -1);

        if (currentDuration <= effectDuration) {
            player.effectManager.put(name, effectDuration);

            if (currentDuration == -1) {
                player.effect.add(this);
            }
        } else if (currentDuration != -1) {
            effectCounter.reset();
        }
    }


    public void update(){
        effectCounter.update();
        effectFinished = effectCounter.isFinished();
    };

    public void affect(){}
    public void remove(){};
    public void setEffectAnimation(){};
    public void setEffectDuration(int duration){
        effectFinished = false;
        effectDuration = duration;
        effectCounter.setPeriod(duration);
    }


    public Effect clone(){
        return null;
    };
}
