package org.kat.app.ui.components;

import org.kat.app.ui.views.Text;

import java.awt.*;

public class GameButton extends Button{
    public GameButton(){
        super();
    }

    public GameButton(Text text, int x, int y, int width, int height, int roundArc) {
        super();

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.roundArc = roundArc;

        text.attach(this);
    }

    @Override
    public void render(Graphics2D g2){
        g2.drawRoundRect(x, y, width, height, roundArc, roundArc);

        g2.setColor(Color.WHITE);
        text.render(g2);
    }
}
