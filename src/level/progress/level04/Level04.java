package level.progress.level04;

import ai.PathFinder2;
import entity.mob.Mon_Boss;
import level.EventRectangle;
import level.Level;
import main.GamePanel;
import map.MapManager;
import map.MapParser;

import static main.GamePanel.*;

public class Level04 extends Level {
    public EventHandler04 eventHandler04;
    Mon_Boss boss;

    public Level04() {
        map = MapParser.loadMap( "/map/map4.tmx");
        eventHandler04 = new EventHandler04(this);
        init();
        setter.setFilePathObject("/level/level04/object_level04.json");
        setter.setFilePathEnemy("/level/level04/enemy_level04.json");
        setter.loadAll();
        stopMusic();
        playMusic(5);
        levelFinished = false;
        changeMapEventRect1 = new EventRectangle(0, 0, 0, 0);
        GamePanel.environmentManager.lighting.setLightRadius(1000);

        boss = map.boss;
    }
    public void updateProgress() {
        eventHandler04.update();
    }

    @Override
    public void dispose() {
        super.dispose();
        eventHandler04.dispose();
    }
}