package level.progress.level03;

import entity.Entity;
import entity.effect.type.Stun;
import level.event.EventHandler;
import level.event.EventRectangle;
import level.Level;
import main.GamePanel;
import main.GameState;

import java.util.TimerTask;

import static main.GamePanel.*;

public class EventHandler03 extends EventHandler {
    private EventRectangle beginRoom2;
    private EventRectangle endRoom2;

    private EventRectangle beginRoom3;
    private boolean inRoom3 = false;
    private EventRectangle endRoom3;

    private EventRectangle beginRoom4;

    private EventRectangle quizArea;

    private EventRectangle completeArea;
    private final Entity[] eventEntity = new Entity[10];
    public static int time = 0;

    private Stun temp;

    public EventHandler03(Level lvl) {
        super(lvl);
        setFirstDialogue();
        setEventRect();
        temp = new Stun(lvl.map.player);
        time = 2000;
//        setEventEntity();
    }

    private void setEventRect(){
        beginRoom2 = new EventRectangle(384 , 320 , 128 , 32 , true);
        endRoom2 = new EventRectangle(704 , 128 , 10 , 128 , true);
        beginRoom3 = new EventRectangle(1536 , 515 , 128 , 12 , true);
        endRoom3 = new EventRectangle(1536 , 960 , 128 , 64 , true);
        beginRoom4 = new EventRectangle(1536 , 1540 , 128 , 32 , true);
        quizArea = new EventRectangle(1472 , 1600 , 64 , 64 , true);
        completeArea = new EventRectangle(1536 , 1900 , 128 , 64 , true);
    }

    void startingDialogue(){
        TimerTask beginGameDialogue = new TimerTask() {
            //Con điên, sao k để chạy thoại của eventMastr luôn tách ra làm đ gì
            public void run() {
                eventMaster.startDialogue(eventMaster , 0);
                lvl.finishedBeginningDialogue = true;
            }
        };
        timer.schedule(beginGameDialogue , 800);
    }

    private void setFirstDialogue(){
        eventMaster.dialogues[0][0] = new StringBuilder("Player: Chuyện gì vậy?");
        eventMaster.dialogues[0][1] = new StringBuilder("Boss: Chào mừng ngươi đến với\n" +
                "tầng hầm đặc biệt của BK.");
        eventMaster.dialogues[0][2] = new StringBuilder("Boss: Một khi ngươi bước vào thì \n" +
                "gần như ngươi không thể thoát ra \n" +
                "ngoài...");
        eventMaster.dialogues[0][3] = new StringBuilder("Boss: trừ khi ngươi có thể vượt \nqua các nhiệm vụ đặc biệt ở mỗi \ncửa ngươi bước vào!");
        eventMaster.dialogues[0][4] = new StringBuilder("Player: Haha! Ngươi đang trêu \nngươi ta phải không?");
        eventMaster.dialogues[0][5] = new StringBuilder("Boss: Không đơn giản như ngươi \nnghĩ đâu, căn phòng này được \nthiết kế đặc biệt.");
        eventMaster.dialogues[0][6] = new StringBuilder("Boss: Ánh sáng càng ngày càng \ngiảm sau 1 khoảng thời gian \nnhất định.");
        eventMaster.dialogues[0][7] = new StringBuilder("Boss: từ đó nếu ngươi không thể\nthoát khi ánh sáng còn, ngươi sẽ \nbị nhốt vĩnh viễn ở nơi này.");
        eventMaster.dialogues[0][8] = new StringBuilder("Boss: Vì vậy ta chúc ngươi may \nmắn, tên nhóc liều mạng của ta ….\n");
    }

    private void startSecondDialogue(){
        eventMaster.dialogues[0] = null;

        eventMaster.dialogues[1][0] = new StringBuilder("Player: Sao mình cảm giác nặng\nnề vậy nhỉ?");
        eventMaster.dialogues[1][1] = new StringBuilder("Boss: Haha, căn phòng này ta đã \nthiết kế đặc biệt, mọi hành động\n" +
                "kể cả tốc độ viên đạn của ngươi \nđều bị giảm đi.");
        eventMaster.dialogues[1][2] = new StringBuilder("Boss: hãy chuẩn bị bỏ mạng đi…\n");
        eventMaster.startDialogue(eventMaster,1);
        lvl.map.player.speed=1;
        lvl.map.player.lastSpeed =1;
        lvl.map.player.projectile.speed = 1;
        lvl.map.player.projectile.maxHP = 150;
    }

