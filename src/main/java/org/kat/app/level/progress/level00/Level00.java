package org.kat.app.level.progress.level00;

import org.kat.app.entity.npc.Npc_CorruptedHustStudent;
import org.kat.app.level.Level;
import org.kat.app.level.LevelState;
import org.kat.app.level.event.Event;
import org.kat.app.level.event.EventManager;
import org.kat.app.level.event.EventRectangle;
import org.kat.app.main.GamePanel;
import org.kat.app.main.GameState;
import org.kat.app.thread.LoadingService;

import java.util.List;

import static org.kat.app.main.GamePanel.*;

public class Level00 extends Level {
    public Level00(){
        super();
    }

    @Override
    public void onLoad(){

    }

    @Override
    public void onCreate() {
        setLevelState(LevelState.CUTSCENE);
        map.player.setGoal(834, 1854);
    }

    @Override
    public void onBegin() {
        eventMaster.submitDialogue(0);
        finishedBeginningDialogue = true;
    }

    @Override
    public void onFinish() {

    }

    @Override
    public String getMapPath() {
        return "/data/map/map0.tmx";
    }

    @Override
    public int getMusicFile(){
        return 6;
    }

    @Override
    public String getObjectJsonPath() {
        return "/data/level/level00/object_level00.json";
    }

    @Override
    public String getEnemyJsonPath() {
        return "/data/level/level00/enemy_level00.json";
    }

    @Override
    public String getNPCJsonPath() {
        return "/data/level/level00/npc_level00.json";
    }

    @Override
    public void setup(){
        levelFinished = false;
        eventManager = new EventManager();

        changeMapEventRect1 = new EventRectangle(1088 , 2280 , 64 , 32);

        eventMaster.setDialogueAt(0, 0,
                "Năm 2700, bạn nhận được nhiệm vụ giải cứu một đại học...");
        eventMaster.setDialogueAt(0, 1 ,
                "Nhưng ngay sau khi nhận nhiệm vụ bạn thấy mình nằm trong một căn phòng kì lạ cùng với một gã lạ mặt nào đó trông rất giống những người bạn đã từng gặp!");
        eventMaster.setDialogueAt(0, 2,
                "Thử đến nói chuyện xem sao!");
        eventMaster.buildDialogue();

        configureRoom("Room1",
                List.of(),
                List.of("DoorA002"),
                null,
                List.of(),
                List.of()
        );

        eventManager.register(new Event(
                () -> entityManager.get("NPC001", Npc_CorruptedHustStudent.class).hasTalkYet(),
                () -> getRoom("Room1").finish()
        ));

        eventManager.register(new Event(
                () -> changeMapEventRect1.isTriggered(map.player),
                () -> {
                    GamePanel.gameState = GameState.LOADING;
                    levelProgress++;
                    LoadingService.loadLevel();
                }
        ));
    }

    public void update(){
        eventManager.update();
    }

    @Override
    public void dispose() {
        super.dispose();

        if (eventManager != null) {
            eventManager.clear();
            eventManager = null;
        }

        changeMapEventRect1 = null;
    }

}
