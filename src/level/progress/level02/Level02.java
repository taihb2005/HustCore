package level.progress.level02;

import entity.Entity;
import level.EventRectangle;
import level.Level;
import main.GamePanel;
import map.MapManager;
import map.MapParser;

public class Level02 extends Level {
    private Entity [] phase = new Entity[100];
    public Level02(GamePanel gp){
        this.gp = gp;
        MapParser.loadMap( "map1" ,"res/map/map1.tmx");
        map = MapManager.getGameMap("map1");
        map.gp = gp;
        init();
        changeMapEventRect = new EventRectangle(0 , 0 , 0 , 0);
    }
}
