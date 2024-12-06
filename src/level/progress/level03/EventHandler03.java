package level.progress.level03;

import entity.Entity;
import entity.npc.Npc_CorruptedHustStudent;
import entity.object.Obj_Door;
import level.EventHandler;
import level.Level;
import map.GameMap;

import java.util.TimerTask;
import static main.GamePanel.ui;

public class EventHandler03 extends EventHandler {
    private int dialogueIdx = 0;
    public EventHandler03(Level lvl) {
        super(lvl);
        setDialogue();
    }
    void checkForTutorialEvent(){
        for(Entity npc : lvl.map.npc){
            if(npc != null && npc.idName.equals("Chill Guy")){
                Npc_CorruptedHustStudent npc_tmp = (Npc_CorruptedHustStudent) npc;
                lvl.finishedTutorialDialogue = npc_tmp.hasTalkYet();
            }
        }
    }

    void openTutorialDoor(){
        for(Entity object: lvl.map.activeObj){
            if(object != null && (object.idName.equals("Begin DoorMap 01") || object.idName.equals("Begin DoorMap 02") || object.idName.equals("Begin DoorMap 03"))){
                Obj_Door door = (Obj_Door) object;
                door.canChangeState = true;
            }
        }
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

    private void setDialogue(){
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
}
