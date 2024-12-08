package level.progress.level02;

import entity.Entity;
import entity.object.Obj_Door;
import level.EventRectangle;
import level.Level;
import level.EventHandler;
import main.GamePanel;
import main.GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.TimerTask;


import static main.KeyHandler.*;

public class EventHandler02 extends EventHandler {
    private final int totalEnemy ;
    private int enemiesDefeated = 0;
    private boolean allEnemyDefeated;
    private boolean hasPopUpHint;
    private boolean isInRegion;
    private final EventRectangle showPasswordInput;


    public EventHandler02(Level lvl) {
        super(lvl);

        totalEnemy = 1;
        showPasswordInput = new EventRectangle(822 , 694 , 74 , 74 , false);

    }

    public void onDefeatEnemy(){
        for(Entity e : lvl.map.enemy){
            if(e != null && e.canbeDestroyed) enemiesDefeated++;
        }
        System.out.println(enemiesDefeated);
    }

    private void popUpPasswordHint(){
        if(enemiesDefeated % 5 == 0 && enemiesDefeated != 0 && !hasPopUpHint){
            eventMaster.dialogues[0][0] = "Gợi ý cho mật khẩu là: " + lvl.correctPassword.charAt(enemiesDefeated / 5 - 1);
            hasPopUpHint = true;
            eventMaster.startDialogue(eventMaster , 0);
        }
        if(enemiesDefeated % 5 != 0 && hasPopUpHint) hasPopUpHint = false;
    }

    public void checkForPasswordDoor(){
        if(enemiesDefeated == totalEnemy){
            allEnemyDefeated = true;
            for(Entity e : lvl.map.activeObj){
                if(e != null && e.idName != null){
                    if(e.idName.equals("Password Door")) {
                        Obj_Door door_tmp = (Obj_Door) e;
                        door_tmp.canChangeState = true;
                    }
                }
            }
        }
    }

    private void checkForCompletingLevel(){
        for(Entity e : lvl.map.activeObj) {
            if (e != null && e.idName.equals("EndDoor")) {
                Obj_Door tmp = (Obj_Door) e;
                tmp.canChangeState = true;
                break;
            }
        }
    }

    private void checkForShowPasswordInput(){
        if(triggerEvent(showPasswordInput) && !showPasswordInput.eventFinished && !isInRegion){
            isInRegion = true;
            GamePanel.gameState = GameState.PASSWORD_STATE;
            lvl.map.player.attackCanceled = true;
        }
        if(!triggerEvent(showPasswordInput)){
            isInRegion = false;
            showPasswordInput.eventFinished = false;
        }
    }

    public void update(){
        onDefeatEnemy();
        checkForPasswordDoor();
        if(!allEnemyDefeated) popUpPasswordHint();
        if(allEnemyDefeated) checkForShowPasswordInput();
        if(lvl.levelFinished) checkForCompletingLevel();
    }

    public void render(Graphics2D g2){

    }

}
