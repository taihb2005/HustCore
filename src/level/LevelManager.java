package level;

import main.GamePanel;

import java.util.Timer;
import java.util.TimerTask;

import static main.GamePanel.*;

public class LevelManager {
    GamePanel gp;
    Timer timer;
    private final int maxLevel = 4;
    public LevelManager(GamePanel gp){
        this.gp = gp;
        timer = new Timer();
    }
    public void update() {
        if (levelProgress == previousLevelProgress) {
            if (levelProgress == 0) levelProgress = 1;
            else if (levelProgress == 1) levelProgress = 2;
            else if (levelProgress == 2) levelProgress = 3;
            else if (levelProgress == 3) levelProgress = 4;
            currentMap.player.storeValue();
            gp.darker = true;
            TimerTask changeMapAnimation = new TimerTask() {
                @Override
                public void run() {
                    previousLevelProgress = levelProgress;
                    gp.loadMap();
                    gp.lighter = true;
                }
            };
            timer.schedule(changeMapAnimation , 1500);
        }
    }
}
