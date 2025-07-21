package org.kat.app.ui.views;

import org.kat.app.ui.Updatable;

import java.awt.*;

public class TextView extends View implements Updatable {
    private final Text text;

    public TextView(Text text, int x, int y, int width, int height){
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        text.attach(this);
    }

    @Override
    public void update(){
        text.attach(this);
    }

    @Override
    public void render(Graphics2D g2){
        text.render(g2);

        g2.drawRect(x, y, width, height);
    }
}
