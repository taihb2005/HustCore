package graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class Animation{
    public ArrayList<BufferedImage> ani_sprite;

    private int frameCounts;
    private int currentFrames;

    private int ani_Speed;
    private int ani_Tick;

    private boolean playOnce;
    private boolean playFinished;


    public Animation()
    {
        ani_Tick = 0;
    }

    public void setAnimationState(BufferedImage[] frame ,int ani_Speed)
    {
        this.ani_Tick = 0;
        this.ani_sprite = new ArrayList<>(Arrays.asList(frame));
        this.ani_Speed = ani_Speed;
        this.frameCounts = ani_sprite.size();
        this.currentFrames = 0;
    }

    public void setAnimationState(BufferedImage[] frame ,int ani_Speed , boolean reset)
    {
        this.ani_Tick = 0;
        this.ani_sprite = new ArrayList<>(Arrays.asList(frame));
        this.ani_Speed = ani_Speed;
        this.frameCounts = ani_sprite.size();
        this.currentFrames = currentFrames;
    }


    public void update()
    {
        if (playOnce && currentFrames == frameCounts - 1) {
            playOnce = false;
            playFinished = true;
            return;
        }
        ani_Tick++;
        if(ani_Tick >= ani_Speed)
        {
            ani_Tick = 0;
            currentFrames = (currentFrames + 1) % frameCounts;
        }
    }

    public boolean isPlaying() {
        return playOnce;
    }

    public void playOnce(){
        playOnce = true;
        playFinished = false;
        ani_Tick = 0;
        currentFrames = 0;
    }

    public void setAnimationSpeed(int speed){this.ani_Speed = speed;}
    public int getCurrentFrames(){return currentFrames;}
    public int getFrameCounts(){return frameCounts;}
    public ArrayList<BufferedImage> getAni_sprite(){return ani_sprite;}
}