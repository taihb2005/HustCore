package level.progress.level03;

import entity.Entity;
import entity.npc.Npc_CorruptedHustStudent;
import entity.object.Obj_Door;
import level.EventHandler;
import level.EventRectangle;
import level.Level;
import main.GamePanel;

import java.util.TimerTask;

public class EventHandler03 extends EventHandler {
    private EventRectangle beginRoom1;
    private EventRectangle beginRoom2;
    private EventRectangle endRoom2;
    private EventRectangle beginRoom3;
    private EventRectangle beginRoom4;
    private final Entity[] eventEntity = new Entity[10];
    private int time = 0;

    public EventHandler03(Level lvl) {
        super(lvl);
        setFirstDialogue();
        setEventRect();
//        setEventEntity();
        time = 9999999;
    }

    private void setEventRect(){
        beginRoom1 = new EventRectangle(896 , 1408 , 128, 64 , true);
        beginRoom2 = new EventRectangle(320 , 1230 , 64 , 32 , true);
        endRoom2 = new EventRectangle(704 , 128 , 10 , 128 , true);
        beginRoom3 = new EventRectangle(1408 , 992 , 128 , 32 , true);
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
        eventMaster.dialogues[0][1] = "Boss: Chào mừng ngươi đến với tầng hầm đặc biệt của BK\n" +
                ", một khi ngươi bước vào thì gần như ngươi không thể thoát ra ngoài,\n" +
                " trừ khi ngươi có thể vượt qua các câu hỏi đặc biệt ở mỗi cửa ngươi bước vào\n";
        eventMaster.dialogues[0][2] = "Player: Haha! Ngươi đang trêu ngươi ta phải không?";
        eventMaster.dialogues[0][3] = "Boss: Không đơn giản như ngươi nghĩ đâu, căn phòng này được thiết kế đặc biệt,\n" +
                " ánh sáng càng ngày càng giảm sau 1 khoảng thời gian nhất định";
        eventMaster.dialogues[0][4] = "Boss: từ đó nếu ngươi không thể thoát khi ánh sáng còn, ngươi sẽ bị nhốt vĩnh viễn ở nơi này";
        eventMaster.dialogues[0][5] = "Boss: Vì vậy ta chúc ngươi may mắn đó, tên nhóc liều mạng của ta ….\n";
    }

    private void startSecondDialogue(){
        eventMaster.dialogues[1][0] = "Player: Sao mình cảm giác nặng nề vậy nhỉ?";
        eventMaster.dialogues[1][1] = "Boss: Haha, căn phòng này ta đã thiết kế đặc biệt, mọi hành động \n " +
                "kể cả tốc độ viên đạn của ngươi đều bị giảm đi,";
        eventMaster.dialogues[1][2] = "Boss:  hãy chuẩn bị bỏ mạng đi…\n";
        eventMaster.startDialogue(eventMaster,1);
        lvl.map.player.speed=1;
        lvl.map.player.projectile.speed = 1;
    }

    private void startThirdDialogue(){
        eventMaster.dialogues[2][0] = "Player: Ủa tại sao mình lại dừng \n" +
                "một chỗ vậy?";
        eventMaster.dialogues[2][1] = "Boss: Muahaha, ta đã bỏ một \n" +
                "quả bom dọc hành lang này, \n" +
                "người chỉ có 10 giây để chạy thôi \n" +
                "nhóc con của ta";
        eventMaster.dialogues[2][2] = "Player: Trời ơi… sắp tối thui \n" +
                "màn hình rồi còn bắt đọc hết đống thoại này...\n";
        eventMaster.startDialogue(eventMaster,2);
    }

    public void update() {
        if(!lvl.finishedBeginingDialogue) startingDialogue();
        else {
            if (time > 0) {
                time--;
                int currentRadius = time;
                // Cập nhật bán kính ánh sáng bằng cách gọi hàm blindFadein.
                GamePanel.environmentManager.lighting.setLightRadius(currentRadius);
            }
            else {
                GamePanel.environmentManager.lighting.setLightRadius(200);
                lvl.map.player.currentHP = 0;
            }
            if(!beginRoom2.eventFinished && triggerEvent(beginRoom2)) {
                System.out.println("jiafei");
                lvl.map.addObject(eventEntity[0] , lvl.map.activeObj);
                startSecondDialogue();
            }

            if(!endRoom2.eventFinished && triggerEvent(endRoom2)) {
                lvl.map.addObject(eventEntity[0] , lvl.map.activeObj);
                startThirdDialogue();
            }
        }
    }
}
