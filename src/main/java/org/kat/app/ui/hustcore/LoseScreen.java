package org.kat.app.ui.hustcore;

import org.kat.app.ui.components.Button;
import org.kat.app.ui.views.Cursor;
import org.kat.app.ui.views.UIScreen;
import org.kat.app.ui.views.View;
import org.kat.app.util.Tree;

import java.util.List;

public class LoseScreen extends UIScreen {
    public LoseScreen(String id, Tree<View> viewTree) {
        super(id, viewTree);
    }

    @Override
    protected void onCreate() {

    }

    @Override
    public List<Button> getButtonList() {
        return List.of();
    }

    @Override
    public Cursor getCursor() {
        return null;
    }

    @Override
    public int getDefaultCursorPos() {
        return 0;
    }

    @Override
    public int getCurrentCursorPos() {
        return 0;
    }

    @Override
    public void setCursorPos(int pos) {

    }
}
