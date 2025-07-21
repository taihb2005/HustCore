package org.kat.app.ui.views;

import org.kat.app.ui.Updatable;
import org.kat.app.ui.components.Button;

import java.awt.*;

public class Cursor extends Text implements Updatable {
    private static final Stroke DEFAULT_THICKNESS = new BasicStroke(5.0f);
    private static final int DEFAULT_ROUND_ARC = 30;

    private Stroke thickness = DEFAULT_THICKNESS;
    private int roundArc = DEFAULT_ROUND_ARC;

    public Cursor() {
        super();
    }

    public void setThickness(float thickness) {
        this.thickness = new BasicStroke(thickness);
    }

    @Override
    public void attach(View view){
        super.attach(view);

        if(view instanceof Button button){
            roundArc = button.getRoundArc();
        }
    }

    @Override
    public void update(){


    }

    @Override
    public void render(Graphics2D g2){
        Stroke oldStroke = g2.getStroke();

        g2.setColor(color);
        g2.setStroke(thickness);

        g2.drawRoundRect(parentX, parentY, parentWidth, parentHeight, roundArc, roundArc);

        g2.setStroke(oldStroke);
    }
}
