package level;

import entity.Entity;

import java.util.*;
import java.awt.*;

public class EventHandler {
    protected Entity eventMaster = new Entity();
    protected Level lvl;
    public Timer timer;
    public EventHandler(Level lvl){
        this.lvl = lvl;
        timer = new Timer();
    }

    public void detectForMapChange(){
        lvl.canChangeMap = false;

        int newSolidAreaX1 = lvl.map.player.worldX + lvl.map.player.solidArea1.x;
        int newSolidAreaY1 = lvl.map.player.worldY + lvl.map.player.solidArea1.y;

        Rectangle tmp1 = new Rectangle(newSolidAreaX1 , newSolidAreaY1 , lvl.map.player.solidArea1.width , lvl.map.player.solidArea1.height);
        if(tmp1.intersects(lvl.changeMapEventRect)){
            lvl.canChangeMap = true;
        }

    }
}
