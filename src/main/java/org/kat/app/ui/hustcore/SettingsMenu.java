package org.kat.app.ui.hustcore;

import org.kat.app.ui.components.Button;
import org.kat.app.ui.views.Cursor;
import org.kat.app.ui.views.UIScreen;
import org.kat.app.ui.views.View;
import org.kat.app.util.Tree;

import java.util.ArrayList;
import java.util.List;

public class SettingsMenu extends UIScreen {
    public SettingsMenu(String id, Tree<View> viewTree) {
        super(id, viewTree);
    }

    @Override
    protected void onCreate() {

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
}
