package entity;

import main.GamePanel;

import java.awt.*;

public class EffectManager {
    private int effectCounter = 0;
    private int effectDuration = 0;
    public EffectManager(){

    }

    public boolean slowEffect(){
        effectCounter++;
        if(effectCounter >= effectDuration){
            effectCounter = 0;
            return true;
        }
        return false;
    }
    public boolean speed_boostEffect(){
        effectCounter++;
        if(effectCounter >= effectDuration){
            effectCounter = 0;
            return true;
        }
        return false;
    }
    public boolean blindEffect(){
        effectCounter++;
        if(effectCounter >= effectDuration){
            effectCounter = 0;
            return true;
        }
        return false;
    }
    public boolean transistionBlindEffect(){
        boolean done = false;
        effectCounter++;
        if(effectCounter == 1) GamePanel.environmentManager.lighting.setLightSource(145);
        if(effectCounter == 2) GamePanel.environmentManager.lighting.setLightSource(130);
        if(effectCounter == 3) GamePanel.environmentManager.lighting.setLightSource(125);
        if(effectCounter == 4) GamePanel.environmentManager.lighting.setLightSource(120);
        if(effectCounter == 5) GamePanel.environmentManager.lighting.setLightSource(115);
        if(effectCounter == 6) GamePanel.environmentManager.lighting.setLightSource(105);

        if(effectCounter >= 6){
            done = true;
            effectCounter = 0;
        }
        return done;
    }
    public void stunEffect(){};
    public void poisonEffect(){};

    public void setEffectDuration(int effDuration){
        this.effectCounter = 0;
        this.effectDuration = effDuration;
    }
    public int getEffectDuration(){return effectDuration;}
}
