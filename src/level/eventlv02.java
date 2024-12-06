package level;

import entity.mob.Mon_Cyborgon;
import map.GameMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EventLv02 {
    private final GameMap gameMap;
    private final List<Mon_Cyborgon> enemies;
    private boolean eventTriggered = false;

    public EventLv02(GameMap gameMap) {
        this.gameMap = gameMap;
        this.enemies = new ArrayList<>();
    }

    public void openDoor(int X, int Y) {
        if (!eventTriggered) {

            Mon_Cyborgon enemy1 = new Mon_Cyborgon(gameMap, X + 50, Y + 50);
            Mon_Cyborgon enemy2 = new Mon_Cyborgon(gameMap, X - 50, Y + 100);

            enemies.add(enemy1);
            enemies.add(enemy2);
            gameMap.addObject(enemy1,gameMap.enemy);
            gameMap.addObject(enemy2, gameMap.enemy);

            eventTriggered = true;
        }
    }

    public void update() {
        for (Mon_Cyborgon enemy : enemies) {
            if (!enemy.canbeDestroyed) {
                enemy.update();
            }
        }
    }

    public void render(Graphics2D g2) {
        for (Mon_Cyborgon enemy : enemies) {
            if (!enemy.canbeDestroyed) { // Nếu enemy chưa bị tiêu diệt
                enemy.render(g2);
            }
        }
    }
}
