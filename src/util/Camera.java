package util;

import java.util.Random;

public class Camera {
    private int x, y;
    private int viewportWidth;
    private int viewportHeight;
    private int mapWidth;
    private int mapHeight;

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

    public void cameraShake(int targetX , int targetY){
        Random amplitude_generator = new Random();
        int offsetX = -2 + amplitude_generator.nextInt(5);
        int offsetY = -2 + amplitude_generator.nextInt(5);

        x = targetX - viewportWidth / 2 + 32 + offsetX;
        y = targetY - viewportHeight / 2 + 32 + offsetY;

        clamp();
    }

}
