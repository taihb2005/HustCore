package org.kat.app.ui.components;

import org.kat.app.ui.views.Cursor;
import org.kat.app.ui.views.Text;

import java.awt.*;

public class GameButton extends Button{
    public GameButton(){
        super();
    }

    public GameButton(Text text, int x, int y, int width, int height, int roundArc, boolean enabled) {
        super(enabled);

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.cursor = new Cursor();
        this.text = text;
        if(!enabled) text.setColor(DISABLED_COLOR_TEXT);
        this.roundArc = roundArc;

        text.attach(this);
    }

    @Override
    public void render(Graphics2D g2){
        if(currentState == ButtonState.DISABLE){
            g2.setColor(DISABLED_COLOR_BORDER);
        } else {
            g2.setColor(Color.WHITE);
        }
        g2.drawRoundRect(x, y, width, height, roundArc, roundArc);

        text.render(g2);
    }
}
