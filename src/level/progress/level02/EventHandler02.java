package level.progress.level02;

import entity.Entity;
import entity.object.Obj_Door;
import level.event.EventRectangle;
import level.Level;
import level.event.EventHandler;
import main.GamePanel;
import main.GameState;

import java.awt.*;
import java.util.ArrayList;


import static main.UI.joystix;

public class EventHandler02 extends EventHandler {
    private final int totalEnemy ;
    private int enemiesDefeated = 0;
    private boolean allEnemyDefeated;
    private boolean hasPopUpHint;
    private boolean isInRegion;
    private EventRectangle showPasswordInput;
    ArrayList<String> hint = new ArrayList<>();


    public EventHandler02(Level lvl) {
        super(lvl);

        totalEnemy = 20;
        showPasswordInput = new EventRectangle(822 , 694 , 74 , 74 , false);

    }

    public void onDefeatEnemy(){
        for(Entity e : lvl.map.enemy){
            if(e != null && e.canbeDestroyed) enemiesDefeated++;
        }
    }

    private void popUpPasswordHint(){
        if(enemiesDefeated % 5 == 0 && enemiesDefeated != 0 && !hasPopUpHint){
            eventMaster.dialogues[0][0] = new StringBuilder("Gợi ý cho mật khẩu là: " + lvl.correctPassword.charAt(enemiesDefeated / 5 - 1));
            hint.add(String.valueOf(lvl.correctPassword.charAt(enemiesDefeated / 5 - 1)));
            hasPopUpHint = true;
            eventMaster.startDialogue(eventMaster , 0);
        }
        if(enemiesDefeated % 5 != 0) hasPopUpHint = false;
    }

    public void checkForPasswordDoor(){
        if(enemiesDefeated == totalEnemy){
            allEnemyDefeated = true;
            for(Entity e : lvl.map.activeObj){
                if(e != null && e.idName != null){
                    if(e.idName.equals("Password Door")) {
                        Obj_Door door_tmp = (Obj_Door) e;
                        door_tmp.canChangeStatus = true;
                    }
                }
            }
        }
    }

    private void checkForCompletingLevel(){
        for(Entity e : lvl.map.activeObj) {
            if (e != null && e.idName.equals("EndDoor")) {
                Obj_Door tmp = (Obj_Door) e;
                tmp.canChangeStatus = true;
            }
        }
        if(triggerEvent(lvl.changeMapEventRect1) || triggerEvent(lvl.changeMapEventRect2)) lvl.canChangeMap = true;
    }

    private void checkForShowPasswordInput(){
        if(triggerEvent(showPasswordInput) && !showPasswordInput.eventFinished && !isInRegion){
            isInRegion = true;
            GamePanel.gameState = GameState.PASSWORD;
            lvl.enteredPassword = "";
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
        popUpPasswordHint();
        if(allEnemyDefeated) checkForShowPasswordInput();
        if(lvl.levelFinished) {
            checkForCompletingLevel();
            lvl.map.player.storeValue();
        }
    }

    public void render(Graphics2D g2){
        int lineHeight = 20;
        g2.setFont(joystix.deriveFont(Font.PLAIN, 19));
        g2.setColor(Color.white);
        g2.drawString("Còn lại:" + (totalEnemy - enemiesDefeated) , 10 , 20);
        g2.setFont(joystix.deriveFont(Font.ITALIC, 14));
        g2.setColor(Color.RED);
        for(int i = 0 ; i < hint.size() ; i++){
            g2.drawString("Gợi ý:" + hint.get(i) , 10 , 60 + lineHeight * i);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        hint.clear();
    }
}
