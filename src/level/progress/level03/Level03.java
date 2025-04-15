package level.progress.level03;

import entity.Entity;
import level.EventRectangle;
import level.Level;
import main.GamePanel;
import map.MapManager;
import map.MapParser;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import static main.GamePanel.playMusic;
import static main.GamePanel.stopMusic;

public class Level03 extends Level {
    public EventHandler03 eventHandler03;

    public Level03() {
        map = MapParser.loadMap("/map/map3.tmx");
        eventHandler03 = new EventHandler03(this);
        init();
        setter.setFilePathObject("/level/level03/object_level03.json");
        setter.setFilePathEnemy("/level/level03/enemy_level03.json");
        setter.loadAll();
        levelFinished = false;
        changeMapEventRect1 = new EventRectangle(1536, 1888, 128, 32);
        Timer timer = new Timer();

        TimerTask play = new TimerTask() {
            @Override
            public void run() {
                stopMusic();
                stopMusic();
                playMusic(6);
            }
        };
        timer.schedule(play , 200);
    }
    public void updateProgress() {
        eventHandler03.update();
    }
    public void render(Graphics2D g2){eventHandler03.render(g2);}

    public void dispose() {
        super.dispose();
        eventHandler03.dispose();
    }
}