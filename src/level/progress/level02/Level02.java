package level.progress.level02;

import level.EventRectangle;
import level.Level;
import main.GamePanel;
import map.MapManager;
import map.MapParser;

import java.awt.*;
import java.util.Random;

import static main.GamePanel.playMusic;
import static main.GamePanel.stopMusic;

public class Level02 extends Level {
    private EventHandler02 eventHandler02;

    public Level02(GamePanel gp) {
        this.gp = gp;
        MapParser.loadMap("map2", "/map/map2.tmx");
        map = MapManager.getGameMap("map2");
        map.gp = gp;
        init();
        setter.setFilePathObject("/level/level02/object_level02.json");
        setter.setFilePathNpc("/level/level02/npc_level02.json");
        setter.setFilePathEnemy("/level/level02/enemy_level02.json");
        setter.loadAll();
        eventHandler02 = new EventHandler02(this);
        stopMusic();
        playMusic(6);

        changeMapEventRect1 = new EventRectangle(192, 0, 128, 32 , true);
        changeMapEventRect2 = new EventRectangle(1280 , 0 , 120 , 9 , true);
        passwordGenerator();
        enteredPassword = "";
    }

    private void passwordGenerator(){
        Random random = new Random();
        StringBuilder correctPassword_tmp = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            int nextChar = random.nextInt(10);
            correctPassword_tmp.append(nextChar);
        }
        correctPassword = correctPassword_tmp.toString();

    }

    public void updateProgress(){
       eventHandler02.update();
    }
    public void render(Graphics2D g2){eventHandler02.render(g2);}

    @Override
    public void dispose() {
        super.dispose();
        eventHandler02.dispose();
        changeMapEventRect1 = null;
        changeMapEventRect2 = null;
    }
}
