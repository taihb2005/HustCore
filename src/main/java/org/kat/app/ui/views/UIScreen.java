package org.kat.app.ui.views;

import org.kat.app.main.KeyHandler;
import org.kat.app.ui.Updatable;
import org.kat.app.ui.components.Button;
import org.kat.app.util.Tree;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class UIScreen{
    protected String id;
    protected Tree<View> viewTree;

    protected Visibility currentVisibility;
    protected Visibility lastVisibility;
    protected List<Button> buttonList;
    protected int buttonsNum;
    protected int defaultPos;
    protected int currentPos;


    public UIScreen(String id, Tree<View> viewTree){
        this.id = id;
        this.viewTree = viewTree;

        lastVisibility = Visibility.INVISIBLE;
        currentVisibility = Visibility.INVISIBLE;
        buttonList = getButtonList();
        buttonsNum = getButtonCount();
        currentPos = getDefaultCursorPos();
        defaultPos = getDefaultCursorPos();

        if(buttonList != null && !buttonList.isEmpty()) {
            buttonList.get(currentPos)
                    .setHover();
            getCursor().attach(buttonList.get(currentPos));
        }

        viewTree.inOrderTraverse(viewTree.getRoot(),
                (node) -> {
                    View currentView = node.getData();
                    currentView.show();
                });

        onCreate();
    }

    protected View findViewById(String id){
        View view = viewTree.get(viewTree.getRoot(),
                (node) -> node.getData().getId().equals(id))
                .getData();

        if(view == null){
            throw new NoSuchElementException("No such view with id as: " + id);
        }

        return view;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setViewTree(Tree<View> viewTree){
        this.viewTree = viewTree;
    }

    protected abstract void onCreate();
    protected void onShow(){
        currentPos = 0;

        for (Button btn : buttonList) {
            btn.setIdle();
        }

        Button defaultButton = buttonList.get(currentPos);
        defaultButton.setHover();

        getCursor().attach(defaultButton);
        getCursor().hold();
    }
    protected void onLeave(){

    }

    public void show(){
        currentVisibility = Visibility.VISIBLE;
        lastVisibility = Visibility.INVISIBLE;
    }

    public void hide(){
        currentVisibility = Visibility.INVISIBLE;
        lastVisibility = Visibility.VISIBLE;
    }

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

    public int getButtonCount(){
        if(getButtonList() == null)
            return 0;
        return getButtonList().size();
    }

    public Cursor getCursor(){
        return buttonList.get(currentPos).getCursor();
    };

    public int getDefaultCursorPos(){
        return 0;
    }

    public int getCurrentCursorPos(){
        return this.currentPos;
    };
    public void setCursorPos(int pos){
        this.currentPos = pos;
    };

    private void handleKeyNavigation() {
        int cursorPos = getCurrentCursorPos();

        if (KeyHandler.downPressed) {
            KeyHandler.downPressed = false;

            Button button = buttonList.get(cursorPos);
            button.setIdle();
            getCursor().release();

            cursorPos = getNextEnabledButtonPos(cursorPos);

            setCursorPos(cursorPos);
            getCursor().release();
        }
        else if (KeyHandler.upPressed) {
            KeyHandler.upPressed = false;

            Button button = buttonList.get(cursorPos);
            button.setIdle();
            getCursor().release();

            cursorPos = getPreviousEnabledButtonPos(cursorPos);

            setCursorPos(cursorPos);
            getCursor().release();
        } else if(KeyHandler.keyEscpressed) {
            KeyHandler.keyEscpressed = false;
            onLeave();
        }

        Button button = buttonList.get(cursorPos);
        button.setHover();
        getCursor().attach(button);
        getCursor().hold();
    }

    private int getNextEnabledButtonPos(int cursorPos){
        List<Button> buttons = getButtonList();
        int attempts = 0;
        do {
            cursorPos++;
            if (cursorPos >= buttons.size()) {
                cursorPos = 0;
            }
            attempts++;
        } while (buttons.get(cursorPos).currentStateIs(Button.ButtonState.DISABLE)
                && attempts <= buttons.size());

        return cursorPos;
    }

    private int getPreviousEnabledButtonPos(int cursorPos){
        List<Button> buttons = getButtonList();
        int attempts = 0;
        do {
            cursorPos--;
            if (cursorPos < 0) {
                cursorPos = buttons.size() - 1;
            }
            attempts++;
        } while (buttons.get(cursorPos).currentStateIs(Button.ButtonState.DISABLE)
                && attempts <= buttons.size());

        return cursorPos;
    }

    public void update(){
        if(lastVisibility == Visibility.INVISIBLE && currentVisibility == Visibility.VISIBLE){
            lastVisibility = Visibility.VISIBLE;
            onShow();
        } else if(lastVisibility == Visibility.VISIBLE && currentVisibility == Visibility.INVISIBLE){
            lastVisibility = Visibility.INVISIBLE;
        }
        handleKeyNavigation();
        viewTree.inOrderTraverse(viewTree.getRoot(),
                (node) -> {
                    View currentView = node.getData();
                    if(currentView instanceof Updatable updatable) updatable.update();
                });

        Cursor cursor = getCursor();
        if(cursor != null) {
            cursor.update();
        }
    }

    public void render(Graphics2D g2){
        viewTree.inOrderTraverse(viewTree.getRoot(),
                (node) -> {
                    View currentView = node.getData();
                    if(currentView.isVisible()) currentView.render(g2);
                });

        Cursor cursor = getCursor();
        if(cursor != null){
            cursor.render(g2);
        }
    }
}
