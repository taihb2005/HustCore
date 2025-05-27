package util;

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
        return new Vector2D(this.x + v.x, this.y + v.y);
    }

    public Vector2D subtract(Vector2D v){
        return new Vector2D(this.x - v.x, this.y - v.y);
    }

    public Vector2D scale(float scalar){
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    public float length() {
        return (float)Math.sqrt(x * x + y * y);
    }

    public Vector2D normalize() {
        float len = length();
        if (len != 0) {
            return new Vector2D(x / len, y / len);
        }
        return new Vector2D(0, 0);
    }

    public Vector2D copy() {
        return new Vector2D(x, y);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
