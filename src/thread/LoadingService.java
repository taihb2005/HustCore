package thread;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import entity.mob.*;
import entity.npc.Npc_CorruptedHustStudent;
import entity.object.*;
import entity.player.Player;
import entity.projectile.*;
import graphics.AssetPool;
import level.LevelState;
import level.progress.dev_test.DevTestLevel;
import level.progress.level00.Level00;
import level.progress.level01.Level01;
import level.progress.level02.Level02;
import level.progress.level03.Level03;
import level.progress.level04.Level04;
import main.GameState;

import static main.GamePanel.*;

public class LoadingService {
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(1);

    public static void loadResource() {
        threadPool.submit(() -> {
            try {
                gameState = GameState.LOADING;

                AssetPool.loadAll();
                Player.loadPlayer();
                Obj_Door.load();
                Obj_Computer.load();
                Obj_Chest.load();
                Obj_Heart.load();
                Obj_PasswordAuth.load();
                Obj_Tank.load();
                Obj_Television.load();

                Npc_CorruptedHustStudent.load();

                Mon_Spectron.load();
                Mon_Shooter.load();
                Mon_HustGuardian.load();
                Mon_Cyborgon.load();
                Mon_Boss.load();

                Proj_BasicGreenProjectile.load();
                Proj_TrackingPlasma.load();
                Proj_BasicProjectile.load();
                Proj_ExplosivePlasma.load();
                Proj_Flame.load();
                Proj_GuardianProjectile.load();
                Proj_Plasma.load();

                System.out.println("Resources loaded.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public static void loadMap() {
        threadPool.submit(() -> {
            try {
                gameState = GameState.LOADING;

                dispose();

                //currentLevel = new DevTestLevel();
                switch(levelProgress){
                    case 0 : currentLevel = new Level00(); break;
                    case 1 : currentLevel = new Level01(); break;
                    case 2 : currentLevel = new Level02(); break;
                    case 3 : currentLevel = new Level03(); break;
                    case 4 : currentLevel = new Level04(); break;
                }

                assert currentLevel != null;
                currentMap = currentLevel.map;
                previousLevelProgress = levelProgress;

//                currentLevel.setLevelState(LevelState.CUTSCENE);
//                currentLevel.map.player.setGoal(768, 128);

                Thread.sleep(1000);

                gameState = GameState.PLAY;

                System.out.println("Map loaded.");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static void restart(){
        threadPool.submit(() -> {
            try {
                gameState = GameState.LOADING;

                dispose();

                //currentLevel = new DevTestLevel();
                switch(levelProgress){
                    case 0 : currentLevel = new Level00(); break;
                    case 1 : currentLevel = new Level01(); break;
                    case 2 : currentLevel = new Level02(); break;
                    case 3 : currentLevel = new Level03(); break;
                    case 4 : currentLevel = new Level04(); break;
                }
                //currentLevel = new DevTestLevel();
                currentMap = currentLevel.map;
                previousLevelProgress = levelProgress;
                currentLevel.map.player.resetValue();;
                Thread.sleep(1000);

                gameState = GameState.PLAY;


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static void dispose(){
        if (currentMap != null) {
            currentMap.dispose();
            currentMap = null;
            System.out.println("Map disposed!");
        }

        if(currentLevel != null){
            currentLevel.dispose();
            currentLevel = null;
            System.out.println("Level disposed!");
        }

        System.gc();
    }

    public static void shutdown() {
        threadPool.shutdown();
    }
}
