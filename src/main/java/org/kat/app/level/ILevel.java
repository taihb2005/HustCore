package org.kat.app.level;

public interface ILevel {
    void onLoad();
    void onCreate();
    void onBegin();
    void onFinish();
    default void onDestroy(){};

    String getMapPath();
    String getObjectJsonPath();
    String getEnemyJsonPath();
    default String getNPCJsonPath(){return null;}

    default int getMusicFile(){return -1;};

    void update();
    void setup();
    void dispose();
}
