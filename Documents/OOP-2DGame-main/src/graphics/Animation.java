package graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Animation {
    public BufferedImage[] ani_sprite;

    private int frameCounts;
    private int currentFrames;

    private int ani_Speed;
    private int ani_Tick;

    public Animation()
    {
        ani_Tick = 0;
    }

    public void setAnimationState(BufferedImage[] frame , int ani_Speed)
    {
        this.ani_Tick = 0;
        this.ani_sprite = frame;
        this.ani_Speed = ani_Speed;
        this.frameCounts = frame.length;
        this.currentFrames = 0;
    }

    public void update()
    {
        ani_Tick++;
        if(ani_Tick >= ani_Speed)
        {
            ani_Tick = 0;
            currentFrames = (currentFrames + 1) % frameCounts;
        }
    }

    public void setDelay(int delay){this.ani_Speed = delay;};

    public int getCurrentFrames(){return currentFrames;};
    public BufferedImage[] getAni_sprite(){return ani_sprite;};
}

