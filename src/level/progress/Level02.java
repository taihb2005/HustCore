package level.progress;

import level.EventRectangle;
import level.Level;
import main.GamePanel;
import map.MapManager;
import map.MapParser;

public class Level02 extends Level {
    public Level02(GamePanel gp){
        this.gp = gp;
        MapParser.loadMap( "map1" ,"res/map/map1.tmx");
        map = MapManager.getGameMap("map1");
        map.gp = gp;
        init();
        eventRect = new EventRectangle(0 , 0 , 0 , 0);
    }
}
