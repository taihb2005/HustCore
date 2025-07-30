package org.kat.app.ui.components;

import org.kat.app.main.KeyHandler;
import org.kat.app.ui.UIComponentListener;
import org.kat.app.ui.Updatable;
import org.kat.app.ui.views.AdjustableCursor;
import org.kat.app.ui.views.Cursor;
import org.kat.app.ui.views.Text;
import org.kat.app.ui.views.View;

import java.awt.*;


public class Button extends View implements Updatable {
    protected static final Color DISABLED_COLOR_BORDER = new Color(255, 255, 255, 77);
    protected static final Color DISABLED_COLOR_TEXT = new Color(255, 255, 255, 128);
    protected static final Cursor DEFAULT_CURSOR = new Cursor();
    protected static final Cursor AJUSTABLE_CURSOR = new AdjustableCursor();
    private final static UIComponentListener DEFAULT_LISTENER = () -> {};

    protected Cursor cursor = DEFAULT_CURSOR;
    protected Text text;
    protected ButtonState currentState;
    protected ButtonState lastState;

    protected int roundArc;

    protected UIComponentListener listener = DEFAULT_LISTENER;

    public Button(){
        currentState = ButtonState.IDLE;
        lastState = ButtonState.IDLE;
    }

    public Button(boolean enabled){
        currentState = (enabled) ? ButtonState.IDLE : ButtonState.DISABLE;
        lastState = ButtonState.IDLE;

    }

    public void setListener(UIComponentListener listener){
        this.listener = listener;
    }

    public int getRoundArc() {
        return roundArc;
    }

    public void setText(String text){
        this.text.setText(text);
    }

    public void setHover(){
        currentState = ButtonState.HOVER;
    }

    public void setIdle(){
        currentState = ButtonState.IDLE;
    }

    public void disable(){
        currentState = ButtonState.DISABLE;
    }

    public void enable(){
        currentState = ButtonState.IDLE;
    }

    public void setState(ButtonState newState){
        this.currentState = newState;
    }

    public Cursor getCursor(){
        return this.cursor;
    }

    public boolean lastStateWas(ButtonState state){
        return lastState == state;
    }

    public boolean currentStateIs(ButtonState state){
        return currentState == state;
    }

    @Override
    public void update(){
        switch(currentState){
            case IDLE -> {
                if(lastStateWas(ButtonState.HOVER)){
                    lastState = currentState;
                    listener.onHover();
                }
            }

            case HOVER -> {
                if(KeyHandler.enterPressed){
                    KeyHandler.enterPressed = false;
                    listener.onPress();
                    getCursor().release();
                } else if(KeyHandler.rightPressed){
                    KeyHandler.rightPressed = false;
                    listener.onIncrease();
                } else if(KeyHandler.leftPressed){
                    KeyHandler.leftPressed = false;
                    listener.onDecrease();
                }

                if(lastStateWas(ButtonState.IDLE)){
                    lastState = currentState;
                    listener.onExit();
                }
            }
        }
    }

    @Override
    public void render(Graphics2D g2){
        if(currentState == ButtonState.DISABLE){
            g2.setColor(DISABLED_COLOR_BORDER);
        } else {
            g2.setColor(Color.WHITE);
        }
        g2.drawRect(x, y, width, height);

        if(currentState == ButtonState.DISABLE){
            g2.setColor(DISABLED_COLOR_TEXT);
        } else {
            g2.setColor(Color.WHITE);
        }
        text.render(g2);
    }

    public enum ButtonState {
        IDLE, HOVER, DISABLE
    }
}
