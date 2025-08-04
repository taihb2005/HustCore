package org.kat.app.ui.hustcore;

import org.kat.app.level.LevelState;
import org.kat.app.main.UI;
import org.kat.app.ui.UIComponentListener;
import org.kat.app.ui.components.GameButton;
import org.kat.app.ui.views.EditPassword;
import org.kat.app.ui.views.UIScreen;
import org.kat.app.ui.views.View;
import org.kat.app.util.Tree;

import static org.kat.app.main.GamePanel.currentLevel;

public class PasswordInput extends UIScreen {
    private EditPassword editPassword;
    private String correctPassword;
    public PasswordInput(String id, Tree<View> viewTree) {
        super(id, viewTree);
    }

    public boolean isCorrect(){
        return editPassword.isCorrect();
    }

    @Override
    protected void onCreate() {
        editPassword = (EditPassword) findViewById("editPassword");

        ((GameButton) findViewById("confirmButton")).setListener(new UIComponentListener() {
            @Override
            public void onPress() {
                editPassword.check(correctPassword);
            }
        });
    }

    @Override
    public void onLeave(){
        if(UI._UIManager.getCurrentScreen().getId().equals("password_input")) {
            UI._UIManager.clearFromScreenStack();
        }

        currentLevel.setLevelState(LevelState.RUNNING);
    }

    public void setCorrectPassword(String correctPassword) {
        this.correctPassword = correctPassword;
    }

}
