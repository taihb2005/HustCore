package org.kat.app.ui;

public interface UIComponentListener {
    void onPress();

    default void onIncrease(){}
    default void onDecrease(){}
    default void onHover(){}
    default void onExit(){}
}
