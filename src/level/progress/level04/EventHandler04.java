package level.progress.level04;

import entity.Entity;
import entity.effect.type.Stun;
import level.EventHandler;
import level.EventRectangle;
import level.Level;
import main.GamePanel;
import main.GameState;

import java.util.TimerTask;

public class EventHandler04 extends EventHandler {
    private EventRectangle beginRoom1;

    private EventRectangle beginRoom2;
    private EventRectangle endRoom2;

    private EventRectangle beginRoom3;
    private boolean inRoom3 = false;
    private EventRectangle endRoom3;

    private EventRectangle beginRoom4;

    private EventRectangle quizArea;
    private final Entity[] eventEntity = new Entity[10];
    public static int time = 0;


    public EventHandler04(Level lvl) {
        super(lvl);
        setFirstDialogue();
        setEventRect();
//        setEventEntity();
    }

    private void setFirstDialogue() {
        eventMaster.dialogues[0][0] = "Boss: Ngươi giỏi lắm mới đến \nđược đây";
        eventMaster.dialogues[0][1] = "Boss: Ngắm gà khoả thân mau!";
    }

    private void setEventRect(){
    }

    void startingDialogue(){
        TimerTask beginGameDialogue = new TimerTask() {
            //Con điên, sao k để chạy thoại của eventMastr luôn tách ra làm đ gì
            public void run() {
                eventMaster.startDialogue(eventMaster , 0);
                lvl.finishedBeginingDialogue = true;
            }
        };
        timer.schedule(beginGameDialogue , 800);
    }


    public void update() {
        if(!lvl.finishedBeginingDialogue) startingDialogue();

    }
}
