package level.progress;

import level.EventRectangle;
import level.Level;
import level.AssetSetter;
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
        setter.setFilePathObject("res/level/level02/object_level02.json");
        setter.setFilePathNpc(null);
        setter.setFilePathEnemy("res/level/level02/enemy_level02.json");
        eventRect = new EventRectangle(0 , 0 , 0 , 0);

        setter.setEnemy();

    }
}
