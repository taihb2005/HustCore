package org.kat.app.level.progress.level01;

import org.kat.app.entity.object.Obj_Door;
import org.kat.app.level.Level;
import org.kat.app.level.LevelState;
import org.kat.app.level.RoomTask;
import org.kat.app.level.event.Event;
import org.kat.app.level.event.EventManager;
import org.kat.app.level.event.EventRectangle;
import org.kat.app.main.GamePanel;
import org.kat.app.main.GameState;
import org.kat.app.thread.LoadingService;

import java.util.List;

import static org.kat.app.main.GamePanel.*;

public class Level01 extends Level {
    public Level01(){
        super();
    }

    @Override
    public void onLoad(){

    }

    @Override
    public void onCreate() {
        currentRoomTask = getNextRoomTask();
        setLevelState(LevelState.CUTSCENE);
        map.player.setGoal(768, 128);
    }

    @Override
    public void onBegin() {
        eventMaster.setDialogueAt(0, 0, "Player: Lại gặp một gã nữa...");
        eventMaster.setDialogueAt(0, 1, "Player: Đến nói chuyện xem sao!");

        eventMaster.setDialogueAt(1, 0, "Chúc mừng bạn đã hoàn thành thử thách đầu tiên!");
        eventMaster.setDialogueAt(1, 1, "Đi xuống căn phòng dưới để nhận thêm vật phẩm!");
        eventMaster.setDialogueAt(1, 2, "Sau đó hãy ra cửa phía nam để sang phòng khác!");
        eventMaster.buildDialogue();

        map.addObject(new Obj_Door(
                "big",
                "inactive",
                "Temporary Door",
                768, 0
        ), map.activeObj);

        eventMaster.submitDialogue(0);
    }

    @Override
    public void onFinish() {
        eventMaster.submitDialogue(1);
    }

    @Override
    public String getMapPath() {
        return "/data/map/map1.tmx";
    }

    @Override
    public String getObjectJsonPath() {
        return "/data/level/level01/object_level01.json";
    }

    @Override
    public String getEnemyJsonPath() {
        return "/data/level/level01/enemy_level01.json";
    }

    @Override
    public String getNPCJsonPath() {
        return "/data/level/level01/npc_level01.json";
    }

    @Override
    public int getMusicFile(){
        return 6;
    }

    @Override
    public void setup(){
        levelFinished = false;
        canChangeMap = false;

        changeMapEventRect1 = new EventRectangle(768 , 1888 , 128 , 32 , false);
        eventManager = new EventManager();

        configureRoom(
                "Room1",
                List.of("DoorA006"),
                List.of("DoorA007"),
                new EventRectangle(320, 903, 64, 32, true),
                List.of("Spectron011", "Spectron012"),
                new StringBuilder("Tiêu diệt 2 con Spectron!")
        );

        eventManager.register(new Event(
                () -> {
                    RoomTask currentRoom = getRoom("Room1");
                    return currentRoom.isPending() && currentRoom.getTriggerZone().isTriggered(map.player);
                },
                () -> getRoom("Room1").start()
        ));

        configureRoom(
                "Room2",
                List.of("DoorA007"),
                List.of("DoorA006", "DoorA007", "DoorA008"),
                new EventRectangle(320, 1230, 64, 32, true),
                List.of("Cyborgon013"),
                new StringBuilder("Tiêu diệt Cyborgon!")
        );

        eventManager.register(new Event(
                () -> {
                    RoomTask currentRoom = getRoom("Room2");
                    return currentRoom.isPending() && currentRoom.getTriggerZone().isTriggered(map.player);
                },
                () -> getRoom("Room2").start()
        ));

        configureRoom(
                "Room3",
                List.of("DoorA008"),
                List.of("DoorA008", "DoorA009", "DoorA005"),
                new EventRectangle(1408, 992, 128, 32, true),
                List.of("Shooter023", "Shooter024"),
                new StringBuilder("Tiêu diệt 2 trụ súng!")
        );

        eventManager.register(new Event(
                () -> {
                    RoomTask currentRoom = getRoom("Room3");
                    return currentRoom.isPending() && currentRoom.getTriggerZone().isTriggered(map.player);
                },
                () -> getRoom("Room3").start()
        ));

        configureRoomOrder(List.of(
                getRoom("Room1"),
                getRoom("Room2"),
                getRoom("Room3")
        ));

        eventManager.register((new Event(
                () -> isLevelFinished() && currentLevel == null,
                this::onFinish
        )));

        eventManager.register(new Event(
                () -> changeMapEventRect1.isTriggered(map.player),
                () -> {
                    GamePanel.gameState = GameState.LOADING;
                    levelProgress++;
                    LoadingService.loadLevel();
                    map.player.storeValue();
                }
        ));
    }

    public void update(){
        eventManager.update();
        if(!isLevelFinished()) {
            if (currentRoomTask.isRunning()) {
                currentRoomTask.update();
            } else if(currentRoomTask.isFinished()){
                currentRoomTask = getNextRoomTask();
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        //eventHandler01.dispose();
    }
}
