package entity.json_stat;

import java.util.List;

public class GameObject {
    private String object;
    private String name;
    private int type;
    private Position position;
    private String size;
    private String state; //Dành cho tv
    private int doorType; // Dành cho Obj_Door
    private List<ItemStat> items; // Dành cho Obj_Chest

    // Getter và Setter
    public String getObject() { return object; }
    public void setObject(String object) { this.object = object; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public void setSize(String size){this.size = size;}
    public String getSize(){return size;}

    public void setState(String state){this.state = state;}
    public String getState(){return state;}

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public int getDoorType() { return doorType; }
    public void setDoorType(int doorType) { this.doorType = doorType; }

    public List<ItemStat> getItems() { return items; }
    public void setItems(List<ItemStat> items) { this.items = items; }

    public int getX() { return position.getX(); }
    public int getY() { return position.getY(); }

    static class Position {
        private int x;
        private int y;

        public int getX() { return x; }
        public void setX(int x) { this.x = x; }

        public int getY() { return y; }
        public void setY(int y) { this.y = y; }
    }
}

