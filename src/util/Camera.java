package util;

import math.Rectangle;
import math.Vector2f;

import static main.GamePanel.currentMap;

import static main.GamePanel.windowWidth;
import static main.GamePanel.windowHeight;

public class Camera {

    private float targetX;
    private float targetY;

    private float posX;
    private float posY;

    public static Rectangle viewBox = new Rectangle(0 , 0 , windowWidth , windowHeight);

    public Camera(float targetX , float targetY)
    {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public void update()
    {
        try {
            viewBox.x = targetX - (float) windowWidth / 2;
            viewBox.y = targetY - (float) windowHeight / 2;

            if(viewBox.x < 0) viewBox.x = 0;
            if(viewBox.x > currentMap.getMapWidth() - viewBox.w) viewBox.x = currentMap.getMapWidth() - windowWidth;
            if(viewBox.y < 0) viewBox.y = 0;
            if(viewBox.y > currentMap.getMapHeight() - viewBox.h) viewBox.y = currentMap.getMapHeight() - viewBox.h;

            posX = viewBox.x;
            posY = viewBox.y;

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setposX(float posX){this.posX = posX;};
    public void setposY(float posY){this.posY = posY;};

    public float getposX(){return posX;};
    public float getposY(){return posY;};

}
