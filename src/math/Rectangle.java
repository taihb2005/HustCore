package math;

import java.awt.geom.Rectangle2D;

public class Rectangle {
    public float x;
    public float y;
    public float w;
    public float h;
    public Rectangle()
    {
        this.x = 0;
        this.y = 0;
        this.w = 0;
        this.h = 0;
    }

    public Rectangle(float x , float y , float w , float h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void setRect(float x , float y , float w , float h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public float getX(){return x;};
    public float getY(){return y;};
    public float getWidth(){return w;};
    public float getHeight(){return h;};
}
