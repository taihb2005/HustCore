package entity.json_stat;

import entity.effect.Effect;

import java.util.List;

public class EnemyStat {
    private String enemy;
    private String name;
    private int type;
    private Position position;
    private Size size;
    private Effect effect;
    private DetectionRect detection;
    private String direction;
    private boolean isAlwaysUp;
    private int attackCycle;

    // Getter và Setter
    public String getEnemy() { return enemy; }
    public void setEnemy(String object) { this.enemy = object; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public DetectionRect getDetection(){return detection;}

    public int getX() { return position.getX(); }
    public int getY() { return position.getY(); }

    public int getSizeWidth() { return size.getWidth(); }
    public int getSizeHeight() { return size.getHeight(); }
    public void setSize(Size size) { this.size = size; }

    public String getDirection() {return direction;}

    public boolean isAlwaysUp() {return isAlwaysUp;}

    public int getAttackCycle() {return attackCycle;}

    public Effect getEffect(){return effect;}
    public static class Position {
        private int x;
        private int y;

        public int getX() { return x; }
        public void setX(int x) { this.x = x; }

        public int getY() { return y; }
        public void setY(int y) { this.y = y; }
    }
    public static class Size{
        private int width=64;
        private int height=64;
        public int getWidth(){return width;}
        public int getHeight(){return height;}
    }
    public static class DetectionRect{
        private int x;
        private int y;
        private int width;
        private int height;

        public int getX(){return x;}
        public int getY(){return y;}
        public int getWidth(){return width;}
        public int getHeight(){return height;}
    }
    public static class Effect{
        private String type;
        private int duration;

        public String getEffectType(){return type;}
        public int getDuration(){return duration;}
    }
}


