package level;

import entity.player.Player;

import java.awt.*;

public class EventHandler {
    Level lvl;
    public EventHandler(Level lvl){
        this.lvl = lvl;
    }

    public void detectEvent(){
        lvl.canChangeMap = false;

        int newSolidAreaX1 = lvl.map.player.worldX + lvl.map.player.hitbox.x;
        int newSolidAreaY1 = lvl.map.player.worldY + lvl.map.player.hitbox.y;

        Rectangle tmp1 = new Rectangle(newSolidAreaX1 , newSolidAreaY1 , lvl.map.player.solidArea1.width , lvl.map.player.solidArea1.height);

        if(tmp1.intersects(lvl.eventRect)){
            lvl.canChangeMap = true;
        }

    }
}
