package entity.json_stat;
import java.util.List;

public class Enemystat {
    private String object;
    private String name;
    private int type;
    private Position position;
    private String direction;
    private boolean isAlwaysUp;
    private int attackCycle;

    // Getter và Setter
    public String getObject() { return object; }
    public void setObject(String object) { this.object = object; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public int getX() { return position.getX(); }
    public int getY() { return position.getY(); }

    public String getDirection() {return direction;}

    public boolean isAlwaysUp() {return isAlwaysUp;}

    public int getAttackCycle() {return attackCycle;}
}


