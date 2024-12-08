package level.progress.level03;

import entity.Entity;
import entity.effect.type.Stun;
import entity.npc.Npc_CorruptedHustStudent;
import entity.object.Obj_Door;
import level.EventHandler;
import level.EventRectangle;
import level.Level;
import main.GamePanel;
import main.GameState;

import java.util.TimerTask;

public class EventHandler03 extends EventHandler {
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
        beginRoom1 = new EventRectangle(896 , 1408 , 128, 64 , true);
        beginRoom2 = new EventRectangle(384 , 320 , 128 , 32 , true);
        endRoom2 = new EventRectangle(704 , 128 , 10 , 128 , true);
        beginRoom3 = new EventRectangle(1536 , 515 , 128 , 12 , true);
        endRoom3 = new EventRectangle(1536 , 900 , 128 , 64 , true);
        beginRoom4 = new EventRectangle(1536 , 1540 , 128 , 32 , true);
        quizArea = new EventRectangle(1472 , 1600 , 64 , 64 , true);
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

    private void setFirstDialogue(){
        eventMaster.dialogues[0][0] = "Player: Chuyện gì vậy?";
        eventMaster.dialogues[0][1] = "Boss: Chào mừng ngươi đến với\n" +
                "tầng hầm đặc biệt của BK.";
        eventMaster.dialogues[0][2] = "Boss: Một khi ngươi bước vào thì \n" +
                "gần như ngươi không thể thoát ra \n" +
                "ngoài..." ;
        eventMaster.dialogues[0][3] = "Boss: trừ khi ngươi có thể vượt \nqua các nhiệm vụ đặc biệt ở mỗi \ncửa ngươi bước vào!";
        eventMaster.dialogues[0][4] = "Player: Haha! Ngươi đang trêu \nngươi ta phải không?";
        eventMaster.dialogues[0][5] = "Boss: Không đơn giản như ngươi \nnghĩ đâu, căn phòng này được \nthiết kế đặc biệt.";
        eventMaster.dialogues[0][6] = "Boss: Ánh sáng càng ngày càng \ngiảm sau 1 khoảng thời gian \nnhất định";
        eventMaster.dialogues[0][7] = "Boss: từ đó nếu ngươi không thể\nthoát khi ánh sáng còn, ngươi sẽ \nbị nhốt vĩnh viễn ở nơi này";
        eventMaster.dialogues[0][8] = "Boss: Vì vậy ta chúc ngươi may \nmắn, tên nhóc liều mạng của ta ….\n";
    }

    private void startSecondDialogue(){
        eventMaster.dialogues[1][0] = "Player: Sao mình cảm giác nặng\nnề vậy nhỉ?";
        eventMaster.dialogues[1][1] = "Boss: Haha, căn phòng này ta đã \nthiết kế đặc biệt, mọi hành động\n" +
                "kể cả tốc độ viên đạn của ngươi \nđều bị giảm đi,";
        eventMaster.dialogues[1][2] = "Boss: hãy chuẩn bị bỏ mạng đi…\n";
        eventMaster.startDialogue(eventMaster,0);
        lvl.map.player.speed=1;
        lvl.map.player.projectile.speed = 1;
        lvl.map.player.projectile.maxHP = 150;
    }

    private void startThirdDialogue(){
        eventMaster.dialogues[2][0] = "Player: Ủa tại sao mình lại dừng \n" +
                "một chỗ vậy?";
        eventMaster.dialogues[2][1] = "Boss: Muahaha, ta đã bỏ một \n" +
                "quả bom dọc hành lang này, \n" +
                "người chỉ có 10 giây để chạy thôi \n" +
                "nhóc con của ta";
        eventMaster.dialogues[2][2] = "Player: Trời ơi… sắp tối thui \n" +
                "màn hình rồi còn bắt đọc hết đống\nthoại này...\n";
        eventMaster.startDialogue(eventMaster,0);
        lvl.map.player.speed=3;
        lvl.map.player.projectile.speed = 10;
        lvl.map.player.projectile.maxHP = 30;
    }

    private void startFourthDialogue(){
        eventMaster.dialogues[3][0] = "Boss: Ngươi khá lắm, nhưng mà \n" +
                "chưa xong đâu,";
        eventMaster.dialogues[3][1] = "Boss: căn phòng này ta đã thiết\nkế" +
                " để khi bước vào sau 4s ngươi\n" +
                "sẽ bị bất động trong 1s,";
        eventMaster.dialogues[3][2] = "Boss: đồng thời ngươi phải đối\nđầu với" +
                " những tên lính tinh nhuệ\nnhất " +
                "do ta tự tay chuẩn bị tặng\ncho ngươi hahaha…";
        eventMaster.startDialogue(eventMaster,0);
    }

    private void startFifthDialogue(){
        eventMaster.dialogues[4][0] = "Boss: Chào mừng ngươi đến với căn\nphòng cuối cùng của tầng hầm.\n";
        eventMaster.dialogues[4][1] = "Boss: chú ý ngươi chỉ có 1 lần trả lời duy nhất, nếu sai ngươi sẽ bỏ mạng tại đây:";
        eventMaster.dialogues[4][1] = "Boss: Hãy đến chiếc máy tính để\nnhận câu hỏi";
        eventMaster.startDialogue(eventMaster,0);
    }

    public void update() {
        if(!lvl.finishedBeginingDialogue) startingDialogue();
        else {
            if (time > 0) {
                time--;
                int currentRadius = (int) (time*0.4);
                if(lvl.map.player.effect.isEmpty())GamePanel.environmentManager.lighting.setLightRadius(currentRadius);
            }
            else {
                GamePanel.environmentManager.lighting.setLightRadius(200);
                lvl.map.player.currentHP = 0;
            }
            if(!beginRoom2.eventFinished && triggerEvent(beginRoom2)) {
                lvl.map.addObject(eventEntity[0] , lvl.map.activeObj);
                time+=1000;
                startSecondDialogue();
            }

            if(!endRoom2.eventFinished && triggerEvent(endRoom2)) {
                lvl.map.addObject(eventEntity[0] , lvl.map.activeObj);
                startThirdDialogue();
            }

            if(!beginRoom3.eventFinished && triggerEvent(beginRoom3)) {
                lvl.map.addObject(eventEntity[0] , lvl.map.activeObj);
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
                lvl.map.addObject(eventEntity[0] , lvl.map.activeObj);
                startFifthDialogue();

            }

            if(!quizArea.eventFinished && triggerEvent(quizArea)) {
                lvl.map.addObject(eventEntity[0] , lvl.map.activeObj);
                GamePanel.gameState = GameState.QUIZ_STATE;
            }
        }
    }
}
