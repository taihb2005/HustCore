package org.kat.app.ui.components;

import org.kat.app.ui.views.Text;
import org.kat.app.ui.views.TextView;

public class ValueAdjuster extends TextView {
    private Button increaseButton;
    private Button decreaseButton;

    public ValueAdjuster(Text text, int x, int y, int width, int height) {
        super(text, x, y, width, height);
    }
}
