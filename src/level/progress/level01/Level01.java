package level.progress.level01;

import level.EventRectangle;
import level.Level;
import main.GamePanel;
import map.MapManager;
import map.MapParser;

public class Level01 extends Level {
    public Level01(GamePanel gp){
        this.gp = gp;
        MapParser.loadMap( "map1" ,"res/map/map1.tmx");
        map = MapManager.getGameMap("map1");
        map.gp = gp;
        init();
        setter.setFilePathObject("res/level/level01/object_level01.json");
        setter.setFilePathEnemy(null);
        setter.setFilePathNpc(null);
        setter.loadAll();

        changeMapEventRect = new EventRectangle(64 , 0 , 128 , 32);
    }
}
