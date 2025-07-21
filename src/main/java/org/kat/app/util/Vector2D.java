package org.kat.app.util;

public class Vector2D {
    public float x;
    public float y;

    public Vector2D(){
        this.x = 0;
        this.y = 0;
    }

    public Vector2D(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2D add(Vector2D v){
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2D subtract(Vector2D v){
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vector2D scale(float scalar){
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public float length() {
        return (float)Math.sqrt(x * x + y * y);
    }

    public Vector2D normalize() {
        float len = length();
        if (len != 0) {
            this.x /= len;
            this.y /= len;
            return this;
        }
        return clear();
    }

    public Vector2D clear(){
        this.x = 0;
        this.y = 0;
        return this;
    }

    public Vector2D copy() {
        return new Vector2D(x, y);
    }

    public Vector2D set(float x, float y){
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2D set(Vector2D v){
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
