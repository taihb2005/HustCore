package util;

import entity.Player;

import static main.GamePanel.*;

public class Camera {
    private int x, y; // Top-left corner of the camera
    private int viewportWidth;
    private int viewportHeight; // Size of the viewport (visible area)
    private int mapWidth;
    private int mapHeight; // Size of the map

    public Camera(){
        this.x = 0;
        this.y = 0;
    }

    public Camera(int viewportWidth, int viewportHeight, int mapWidth, int mapHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.x = 0;
        this.y = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setCamera(int mapWidth , int mapHeight)
    {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public void setCamera(int viewportWidth , int viewportHeight , int mapWidth , int mapHeight)
    {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public void centerOn(int targetX, int targetY) {
        x = targetX - viewportWidth / 2 + 32;
        y = targetY - viewportHeight / 2 + 32;

        clamp();
    }

    private void clamp() {
        if (x < 0) x = 0;
        if (y < 0) y = 0;

        if (x + viewportWidth > mapWidth) x = mapWidth - viewportWidth;
        if (y + viewportHeight > mapHeight) y = mapHeight - viewportHeight;

    }

}
