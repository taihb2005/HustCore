package entity;

import java.util.*;
import entity.items.Item;

public class GameObject {
    private String object;
    private String name;
    private int type;
    private Position position;
    private List<Item> items;

    public String getObject() {
        return object;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getX() { return position.x; }
    public int getY() { return position.y; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}

class Position {
    public int x;
    public int y;

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}