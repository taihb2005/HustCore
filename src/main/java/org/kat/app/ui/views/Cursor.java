package org.kat.app.ui.views;

import org.kat.app.ui.Updatable;
import org.kat.app.ui.components.Button;
import org.kat.app.util.GameTimer;

import java.awt.*;

public class Cursor extends Text implements Updatable {
    private static final Stroke DEFAULT_THICKNESS = new BasicStroke(7.0f);
    private static final int DEFAULT_ROUND_ARC = 30;
    private static final float BLINK_SPEED = 0.05f;

    protected Stroke thickness = DEFAULT_THICKNESS;
    protected int roundArc = DEFAULT_ROUND_ARC;
    protected boolean attachToParentOnce;

    private float alpha = 1.0f;
    private boolean fadingOut = true;
    private GameTimer blinkTimer;

    public Cursor() {
        super();

        attachToParentOnce = false;
        blinkTimer = new GameTimer (
                () -> {
                    if (fadingOut) {
                        alpha -= BLINK_SPEED;
                        if (alpha <= 0.2f) {
                            alpha = 0.2f;
                            fadingOut = false;
                        }
                    } else {
                        alpha += BLINK_SPEED;
                        if (alpha >= 1.0f) {
                            alpha = 1.0f;
                            fadingOut = true;
                        }
                    }
                }, 1, true
        );
    }

    public void setThickness(float thickness) {
        this.thickness = new BasicStroke(thickness);
    }

    @Override
    public void attach(View view){
        if(!attachToParentOnce) {
            super.attach(view);

            if (view instanceof Button button) {
                roundArc = button.getRoundArc();
            }
        }
    }

    public void hold(){
        attachToParentOnce = true;
    }

    public void release(){
        attachToParentOnce = false;
    }

    @Override
    public void update(){
        blinkTimer.update();
    }

    @Override
    public void render(Graphics2D g2){
        Stroke oldStroke = g2.getStroke();
        Composite oldComposite = g2.getComposite();

        g2.setStroke(thickness);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(color);

        g2.drawRoundRect(parentX, parentY, parentWidth, parentHeight, roundArc, roundArc);

        g2.setComposite(oldComposite);
        g2.setStroke(oldStroke);
    }
}
