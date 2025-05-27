package level.progress.level00;

import level.event.EventHandler;
import level.Level;
import main.GamePanel;
import main.GameState;
import thread.LoadingService;

import java.util.TimerTask;

import static main.GamePanel.levelProgress;

public class EventHandler00 extends EventHandler {
    public boolean finishedBeginningDialogue = false;
    public boolean finishedTutorialDialogue = false;
    public EventHandler00(Level lvl) {
        super(lvl);
        setDialogue();
    }
    void checkForTutorialEvent(){
        //Npc_CorruptedHustStudent npc = (Npc_CorruptedHustStudent) mp.findEntityById("NPC001");
        //finishedTutorialDialogue = npc.hasTalkYet();
    }

    void openTutorialDoor(){
       //((Obj_Door) mp.findEntityById("DoorA001")).activate();
        //((Obj_Door) mp.findEntityById("DoorA002")).activate();
    }

    void startingDialogue(){
        TimerTask beginGameDialogue = new TimerTask() {
            @Override
            public void run() {
                eventMaster.submitDialogue(eventMaster , 0);
                finishedBeginningDialogue = true;
            }
        };
        timer.schedule(beginGameDialogue , 800);
    }

    private void setDialogue(){
        eventMaster.dialogues[0][0] = new StringBuilder("Năm 2700, bạn nhận được nhiệm vụ\ngiải cứu một đại học...");
        eventMaster.dialogues[0][1] = new StringBuilder("Nhưng ngay sau khi nhận nhiệm vụ\nbạn thấy mình nằm trong một căn\nphòng kì lạ!");
        eventMaster.dialogues[0][2] = new StringBuilder("...Cùng với một gã lạ mặt....");
        eventMaster.dialogues[0][3] = new StringBuilder("Thử đến nói chuyện xem sao.");
    }

    public void update(){
        checkForTutorialEvent();
        if(!finishedBeginningDialogue) startingDialogue();
        if(finishedTutorialDialogue) openTutorialDoor();
        lvl.canChangeMap = triggerEvent(lvl.changeMapEventRect1);
        if(lvl.canChangeMap){
            lvl.levelFinished = true;
            GamePanel.gameState = GameState.LOADING;
            levelProgress++;
            LoadingService.loadMap();
        }
    }

}
