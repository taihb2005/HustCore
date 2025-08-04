package org.kat.app.entity;

public interface Actable {
    default void move(){};
    default void set(){};
    default void talk(){};
    default void attack(){};
    default void loot(){};
}
