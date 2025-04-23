package level.progress.level04;

import entity.Entity;
import level.event.EventHandler;
import level.Level;
import main.GameState;

import java.util.TimerTask;

import static main.GamePanel.gameCompleted;
import static main.GamePanel.gameState;

public class EventHandler04 extends EventHandler {
    private boolean finishedEndingDialogue;
    public EventHandler04(Level lvl) {
        super(lvl);
        setEventRect();
    }

    private void setFirstDialogue() {
        eventMaster.dialogues[0][0] = new StringBuilder("Boss: Ngươi giỏi lắm mới đến \nđược đây");
        eventMaster.dialogues[0][1] = new StringBuilder("Boss: Ngắm gà khoả thân mau!");

        eventMaster.dialogues[1][0] = new StringBuilder("Boss: Ta sẽ còn quay lại.");
        eventMaster.dialogues[1][1] = new StringBuilder("Boss: Hãy đợi đấy!!!!!!!");
    }

    private void setEventRect(){
    }

    private void checkForBossDeath(){
        if(!finishedEndingDialogue) {
            for (int i = 0; i < lvl.map.enemy.length; i++) {
                if (lvl.map.enemy[i] != null && lvl.map.enemy[i].name.equals("Boss")) {
                    if (lvl.map.enemy[i].currentHP == 0) {
                        finishedEndingDialogue = true;
                        killAllEntity();
                        startingEndingDialogue();
                    }
                }
            }
        }
    }

    void killAllEntity(){
        for(int i = 0 ; i < lvl.map.enemy.length ; i++){
            if(lvl.map.enemy[i] != null) lvl.map.enemy[i].currentHP = 0;
        }
        for(int i = 0 ; i < lvl.map.projectiles.length ; i++){
            if(lvl.map.projectiles[i] != null) lvl.map.projectiles[i].currentHP = 0;
        }
    }

    void startingEndingDialogue(){
        eventMaster.submitDialogue(eventMaster , 1);
    }

    void startingDialogue(){
        TimerTask beginGameDialogue = new TimerTask() {
            //Con điên, sao k để chạy thoại của eventMastr luôn tách ra làm đ gì
            public void run() {
                setFirstDialogue();
                eventMaster.submitDialogue(eventMaster , 0);
                lvl.finishedBeginningDialogue = true;
            }
        };
        timer.schedule(beginGameDialogue , 800);
    }

    void spawnShooter(){
        for(Entity e : mp.activeObj)
            if(e != null && e.idName.equals("Door 7")){
                e.canbeDestroyed = true;
            }
    }

    public void update() {
        if(!lvl.finishedBeginningDialogue) startingDialogue();
        if( ((Level04) lvl).boss.isStage2) spawnShooter();
        checkForBossDeath();
        if(finishedEndingDialogue && gameState == GameState.PLAY) {
            gameCompleted = true;
        }
        if(gameCompleted) killAllEntity();
    }
}
