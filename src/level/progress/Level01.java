package level.progress;

import ai.PathFinder;
import environment.EnvironmentManager;
import level.AssetSetter;
import level.EventRectangle;
import level.Level;
import main.GamePanel;
import main.UI;
import map.MapManager;
import map.MapParser;

import static main.GamePanel.*;

public class Level01 extends Level {
    public Level01(GamePanel gp){
        this.gp = gp;
        MapParser.loadMap( "map3" ,"res/map/map3.tmx");
        map = MapManager.getGameMap("map3");
        map.gp = gp;
        init();

        eventRect = new EventRectangle(64 , 0 , 128 , 32);
    }

}
