package level.progress.level00;

import entity.npc.Npc_CorruptedHustStudent;
import level.event.Event;
import level.event.EventManager;
import level.event.EventRectangle;
import level.Level;
import level.LevelState;
import main.GamePanel;
import main.GameState;
import map.MapParser;
import thread.LoadingService;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static main.GamePanel.*;

public class Level00 extends Level {
    public Level00(){;
        map = MapParser.loadMap("/map/map0.tmx");

        init();
        setter.setFilePathObject("/level/level00/object_level00.json");
        setter.setFilePathNpc("/level/level00/npc_level00.json");
        setter.setFilePathEnemy("level/level00/enemy_level00.json");
        setter.loadAll();
        stopMusic();
        playMusic(6);

        setup();

        onCreateLevel = () -> {
            currentState = LevelState.RUNNING;
            TimerTask beginGameDialogue = new TimerTask() {
                @Override
                public void run() {
                    eventMaster.startDialogue(eventMaster , 0);
                    finishedBeginningDialogue = true;
                }
            };
            new Timer().schedule(beginGameDialogue , 800);
        };

        onCreateLevel.run();
    }

    public void update(){
        eventManager.update();
    }

    private void setup(){
        levelFinished = false;
        eventManager = new EventManager();

        changeMapEventRect1 = new EventRectangle(1088 , 2280 , 64 , 32);

        eventMaster.dialogues[0][0] = new StringBuilder("Năm 2700, bạn nhận được nhiệm vụ\ngiải cứu một đại học...");
        eventMaster.dialogues[0][1] = new StringBuilder("Nhưng ngay sau khi nhận nhiệm vụ\nbạn thấy mình nằm trong một căn\nphòng kì lạ!");
        eventMaster.dialogues[0][2] = new StringBuilder("...Cùng với một gã lạ mặt....");
        eventMaster.dialogues[0][3] = new StringBuilder("Thử đến nói chuyện xem sao.");

        configureRoom("Room1",
                List.of(),
                List.of("DoorA001", "DoorA002"),
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
                    LoadingService.loadMap();
                }
        ));
    }

    @Override
    public void dispose() {
        super.dispose(); // gọi hàm hủy từ lớp Level

        if (eventManager != null) {
            eventManager.clear(); // nếu bạn có hàm clear() trong EventManager
            eventManager = null;
        }

        changeMapEventRect1 = null;

        // Huỷ toàn bộ reference khác nếu cần
        onCreateLevel = null;
        onFinishLevel = null;
    }

}
