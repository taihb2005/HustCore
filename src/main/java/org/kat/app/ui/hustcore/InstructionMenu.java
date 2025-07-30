package org.kat.app.ui.hustcore;

import org.kat.app.main.UI;
import org.kat.app.ui.UIComponentListener;
import org.kat.app.ui.components.Button;
import org.kat.app.ui.views.UIScreen;
import org.kat.app.ui.views.View;
import org.kat.app.util.Tree;

public class InstructionMenu extends UIScreen {
    public InstructionMenu(String id, Tree<View> viewTree) {
        super(id, viewTree);
    }

    @Override
    protected void onCreate() {
        Button goBack =  (Button) findViewById("goBackInstructionButton");
        goBack.setListener(new UIComponentListener() {
            @Override
            public void onPress() {
                if(!(UI._UIManager.getCurrentScreen() instanceof MainMenu)) {
                    UI._UIManager.setCurrentScreen("setting_menu");
                }
            }
        });
    }

}
