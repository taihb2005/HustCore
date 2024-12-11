package level.progress.level03;

import entity.Entity;
import level.EventRectangle;
import level.Level;
import main.GamePanel;
import map.MapManager;
import map.MapParser;

import java.awt.*;

import static main.GamePanel.playMusic;
import static main.GamePanel.stopMusic;

public class Level03 extends Level {
    public EventHandler03 eventHandler03;

    public Level03(GamePanel gp) {
        this.gp = gp;
        MapParser.loadMap("map3", "/map/map3.tmx");
        map = MapManager.getGameMap("map3");
        if (map == null) System.out.println("null3");
        map.gp = gp;
        eventHandler03 = new EventHandler03(this);
        init();
        setter.setFilePathObject("/level/level03/object_level03.json");
        setter.setFilePathEnemy("/level/level03/enemy_level03.json");
        setter.loadAll();
        stopMusic();
        playMusic(10);
        levelFinished = false;
        changeMapEventRect1 = new EventRectangle(1536, 1888, 128, 32);
    }
    public void updateProgress() {
        eventHandler03.update();
    }
    public void render(Graphics2D g2){eventHandler03.render(g2);}
}