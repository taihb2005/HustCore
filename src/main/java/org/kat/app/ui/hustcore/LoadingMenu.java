package org.kat.app.ui.hustcore;

import org.kat.app.ui.components.Button;
import org.kat.app.ui.views.Cursor;
import org.kat.app.ui.views.TextView;
import org.kat.app.ui.views.UIScreen;
import org.kat.app.ui.views.View;
import org.kat.app.util.GameTimer;
import org.kat.app.util.Tree;

import java.util.List;

public class LoadingMenu extends UIScreen {
    private final GameTimer timer;
    private int dotCounts;
    private final TextView loadingTextView;
    private final String loadingText;

    public LoadingMenu(String id, Tree<View> viewTree) {
        super(id, viewTree);
        dotCounts = 0;
        loadingTextView = (TextView) findViewById("loadingText");
        loadingText = loadingTextView.getText().getText();
        timer = new GameTimer(
                () -> {
                    dotCounts = (dotCounts + 1) % 4;
                    loadingTextView.setText(loadingText + ".".repeat(dotCounts));
                },
        30, true);
    }

    @Override
    public void update(){
        timer.update();
    }

    @Override
    protected void onCreate() {

    }

    @Override
    public List<Button> getButtonList() {
        return null;
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
