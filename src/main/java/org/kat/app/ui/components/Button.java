package org.kat.app.ui.components;

import org.kat.app.main.KeyHandler;
import org.kat.app.ui.UIComponentListener;
import org.kat.app.ui.Updatable;
import org.kat.app.ui.views.Text;
import org.kat.app.ui.views.View;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Button extends View implements Updatable {
    private final static UIComponentListener DEFAULT_LISTENER = () -> {};

    protected Text text;
    private State currentState;
    private State lastState;

    protected int roundArc;

    protected UIComponentListener listener = DEFAULT_LISTENER;

    public Button(){
        currentState = State.IDLE;
        lastState = State.IDLE;
    }

    public void setListener(UIComponentListener listener){
        this.listener = listener;
    }

    public int getRoundArc() {
        return roundArc;
    }


    @Override
    public void update(){
        switch(currentState){
            case IDLE -> {
                if(lastStateWas(State.HOVER)){
                    lastState = currentState;
                    listener.onHover();
                }
            }

            case HOVER -> {
                if(KeyHandler.enterPressed){
                    KeyHandler.enterPressed = false;
                    listener.onPress();
                } else if(KeyHandler.rightPressed){
                    KeyHandler.rightPressed = false;
                    listener.onIncrease();
                } else if(KeyHandler.leftPressed){
                    KeyHandler.leftPressed = false;
                    listener.onDecrease();
                }

                if(lastStateWas(State.IDLE)){
                    lastState = currentState;
                    listener.onExit();
                }
            }
        }
    }

    public void setHover(){
        currentState = State.HOVER;
    }

    public void setIdle(){
        currentState = State.IDLE;
    }

    public void setState(State newState){
        this.currentState = newState;
    }

    public boolean lastStateWas(State state){
        return lastState == state;
    }

    public boolean currentStateIs(State state){
        return currentState == state;
    }

    @Override
    public void render(Graphics2D g2){
        g2.drawRect(x, y, width, height);

        g2.setColor(Color.WHITE);

        text.render(g2);
    }

    public enum State{
        IDLE, HOVER
    }
}
