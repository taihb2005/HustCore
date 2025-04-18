package ui.manager;

import java.awt.*;

import static main.GamePanel.windowWidth;

public class BaseUI {
    protected Graphics2D g2;
    public int getXForCenteredText(String text)
    {
        int length = (int)g2.getFontMetrics().getStringBounds(text , g2).getWidth();
        return windowWidth / 2 - length / 2;
    }

    public void render(Graphics2D g2){

    };

    public void update(boolean keyPressed, boolean keyReleased){

    }
}
