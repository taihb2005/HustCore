package org.kat.app.ui.hustcore;

import org.jetbrains.annotations.NotNull;
import org.kat.app.main.UI;
import org.kat.app.thread.LoadingService;
import org.kat.app.ui.UIComponentListener;
import org.kat.app.ui.components.Button;
import org.kat.app.ui.views.Cursor;
import org.kat.app.ui.views.UIScreen;
import org.kat.app.ui.views.View;
import org.kat.app.util.Tree;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.kat.app.main.GamePanel.ui;

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
    }

    @Override
    public List<Button> getButtonList(){
        if(this.buttonList == null){
            List<Button> buttons = new ArrayList<>();

            List<View> list = viewTree.getAll(viewTree.getRoot(),
                    (node) -> node.getData() instanceof Button);

            for(View v: list){
                buttons.add((Button) v);
            }

            return buttons;
        } else return this.buttonList;
    }

    @Override
    public Cursor getCursor(){
        return (this.cursor == null) ? new Cursor() : this.cursor;
    }

    @Override
    public int getDefaultCursorPos(){
        return 0;
    }

    @Override
    public int getCursorPos() {
        return this.currentPos;
    }

    @Override
    public void setCursorPos(int pos) {
        this.currentPos = pos;
    }

//    @Override
//    protected void handleEvent(){
//        if(KeyHandler.downPressed){
//            KeyHandler.downPressed = false;
//            buttonList.get(commandNum).setIdle();
//            if(commandNum >= MAX_BUTTON_NUM - 1){
//                commandNum = 0;
//            } else commandNum++;
//        } else if(KeyHandler.upPressed){
//            KeyHandler.upPressed = false;
//            buttonList.get(commandNum).setIdle();
//            if(commandNum <= 0){
//                commandNum = MAX_BUTTON_NUM - 1;
//            } else commandNum--;
//        }
//
//        buttonList.get(commandNum)
//                .setHover();
//
//        try {
//            cursor.setY(defaultCursorY + BUTTON_MARGIN * commandNum);
//
//            //System.out.println(cursor.getY());
//        } catch(NullPointerException e){
//            cursor = viewTree.get(viewTree.getRoot(),
//                            (node) -> node.getData().getId().equals("mainMenuCursor"))
//                    .getData();
//            defaultCursorY = cursor.getY();
//        }
//
//    }

    @Override
    public void render(Graphics2D g2){
        super.render(g2);
    }
}
