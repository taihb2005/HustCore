package org.kat.app.ui.views;

import java.awt.*;

public class SubWindow extends View{
    private static final Color DEFAULT_BACKGROUND_COLOR = new Color(0,0,0, 178);
    private static final Stroke DEFAULT_STROKE = new BasicStroke(5);

    public SubWindow(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(Graphics2D g2) {
        Color c = DEFAULT_BACKGROUND_COLOR;
        Stroke oldStroke = g2.getStroke();
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = Color.WHITE;
        g2.setColor(c);
        g2.setStroke(DEFAULT_STROKE);
        g2.drawRoundRect(x, y, width, height, 25, 25);

        g2.setStroke(oldStroke);
    }
}
