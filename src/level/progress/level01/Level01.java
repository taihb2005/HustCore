package level.progress.level01;

import level.EventRectangle;
import level.Level;
import level.progress.level00.EventHandler00;
import main.GamePanel;
import map.MapManager;
import map.MapParser;

public class Level01 extends Level {
    final EventHandler01 eventHandler01 = new EventHandler01(this);
    public Level01(GamePanel gp){
        this.gp = gp;
        MapParser.loadMap( "map1" ,"res/map/map1.tmx");
        map = MapManager.getGameMap("map1");
        map.gp = gp;
        init();
        setter.setFilePathObject("res/level/level01/object_level01.json");
        setter.setFilePathEnemy("res/level/level01/enemy_level01.json");
        setter.setFilePathNpc(null);
        setter.loadAll();

        levelFinished = false;
        canChangeMap = false;

        changeMapEventRect = new EventRectangle(768 , 1856 , 128 , 32 , true);
    }

    public void updateProgress(){
        eventHandler01.update();
    }
}
