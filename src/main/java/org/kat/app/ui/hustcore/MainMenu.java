package org.kat.app.ui.hustcore;

import org.jetbrains.annotations.NotNull;
import org.kat.app.main.UI;
import org.kat.app.thread.LoadingService;
import org.kat.app.ui.UIComponentListener;
import org.kat.app.ui.components.Button;
import org.kat.app.ui.views.*;
import org.kat.app.ui.views.Cursor;
import org.kat.app.util.Tree;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class MainMenu extends UIScreen {
    public MainMenu(@NotNull String id, Tree<View> viewTree){
        super(id, viewTree);
    }

    @Override
    protected void onCreate(){
        //=========Set up listener========
        ((Button) findViewById("startBtn")).setListener(new UIComponentListener() {
            @Override
            public void onPress() {
                LoadingService.loadResource();
                LoadingService.loadMap();
            }
        });

        ((Button) findViewById("settingsBtn")).setListener(new UIComponentListener() {
            @Override
            public void onPress() {
                UI._UIManager.setCurrentScreen("setting_menu");
            }
        });

        ((Button) findViewById("exitBtn")).setListener(new UIComponentListener() {
            @Override
            public void onPress() {
                exit(0);
            }
        });
    }

    @Override
    protected void onShow(){
        for (Button btn : buttonList) {
            btn.setIdle();
        }

        Button defaultButton = buttonList.get(currentPos);
        defaultButton.setHover();

        getCursor().attach(defaultButton);
        getCursor().hold();
    }

    @Override
    public void render(Graphics2D g2){
        super.render(g2);
    }
}
