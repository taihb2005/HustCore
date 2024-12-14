package level.progress.level00;

import level.EventRectangle;
import level.Level;
import main.GamePanel;
import map.MapManager;
import map.MapParser;

import static main.GamePanel.playMusic;
import static main.GamePanel.stopMusic;

public class Level00 extends Level {
    final EventHandler00 eventHandler00 = new EventHandler00(this);

    public Level00(GamePanel gp){
        this.gp = gp;
        MapParser.loadMap( "map0" ,"/map/map0.tmx");
        map = MapManager.getGameMap("map0");
        map.gp = gp;

        init();
        setter.setFilePathObject("/level/level00/object_level00.json");
        setter.setFilePathNpc("/level/level00/npc_level00.json");
        setter.setFilePathEnemy(null);
        setter.loadAll();
        stopMusic();
        playMusic(6);

        levelFinished = false;
        changeMapEventRect1 = new EventRectangle(1088 , 2280 , 64 , 32);
    }

    public void updateProgress(){
        eventHandler00.update();
    }

    @Override
    public void dispose() {
        super.dispose();
        eventHandler00.dispose();
        changeMapEventRect1 = null;
    }
}
