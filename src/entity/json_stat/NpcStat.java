package entity.json_stat;

public class NpcStat {
    private String npc;
    private String name;
    private StringBuilder[][] dialogue;
    private String direction;
    private Position position;
    public String getNpc() {
        return npc;
    }

    public void setNpc(String npc) {
        this.npc = npc;
    }

    public StringBuilder[][] getDialogue() {
        return dialogue;
    }

    public String getName(){return name;}

    public void setDialogue(StringBuilder[][] dialogue) {
        this.dialogue = dialogue;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

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
