package org.kat.app.ui.views;

import org.kat.app.ui.UIKeyboardNavigator;
import org.kat.app.ui.Updatable;
import org.kat.app.ui.components.Button;
import org.kat.app.util.Tree;

import java.awt.*;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class UIScreen implements UIKeyboardNavigator {
    protected String id;
    protected Tree<View> viewTree;

    protected List<Button> buttonList;
    protected int buttonsNum;
    protected Cursor cursor;
    protected int currentPos;


    public UIScreen(String id, Tree<View> viewTree){
        this.id = id;
        this.viewTree = viewTree;

        buttonList = getButtonList();
        buttonsNum = getButtonCount();
        cursor = getCursor();
        currentPos = getDefaultCursorPos();

        buttonList.get(currentPos)
                .setHover();
        getCursor().attach(buttonList.get(currentPos));

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

    @Override
    public int getCurrentPos(){
        return currentPos;
    }

    @Override
    public void setCurrentPos(int currentPos){
        this.currentPos = currentPos;
    }

    protected abstract void onCreate();

    public void update(){
        handleKeyNavigation();
        viewTree.inOrderTraverse(viewTree.getRoot(),
                (node) -> {
                    View currentView = node.getData();
                    if(currentView instanceof Updatable updatable) updatable.update();
                });
    }

    public void render(Graphics2D g2){
        viewTree.inOrderTraverse(viewTree.getRoot(),
                (node) -> {
                    View currentView = node.getData();
                    currentView.render(g2);
                });

        if(cursor != null) cursor.render(g2);
    }

}
