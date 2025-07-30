package org.kat.app.ui;

import org.kat.app.main.KeyHandler;
import org.kat.app.main.Main;
import org.kat.app.main.UI;
import org.kat.app.ui.components.Button;
import org.kat.app.ui.hustcore.MainMenu;
import org.kat.app.ui.views.AdjustableCursor;
import org.kat.app.ui.views.Cursor;

import java.util.List;

public interface UIKeyboardNavigator {
//    List<Button> getButtonList();
//    default int getButtonCount(){
//        if(getButtonList() == null)
//            return 0;
//        return getButtonList().size();
//    }
//    Cursor getCursor();
//    int getDefaultCursorPos();
//    int getCurrentCursorPos();
//    void setCursorPos(int pos);
//
//    default void handleKeyNavigation() {
//        List<Button> buttons = getButtonList();
//        int cursorPos = getCurrentCursorPos();
//
//        if (KeyHandler.downPressed) {
//            KeyHandler.downPressed = false;
//
//            Button button = buttons.get(cursorPos);
//            button.setIdle();
//
//            cursorPos = getNextEnabledButtonPos(cursorPos);
//
//            setCursorPos(cursorPos);
//            getCursor().release();
//        }
//        else if (KeyHandler.upPressed) {
//            KeyHandler.upPressed = false;
//
//            Button button = buttons.get(cursorPos);
//            button.setIdle();
//
//            cursorPos = getPreviousEnabledButtonPos(cursorPos);
//
//            setCursorPos(cursorPos);
//            getCursor().release();
//        } else if(KeyHandler.keyEscpressed) {
//            KeyHandler.keyEscpressed = false;
//            onLeave();
//        }
//
//        Button button = buttons.get(cursorPos);
//        button.setHover();
//        getCursor().attach(button);
//        getCursor().hold();
//    }
//
//    private int getNextEnabledButtonPos(int cursorPos){
//        List<Button> buttons = getButtonList();
//        int attempts = 0;
//        do {
//            cursorPos++;
//            if (cursorPos >= buttons.size()) {
//                cursorPos = 0;
//            }
//            attempts++;
//        } while (buttons.get(cursorPos).currentStateIs(Button.ButtonState.DISABLE)
//                && attempts <= buttons.size());
//
//        return cursorPos;
//    }
//
//    private int getPreviousEnabledButtonPos(int cursorPos){
//        List<Button> buttons = getButtonList();
//        int attempts = 0;
//        do {
//            cursorPos--;
//            if (cursorPos < 0) {
//                cursorPos = buttons.size() - 1;
//            }
//            attempts++;
//        } while (buttons.get(cursorPos).currentStateIs(Button.ButtonState.DISABLE)
//                && attempts <= buttons.size());
//
//        return cursorPos;
//    }
//
//    default void onLeave(){
//        if(!(UI._UIManager.getCurrentScreen() instanceof MainMenu)) {
//            UI._UIManager.popFromScreenStack();
//        }
//    };
}
