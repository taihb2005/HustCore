package thread;

import level.progress.dev_test.DevTestLevel;
import level.progress.level00.Level00;
import level.progress.level01.Level01;
import level.progress.level02.Level02;
import level.progress.level03.Level03;
import level.progress.level04.Level04;
import main.GamePanel;
import main.GameState;

import static main.GamePanel.*;

public class LoadMapThread extends LoadThread{
    public void run(){
        try {
            gameState = GameState.LOADING_STATE;
            semaphore.acquire();
            if(currentMap != null) {
                currentMap.dispose();
                currentMap = null;
            }
            loadMap();
            Thread.sleep(1000);
            semaphore.release();
            GamePanel.gameState = GameState.PLAY_STATE;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static void loadMap()
    {
        boolean loadSuccess = false;
        if(currentLevel != null) {
            currentLevel.dispose();
            System.gc();
        }
        currentLevel = new DevTestLevel();
//        switch(levelProgress){
//            case 0 : currentLevel = new Level00(); break;
//            case 1 : currentLevel = new Level01(); break;
//            case 2 : currentLevel = new Level02(); break;
//            case 3 : currentLevel = new Level03(); break;
//            case 4 : currentLevel = new Level04(); break;
//        }
        currentMap = currentLevel.map;
        //previousLevelProgress = levelProgress;
    }
}
