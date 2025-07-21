package org.kat.app.level.event;

import org.kat.app.entity.Entity;
import org.kat.app.level.Level;
import org.kat.app.map.GameMap;

import java.awt.*;
import java.util.Timer;

public class EventHandler {
    public GameMap mp;
    protected Entity eventMaster = new Entity();
    protected Level lvl;
    public Timer timer;
    public EventHandler(Level lvl){
        this.lvl = lvl;
        this.mp = lvl.map;
        timer = new Timer();
    }

    public boolean triggerEvent(EventRectangle e){
        lvl.canChangeMap = false;

        int newSolidAreaX1 = (int)lvl.map.player.position.x + lvl.map.player.solidArea1.x;
        int newSolidAreaY1 = (int)lvl.map.player.position.y + lvl.map.player.solidArea1.y;

        Rectangle tmp1 = new Rectangle(newSolidAreaX1 , newSolidAreaY1 , lvl.map.player.solidArea1.width , lvl.map.player.solidArea1.height);
        try {
            if (tmp1.intersects(e)) {
                if (e.oneTimeOnlyEvent) e.eventFinished = true;
                return true;
            }
        } catch(NullPointerException exception){
            return false;
        }
        return false;
    }

    public void dispose(){
        timer = null;
        eventMaster.dispose();
    }

    public void update(){};
    public void render(Graphics2D g2){};
}
