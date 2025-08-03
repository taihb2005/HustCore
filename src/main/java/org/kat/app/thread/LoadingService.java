package org.kat.app.thread;

import org.kat.app.entity.mob.*;
import org.kat.app.entity.npc.Npc_CorruptedHustStudent;
import org.kat.app.entity.object.*;
import org.kat.app.entity.player.Player;
import org.kat.app.entity.projectile.*;
import org.kat.app.graphics.AssetPool;
import org.kat.app.level.progress.level00.Level00;
import org.kat.app.level.progress.level01.Level01;
import org.kat.app.level.progress.level02.Level02;
import org.kat.app.level.progress.level03.Level03;
import org.kat.app.level.progress.level04.Level04;
import org.kat.app.main.GameState;
import org.kat.app.main.UI;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.kat.app.main.GamePanel.*;

public class LoadingService {
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(1);

    public static void loadResource() {
        threadPool.submit(() -> {
            try {
                UI._UIManager.setCurrentScreen("loading_menu");
                
                AssetPool.loadAll(AssetPool.SPRITE_FOLDER);
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


    public static void loadLevel() {
        threadPool.submit(() -> {
            try {
//                UI._UIManager.clearFromScreenStack();
//                UI._UIManager.setCurrentScreen("loading_menu");
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

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void restart(){
        threadPool.submit(() -> {
            try {
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

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void dispose(){
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

        ui.dispose();

        System.gc();
    }

    public static void shutdown() {
        AssetPool.dispose();
        threadPool.shutdown();
    }
}
