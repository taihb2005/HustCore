package level.progress.level01;

import entity.mob.Mon_EffectDealer;
import entity.object.Obj_Door;
import level.RoomTask;
import level.event.Event;
import level.event.EventManager;
import level.event.EventRectangle;
import level.Level;
import level.LevelState;
import map.MapParser;

import java.util.List;

import static main.GamePanel.*;

public class Level01 extends Level {
    public Level01(){
        map = MapParser.loadMap( "/map/map1.tmx");
        init();
        setter.setFilePathObject("/level/level01/object_level01.json");
        setter.setFilePathEnemy("/level/level01/enemy_level01.json");
        setter.setFilePathNpc("/level/level01/npc_level01.json");
        setter.loadAll();

        stopMusic();
        playMusic(6);

        setup();

        onCreateLevel = () -> {
            currentRoomTask = getNextRoomTask();
            setLevelState(LevelState.CUTSCENE);
            map.player.setGoal(768, 128);
        };

        onBeginLevel = () -> {
            eventMaster.dialogues[0][0] = new StringBuilder("Player: Lại gặp một gã nữa...");
            eventMaster.dialogues[0][1] = new StringBuilder("Player: Đến nói chuyện xem sao!");

            map.addObject(new Obj_Door(
                    "big",
                    "inactive",
                    "Temporary Door",
                    768, 0
            ), map.activeObj);

            eventMaster.startDialogue(eventMaster, 0);
        };

        onFinishLevel = () -> {
          eventMaster.dialogues[0][0] = new StringBuilder("Chúc mừng bạn đã hoàn thành\nthử thách đầu tiên!");
          eventMaster.dialogues[0][1] = new StringBuilder("Đi xuống căn phòng dưới để\nnhận thêm vật phẩm!");
          eventMaster.dialogues[0][2] = new StringBuilder("Sau đó hãy ra cửa phía nam\nđể sang phòng khác!");
          eventMaster.startDialogue(eventMaster, 0);
        };

        onCreateLevel.run();
    }

    public void update(){
        eventManager.update();
        if(!isLevelFinished()) {
            if (currentRoomTask.isRunning()) currentRoomTask.update();
        } else {
            currentRoomTask = getNextRoomTask();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        //eventHandler01.dispose();
    }

    private void setup(){
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
                () -> onFinishLevel.run()
        )));

    }
}