    private void startThirdDialogue(){
        eventMaster.dialogues[1] = null;

        eventMaster.dialogues[2][0] = new StringBuilder("Player: Ủa tại sao mình lại dừng \n" +
                "một chỗ vậy?");
        eventMaster.dialogues[2][1] = new StringBuilder("Boss: Muahaha, ta đã bỏ một " +
                "quả\nbom dọc hành lang này " +
                "ngươi chỉ\n" +
                "có 10 giây để chạy thôi " +
                "nhóc con\ncủa ta.");
        eventMaster.dialogues[2][2] = new StringBuilder("Player: Trời ơi… sắp tối thui \n" +
                "màn hình rồi còn bắt đọc hết đống\nthoại này...\n");
        eventMaster.startDialogue(eventMaster,2);
        lvl.map.player.speed=3;
        lvl.map.player.lastSpeed =3;
        lvl.map.player.projectile.speed = 10;
        lvl.map.player.projectile.maxHP = 30;
    }

    private void startFourthDialogue(){
        eventMaster.dialogues[2] = null;

        eventMaster.dialogues[3][0] = new StringBuilder("Boss: Ngươi khá lắm, nhưng mà \n" +
                "chưa xong đâu.");
        eventMaster.dialogues[3][1] = new StringBuilder("Boss: căn phòng này ta đã thiết\nkế" +
                " để khi bước vào sau 4s ngươi\n" +
                "sẽ bị bất động trong 1s.");
        eventMaster.dialogues[3][2] = new StringBuilder("Boss: đồng thời ngươi phải đối\nđầu với" +
                " những tên lính tinh nhuệ\nnhất " +
                "do ta tự tay chuẩn bị tặng\ncho ngươi hahaha…");
        eventMaster.startDialogue(eventMaster,3);
    }

    private void startFifthDialogue(){
        eventMaster.dialogues[3] = null;

        eventMaster.dialogues[4][0] = new StringBuilder("Boss: Chào mừng ngươi đến với căn\nphòng cuối cùng của tầng hầm.\n");
        eventMaster.dialogues[4][1] = new StringBuilder("Boss: chú ý ngươi chỉ có 1 lần\ntrả lời duy nhất, nếu sai ngươi\nsẽ bỏ mạng tại đây.");
        eventMaster.dialogues[4][2] = new StringBuilder("Boss: Hãy đến chiếc máy tính để\nnhận câu hỏi.");
        eventMaster.startDialogue(eventMaster,4);
    }

    public void update() {
        if(!lvl.finishedBeginningDialogue) startingDialogue();
        else {
            if (time > 0) {
                time--;
                int currentRadius = (int) (time*0.4);
                //if(lvl.map.player.effect.isEmpty())GamePanel.environmentManager.lighting.setLightRadius(currentRadius);
            }
            else {
                //GamePanel.environmentManager.lighting.setLightRadius(200);
                lvl.map.player.currentHP = 0;
            }
            if(!beginRoom2.eventFinished && triggerEvent(beginRoom2)) {
                time+=1000;
                startSecondDialogue();
            }

            if(!endRoom2.eventFinished && triggerEvent(endRoom2)) {
                startThirdDialogue();
            }

            if(!beginRoom3.eventFinished && triggerEvent(beginRoom3)) {
                time+=1000;
                startFourthDialogue();
                inRoom3 = true;
            }

            if(inRoom3) {
                if (time % 500 == 400) {
                    temp.add();
                    temp.affect();
                }
                if (time % 500 == 0) {
                    temp.remove();
                }
            }

            if(!endRoom3.eventFinished && triggerEvent(endRoom3)) {
                inRoom3 = false;
                lvl.map.player.effect.removeIf(effect -> effect.effectFinished);
            }

            if(!beginRoom4.eventFinished && triggerEvent(beginRoom4)) {
                time+=1000;
                startFifthDialogue();

            }

            if(!quizArea.eventFinished && triggerEvent(quizArea) && GamePanel.ui.selectedOption == -1) {
                eventMaster.dialogues[4] = null;
                System.gc();
                GamePanel.gameState = GameState.QUIZ;
                stopMusic();
                playMusic(7);
            }

            else if (GamePanel.ui.selectedOption == 5) lvl.map.player.currentHP = 0;
            else if (!completeArea.eventFinished && triggerEvent(completeArea) && GamePanel.ui.selectedOption == 4) {
                lvl.map.player.storeValue();
                lvl.canChangeMap = triggerEvent(lvl.changeMapEventRect1);
                }
            }
            if(lvl.canChangeMap){
                lvl.levelFinished = true;
                levelProgress++;
            }
        }

}
