package org.kat.app.ui;

import org.kat.app.main.KeyHandler;
import org.kat.app.ui.components.Button;
import org.kat.app.ui.views.Cursor;

import java.util.List;

public interface UIKeyboardNavigator {
    List<Button> getButtonList();
    default int getButtonCount(){
        return getButtonList().size();
    }
    Cursor getCursor();
    int getDefaultCursorPos();
    int getCursorPos();
    void setCursorPos(int pos);

    default void handleKeyNavigation() {
        List<Button> buttons = getButtonList();
        int cursorPos = getCursorPos();

        if (KeyHandler.downPressed) {
            KeyHandler.downPressed = false;

            Button button = buttons.get(cursorPos);
            button.setIdle();

            if(cursorPos == buttons.size() - 1){
                cursorPos = 0;
            } else {
                cursorPos++;
            }

            setCursorPos(cursorPos);
        }
        else if (KeyHandler.upPressed) {
            KeyHandler.upPressed = false;

            Button button = buttons.get(cursorPos);
            button.setIdle();

            if(cursorPos == 0){
                cursorPos = buttons.size() - 1;
            } else {
                cursorPos--;
            }

            setCursorPos(cursorPos);
        }

        Button button = buttons.get(cursorPos);
        button.setHover();
        getCursor().attach(button);
    }

    int getCurrentPos();

    void setCurrentPos(int currentPos);
}
