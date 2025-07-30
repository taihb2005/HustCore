package org.kat.app.ui.views;

import org.kat.app.util.GameTimer;

import java.awt.*;

public class AdjustableCursor extends Cursor{
    private final TextView increase;
    private final TextView decrease;
    private GameTimer timer;
    private int sign;

    private int baseIncreaseX;
    private int baseDecreaseY;

    public AdjustableCursor(){
        attachToParentOnce = false;

        sign = -1;
        increase = new TextView(new Text(">"));
        decrease = new TextView(new Text("<"));

        increase.getText().setFontSize(19);
        decrease.getText().setFontSize(19);

        timer = new GameTimer(
                () -> {
                    sign *= -1;
                    int offsetX = 5 * sign;

                    increase.setDimensions(baseIncreaseX - offsetX, parentY, 20, parentHeight);
                    decrease.setDimensions(baseDecreaseY + offsetX, parentY, 20, parentHeight);
                },
                30, true
        );
    }

    public TextView getIncreaseTextView(){
        return increase;
    }

    public TextView getDecreaseTextView(){
        return decrease;
    }

    @Override
    public void attach(View view){
        if(!attachToParentOnce) {
            super.attach(view);

            increase.setDimensions(parentX + parentWidth + 20, parentY, 20, parentHeight);
            decrease.setDimensions(parentX - 40, parentY, 20, parentHeight);

            baseIncreaseX = parentX + parentWidth + 20;
            baseDecreaseY = parentX - 40;

        }
    }

    @Override
    public void update(){
        timer.update();
    }

    @Override
    public void render(Graphics2D g2){
        super.render(g2);
        if(increase.isVisible()) increase.render(g2);
        if(decrease.isVisible()) decrease.render(g2);
    }
}
