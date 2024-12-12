package level.progress.level01;

import level.EventRectangle;
import level.Level;
import main.GamePanel;
import map.MapManager;
import map.MapParser;

import static main.GamePanel.playMusic;
import static main.GamePanel.stopMusic;

public class Level01 extends Level {
    final EventHandler01 eventHandler01 = new EventHandler01(this);
    public Level01(GamePanel gp){
        this.gp = gp;
        MapParser.loadMap( "map1" ,"/map/map1.tmx");
        map = MapManager.getGameMap("map1");
        map.gp = gp;
        init();
        setter.setFilePathObject("/level/level01/object_level01.json");
        setter.setFilePathEnemy("/level/level01/enemy_level01.json");
        setter.setFilePathNpc("/level/level01/npc_level01.json");
        setter.loadAll();
        stopMusic();
        playMusic(6);


        levelFinished = false;
        canChangeMap = false;

        changeMapEventRect1 = new EventRectangle(768 , 1888 , 128 , 32 , false);
    }

    public void updateProgress(){
        eventHandler01.update();
    }

    @Override
    public void dispose() {
        super.dispose();
        eventHandler01.dispose();
    }
}
