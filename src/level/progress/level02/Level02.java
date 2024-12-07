package level.progress;

import level.EventRectangle;
import level.Level;
import level.AssetSetter;
import level.level02.
import main.GamePanel;
import map.MapManager;
import map.MapParser;

public class Level02 extends Level {
    public Level02(GamePanel gp){
        this.gp = gp;
        MapParser.loadMap( "map_special" ,"res/map/map_special.tmx");
        map = MapManager.getGameMap("map_special");
        map.gp = gp;
        init();
        setter.setFilePathObject("res/level/level02/object_level02.json");
        setter.setFilePathNpc(null);
        setter.setFilePathEnemy("res/level/level02/enemy_level02.json");
        changeMapEventRect = new EventRectangle(0 , 0 , 0 , 0);

        eventHandler02 = new EventHandler02(this, "res/level/level02/enemy_level02.json");
        eventHandler02.setEnemy()

    }
}
