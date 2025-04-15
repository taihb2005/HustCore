package thread;

import entity.mob.Mon_Cyborgon;
import entity.mob.Mon_HustGuardian;
import entity.mob.Mon_Shooter;
import entity.mob.Mon_Spectron;
import entity.object.*;
import entity.player.Player;
import graphics.AssetPool;
import main.GameState;

import java.io.IOException;

import static main.GamePanel.gameState;

public class LoadResourceThread extends LoadThread {
    public void run(){
        try{
            gameState = GameState.LOADING_STATE;
            semaphore.acquire();
            AssetPool.loadAll();
            Player.loadPlayer();
            Obj_Door.load();
            Obj_Computer.load();
            Obj_Chest.load();
            Obj_Heart.load();
            Obj_PasswordAuth.load();
            Obj_Tank.load();
            Obj_Television.load();

            Mon_Spectron.load();
            Mon_Shooter.load();
            Mon_HustGuardian.load();
            Mon_Cyborgon.load();
            semaphore.release();
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
