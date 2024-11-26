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
        if(!player.effectManager.containsKey(name)){ //Nếu như chưa tồn tại effect thì thêm vào
            player.effectManager.put(name , effectDuration);
            setEffectAnimation();
            player.effect.add(this);
        } else{         //Nếu như tồn tại rồi thì check một vài thứ sau
            for(Effect eff : player.effect){
                if(eff.name.equals(name)) {   //Tìm đến cái effect đã tồn tại
                    int effectRemainingTime = player.effectManager.get(name) - effectDurationCounter;
                    if(effectRemainingTime < effectDuration) { //Nếu như thời gian effect còn lại nhỏ hơn thì mới set cái mới
                        eff.setEffectDuration(effectDuration);
                        player.effectManager.put(name , effectDuration); //Đặt thời lượng của effect vào map để dùng sau
                    }
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
        effectDurationCounter = 0;
    }
}
