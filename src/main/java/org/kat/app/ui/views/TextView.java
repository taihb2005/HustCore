package org.kat.app.ui.views;

import org.kat.app.ui.Updatable;
import org.kat.app.util.GameTimer;

import java.awt.*;

import static org.kat.app.main.GamePanel.playSE;

public class TextView extends View implements Updatable {
    protected final Text text;
    private Text currentDisplayText;
    protected boolean displayCharByChar;
    private GameTimer currentDisplayTextTimer;

    protected TextState lastState;
    protected TextState currentState;
    protected int textIndex;

    public TextView(Text text){
        this.text = text;
    }

    public TextView(Text text, int x, int y, int width, int height){
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.displayCharByChar = false;
        this.lastState = TextState.IDLE;
        this.currentState = TextState.IDLE;

        text.attach(this);
    }

    public void setDimensions(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.displayCharByChar = false;
        this.lastState = TextState.IDLE;
        this.currentState = TextState.IDLE;

        text.attach(this);
    }

    public Text getText(){
        return text;
    }

    public TextView setText(String text){
        this.text.setText(text);
        return this;
    }

    public TextView enableDisplayCharByChar(){
        this.displayCharByChar = true;
        this.textIndex = 0;
        this.lastState = TextState.PLAYING;
        this.currentState = TextState.PLAYING;

        if(currentDisplayText == null){
            currentDisplayText = new Text("");
            currentDisplayText.setProperties(text);

            currentDisplayText.attach(this);
        } else {
            currentDisplayText.clear();
        }

        if(currentDisplayTextTimer == null){
            currentDisplayTextTimer =  new GameTimer(
                    ()->{
                        currentDisplayText.append(text.getTextAt(textIndex));
                        playSE(1);
                        textIndex++;

                        if(currentDisplayText.equals(text)){
                            reset();
                        }
                    }, 1, true
            );
        }
        return this;
    }

    public void skipDisplaying(){
        currentState = TextState.IDLE;
        displayCharByChar = false;
    }

    public void reset(){
        currentState = TextState.IDLE;
        textIndex = 0;
        currentDisplayText.clear();
        displayCharByChar = false;
    }

    public boolean isPlaying(){
        return currentState == TextState.PLAYING;
    }

    public boolean isDisplayFinished(){
        return currentState == TextState.IDLE && lastState == TextState.PLAYING;
    }

    @Override
    public void update(){
        if(displayCharByChar){
            if(currentDisplayTextTimer != null){currentDisplayTextTimer.update();}
        }
        text.attach(this);
    }

    @Override
    public void render(Graphics2D g2){
        if(displayCharByChar){
            currentDisplayText.render(g2);
        } else {
            text.render(g2);
        }
//        g2.drawRect(x, y, width, height);
    }

    public enum TextState{
        IDLE, PLAYING
    }
}
