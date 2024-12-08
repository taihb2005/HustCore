package level.progress.level02;

import entity.Entity;
import level.Level;
import level.EventHandler;
import main.GamePanel;
import main.GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.TimerTask;

import static main.KeyHandler.*;

public class EventHandler02 extends EventHandler {
    private int enemiesDefeated = 0;
    private final String correctPassword = "0156";
    private boolean showPasswordInput = false;
    private String enteredPassword = "";

    public EventHandler02(Level lvl) {
        super(lvl);
    }

    public void onEnemyDefeated() {
        for (Entity e : lvl.map.enemy) {
            if (e != null && e.canbeDestroyed) {
                enemiesDefeated++;
            }
        }
        if (enemiesDefeated == 1) {
            eventMaster.dialogues[0][0] = "Chữ số bạn nhận được: 0";
            eventMaster.startDialogue(eventMaster, 0);

//                } else if (enemiesDefeated == 10) {
//                    eventMaster.dialogues[0][0] = "Chữ số bạn nhận được: 1";
//                    eventMaster.startDialogue(eventMaster, 0);
            //               } else if (enemiesDefeated == 15) {
//                    eventMaster.dialogues[0][0] = "Chữ số bạn nhận được: 5";
//                    eventMaster.startDialogue(eventMaster, 0);
//                } else if (enemiesDefeated == 20) {
//                    eventMaster.dialogues[0][0] = "Chữ số bạn nhận được: 6";
//                    eventMaster.startDialogue(eventMaster, 0);
        }
    }


    public void update() {
        if(!showPasswordInput)onEnemyDefeated();
        if (enemiesDefeated == 1) {
            GamePanel.gameState = GameState.PASSWORD_STATE;
            handlePasswordPressed();
        }
    }
    public void render(Graphics2D g2){

    }

    public void handlePasswordPressed(){
        String charPressed = "";
        if(enterPressed){
            enterPressed = false;
            if (enteredPassword.equals(correctPassword)) {
                System.out.println("Mật khẩu đúng! Qua màn!");
                showPasswordInput = false;
            } else {
                System.out.println("Mật khẩu sai. Hãy thử lại!");
                GamePanel.gameState = GameState.PLAY_STATE;
                enteredPassword = "";
            }
        }
        if(key0pressed) {charPressed = "0"; key0pressed = false;} else
        if(key1pressed) {charPressed = "1"; key1pressed = false;} else
        if(key2pressed) {charPressed = "2"; key2pressed = false;} else
        if(key3pressed) {charPressed = "3"; key3pressed = false;} else
        if(key4pressed) {charPressed = "4"; key4pressed = false;} else
        if(key5pressed) {charPressed = "5"; key5pressed = false;} else
        if(key6pressed) {charPressed = "6"; key6pressed = false;} else
        if(key7pressed) {charPressed = "7"; key7pressed = false;} else
        if(key8pressed) {charPressed = "8"; key8pressed = false;} else
        if(key9pressed) {charPressed = "9"; key9pressed = false;}

        enteredPassword += charPressed;

        if(keyEscpressed) GamePanel.gameState = GameState.PLAY_STATE;
    }

}
