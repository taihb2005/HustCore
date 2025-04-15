package graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class Animation{
    public ArrayList<BufferedImage> ani_sprite;
    public BufferedImage[] frame;

    private int frameCounts;
    private int currentFrames;

    private int aniSpeed;
    private int aniTick;

    private boolean playing;
    private boolean loop;
    private boolean finished;


    public Animation()
    {
        aniTick = 0;
    }

    public Animation(BufferedImage[] frame, int aniSpeed){
        this.frame = frame;
        this.aniTick = 0;
        this.playing = true;
        this.finished = false;
        this.aniSpeed = aniSpeed;
        this.frameCounts = frame.length;
        this.currentFrames = 0;
    }

    public Animation(BufferedImage[] frame, int aniSpeed, boolean loop){
        this.frame = frame;
        this.aniTick = 0;
        this.playing = true;
        this.finished = false;
        this.loop = loop;
        this.aniSpeed = aniSpeed;
        this.frameCounts = frame.length;
        this.currentFrames = 0;
    }

    public Animation(BufferedImage[] frame, int initialFrame, int aniSpeed, boolean loop){
        this.frame = frame;
        if(initialFrame > frame.length) throw new RuntimeException("Frame ban đầu phải nhỏ hơn số frame của animation");
        this.currentFrames = initialFrame;
        this.aniTick = 0;
        this.playing = true;
        this.finished = false;
        this.loop = loop;
        this.aniSpeed = aniSpeed;
        this.frameCounts = frame.length;


    }

    public void setAnimationState(BufferedImage[] frame ,int ani_Speed)
    {
        this.aniTick = 0;
        this.ani_sprite = new ArrayList<>(Arrays.asList(frame));
        this.aniSpeed = ani_Speed;
        this.frameCounts = ani_sprite.size();
        this.currentFrames = 0;
    }

    public void setAnimationState(BufferedImage[] frame , int initialFrame,int ani_Speed)
    {
        this.aniTick = 0;
        this.ani_sprite = new ArrayList<>(Arrays.asList(frame));
        this.aniSpeed = ani_Speed;
        this.frameCounts = ani_sprite.size();
        this.currentFrames = initialFrame;
    }

    public void setAnimationState(BufferedImage[] frame ,int ani_Speed , boolean reset)
    {
        this.aniTick = 0;
        this.ani_sprite = new ArrayList<>(Arrays.asList(frame));
        this.aniSpeed = ani_Speed;
        this.frameCounts = ani_sprite.size();
    }


    public void update()
    {
        if (!playing || finished) return;

        aniTick++;

        if (aniTick >= aniSpeed)
        {
            if (loop) {
                aniTick = 0;
                currentFrames = (currentFrames + 1) % frameCounts;
            } else {
                currentFrames++;
                if (currentFrames >= frameCounts) {
                    currentFrames = frameCounts - 1;
                    playing = false;
                    finished = true;
                }
                aniTick = 0;
            }
        }
    }


    public void render(Graphics2D g2, int x, int y){
        g2.drawImage(frame[currentFrames], x, y , null);
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isFinished(){
        return finished;
    }

    public void playOnce(){
        loop = false;
        finished = false;
        aniTick = 0;
        currentFrames = 0;
    }

    public void reset(){
        currentFrames = 0;
        aniTick = 0;
        playing = true;
        finished = false;
    }

    public void setAnimationSpeed(int speed){this.aniSpeed = speed;}
    public int getCurrentFrames(){return currentFrames;}
    public int getFrameCounts(){return frameCounts;}
    public ArrayList<BufferedImage> getAni_sprite(){return ani_sprite;}

    public Animation clone(){
        return new Animation(frame, aniSpeed, loop);
    }

    public Animation clone(int initialFrame){
        return new Animation(frame, initialFrame, aniSpeed, loop);
    }

    public void dispose(){
        for(BufferedImage bf : ani_sprite){
            if(bf != null) bf.flush();
        }
        ani_sprite.clear();
        ani_sprite = null;
    }
}