package org.kat.app.ui.components;

import org.kat.app.ui.views.*;
import org.kat.app.ui.views.Cursor;

import java.awt.*;

public class ValueAdjuster extends Button {
    private int lowerThreshold = -1;
    private int upperThreshold = -1;
    private int currentValue;
    private final SubWindow textHolder;

    public ValueAdjuster(Text text, int x, int y, int width, int height) {
        super();

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;

        textHolder = new SubWindow(x, y, width, height);
        this.text.attach(textHolder);
    }

    public ValueAdjuster(Text text, int x, int y, int width, int height, int defaultValue, int lowerThreshold, int upperThreshold) {
        super();

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.cursor = new AdjustableCursor();
        this.text = text;
        this.currentValue = defaultValue;
        this.lowerThreshold = lowerThreshold;
        this.upperThreshold = upperThreshold;

        textHolder = new SubWindow(x, y, width, height);
        this.text.attach(textHolder);
    }

    public int getLowerThreshold() {
        return lowerThreshold;
    }

    public int getUpperThreshold() {
        return upperThreshold;
    }

    public int getValue(){
        return currentValue;
    }

    public void setValue(int value) {
        currentValue = value;
        setText(String.valueOf(currentValue));
    }

    @Override
    public void update(){
        super.update();
        if(upperThreshold != -1 && lowerThreshold != -1){
            if(currentValue >= upperThreshold){
                ((AdjustableCursor) getCursor()).getIncreaseTextView().hide();
            } else {
                ((AdjustableCursor) getCursor()).getIncreaseTextView().show();
            }

            if(currentValue <= lowerThreshold){
                ((AdjustableCursor) getCursor()).getDecreaseTextView().hide();
            } else {
                ((AdjustableCursor) getCursor()).getDecreaseTextView().show();
            }
        }
    }

    @Override
    public void render(Graphics2D g2){
        textHolder.render(g2);
        text.render(g2);
    }
}
