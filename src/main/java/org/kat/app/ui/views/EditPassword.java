package org.kat.app.ui.views;

import org.kat.app.level.LevelState;
import org.kat.app.ui.Updatable;
import org.kat.app.ui.hustcore.PasswordInput;

import java.awt.*;

import static org.kat.app.main.GamePanel.currentLevel;
import static org.kat.app.main.KeyHandler.*;
import static org.kat.app.main.KeyHandler.key2pressed;
import static org.kat.app.main.KeyHandler.key3pressed;
import static org.kat.app.main.KeyHandler.key4pressed;
import static org.kat.app.main.KeyHandler.key5pressed;
import static org.kat.app.main.KeyHandler.key6pressed;
import static org.kat.app.main.KeyHandler.key7pressed;
import static org.kat.app.main.KeyHandler.key8pressed;
import static org.kat.app.main.KeyHandler.key9pressed;

public class EditPassword extends TextView implements Updatable {
    private int maxLength = 20;
    private StringBuilder charPressed;

    private PasswordState currentState;
    private PasswordState finalState;
    public EditPassword(Text text) {
        super(text);

        charPressed = new StringBuilder();
        currentState = PasswordState.IDLE;
        finalState = PasswordState.IDLE;
    }

    public EditPassword(Text text, int x, int y, int width, int height, int maxLength) {
        super(text, x, y, width, height);

        charPressed = new StringBuilder();
        currentState = PasswordState.IDLE;
        finalState = PasswordState.IDLE;
        this.maxLength = maxLength;
    }

    public void check(Text correctPassword){
        if(text.equals(correctPassword)){
            currentState = PasswordState.CORRECT;
            finalState = PasswordState.CORRECT;
        } else {
            currentState = PasswordState.INCORRECT;
            finalState = PasswordState.INCORRECT;
        }
    }

    public void check(String correctPassword){
        if(text.getText().equals(correctPassword)){
            currentState = PasswordState.CORRECT;
            finalState = PasswordState.CORRECT;
        } else {
            currentState = PasswordState.INCORRECT;
            finalState = PasswordState.INCORRECT;
        }
    }

    public boolean isCorrect(){
        return finalState == PasswordState.CORRECT;
    }

    public void handleKeyInput(){
        charPressed.setLength(0);
        if(key0pressed) {key0pressed = false; charPressed.append("0");} else
        if(key1pressed) {key1pressed = false; charPressed.append("1");} else
        if(key2pressed) {key2pressed = false; charPressed.append("2");} else
        if(key3pressed) {key3pressed = false; charPressed.append("3");} else
        if(key4pressed) {key4pressed = false; charPressed.append("4");} else
        if(key5pressed) {key5pressed = false; charPressed.append("5");} else
        if(key6pressed) {key6pressed = false; charPressed.append("6");} else
        if(key7pressed) {key7pressed = false; charPressed.append("7");} else
        if(key8pressed) {key8pressed = false; charPressed.append("8");} else
        if(key9pressed) {key9pressed = false; charPressed.append("9");}

        if(text.getText().length() < maxLength && !charPressed.isEmpty()) {
            currentState = PasswordState.IDLE;
            text.append(charPressed.toString());
            System.out.println(text.getText());
        }

        if(keyBackspacepressed){
            keyBackspacepressed = false;
            if(!text.getText().isEmpty()){
                text.setText(text.getText().substring(0, text.getText().length() - 1));
                currentState = PasswordState.IDLE;
            }
        }
    }

    @Override
    public void update(){
        if(!currentLevel.checkState(LevelState.PASSWORD))
            return;
        handleKeyInput();

        switch(currentState){
            case IDLE -> text.setColor(Text.DEFAULT_COLOR);
            case CORRECT -> text.setColor(Color.GREEN);
            case INCORRECT -> text.setColor(Color.RED);
        }
    }

    @Override
    public void render(Graphics2D g2){
        text.render(g2);
    }

    public enum PasswordState{
        IDLE, CORRECT, INCORRECT
    }
}
