package org.kat.app.ui;

import static org.kat.app.main.GamePanel.playSE;

public interface UIComponentListener {
    void onPress();

    default void onIncrease(){}
    default void onDecrease(){}
    default void onHover(){playSE(11);}
    default void onExit(){}
}
