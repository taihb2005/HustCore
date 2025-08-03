package org.kat.app.ui.hustcore;

import org.kat.app.main.UI;
import org.kat.app.thread.LoadingService;
import org.kat.app.ui.UIComponentListener;
import org.kat.app.ui.components.GameButton;
import org.kat.app.ui.views.UIScreen;
import org.kat.app.ui.views.View;
import org.kat.app.util.Tree;

public class LoseScreen extends UIScreen {
    public LoseScreen(String id, Tree<View> viewTree) {
        super(id, viewTree);
    }

    @Override
    protected void onCreate() {
        ((GameButton) findViewById("tryAgainBtn")).setListener(new UIComponentListener() {
            @Override
            public void onPress() {
                LoadingService.restart();
            }
        });

        ((GameButton) findViewById("mainMenuBtn")).setListener(new UIComponentListener() {
            @Override
            public void onPress() {
                LoadingService.dispose();
                UI._UIManager.setCurrentScreen("main_menu");
            }
        });
    }


}
