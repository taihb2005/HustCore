package level.progress.dev_test;

import level.EventRectangle;
import level.Level;
import level.progress.level00.EventHandler00;
import main.GamePanel;
import map.MapManager;
import map.MapParser;

import static main.GamePanel.playMusic;
import static main.GamePanel.stopMusic;

public class DevTestLevel extends Level {
    final EventHandler00 eventHandler00 = new EventHandler00(this);

    public DevTestLevel(){;
        map = MapParser.loadMap("/map/map0.tmx");
        //map.gp = gp;

        init();
        setter.setFilePathObject("/level/dev_test/enemy_dev.json");
        setter.setFilePathObject("/level/dev_test/object_dev.json");
        setter.setFilePathEnemy("/level/dev_test/enemy_dev.json");
        setter.setFilePathNpc(null);
        setter.loadAll();
        stopMusic();
        playMusic(6);

        levelFinished = false;
        changeMapEventRect1 = new EventRectangle(1088 , 2280 , 64 , 32);
    }

    public void updateProgress(){
        //eventHandler00.update();
    }

    @Override
    public void dispose() {
        super.dispose();
        eventHandler00.dispose();
        changeMapEventRect1 = null;
    }
}
