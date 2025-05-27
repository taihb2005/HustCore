package level.progress.level03;

import entity.object.Obj_Chest;
import entity.object.Obj_Door;
import level.LevelState;
import level.RoomTask;
import level.event.EventManager;
import level.event.EventRectangle;
import level.Level;
import main.GamePanel;
import main.GameState;
import map.MapParser;
import level.event.Event;
import thread.LoadingService;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;

import static main.GamePanel.*;

public class Level03 extends Level {
    private int remainingTime;
    private Runnable onEnterFirstRoom;
    private boolean enterFirstRoom = false;

    public Level03() {
        map = MapParser.loadMap("/map/map3.tmx");
        init();
        setter.setFilePathObject("/level/level03/object_level03.json");
        setter.setFilePathEnemy("/level/level03/enemy_level03.json");
        setter.setFilePathNpc("/level/level03/npc_level03.json");
        setter.loadAll();

        setup();

        levelFinished = false;

        onCreateLevel = () -> {
            stopMusic();
            playMusic(6);
            currentRoomTask = getNextRoomTask();
            setLevelState(LevelState.CUTSCENE);
            map.player.setGoal(926, 1608);
        };

        onBeginLevel = () -> {
            map.addObject(new Obj_Door(
                    "big",
                    "inactive",
                    "Temporary",
                    896, 1856
            ), map.activeObj);

            eventMaster.dialogues[0][0] = new StringBuilder("Player: Chuyện gì vậy?");
            eventMaster.dialogues[0][1] = new StringBuilder("Boss: Chào mừng ngươi đến với\n" +
                    "tầng hầm đặc biệt của BK.");
            eventMaster.dialogues[0][2] = new StringBuilder("Boss: Một khi ngươi bước vào thì\n" +
                    "gần như ngươi không thể thoát ra \n" +
                    "ngoài...");
            eventMaster.dialogues[0][3] = new StringBuilder("Boss: trừ khi ngươi có thể vượt \nqua các nhiệm vụ đặc biệt ở mỗi \ncửa ngươi bước vào!");
            eventMaster.dialogues[0][4] = new StringBuilder("Player: Haha! Ngươi đang trêu \nngươi ta phải không?");
            eventMaster.dialogues[0][5] = new StringBuilder("Boss: Không đơn giản như ngươi \nnghĩ đâu, căn phòng này được \nthiết kế đặc biệt.");
            eventMaster.dialogues[0][6] = new StringBuilder("Boss: Ánh sáng càng ngày càng \ngiảm sau 1 khoảng thời gian \nnhất định.");
            eventMaster.dialogues[0][7] = new StringBuilder("Boss: từ đó nếu ngươi không thể\nthoát khi ánh sáng còn, ngươi sẽ \nbị nhốt vĩnh viễn ở nơi này.");
            eventMaster.dialogues[0][8] = new StringBuilder("Boss: Vì vậy ta chúc ngươi may \nmắn, tên nhóc liều mạng của ta ….\n");

            eventMaster.submitDialogue(eventMaster, 0);
        };

        onFinishLevel = () -> {
            eventMaster.dialogues[0][0] = new StringBuilder("Bạn đã hoàn thành thử thách\nthứ ba");
            eventMaster.dialogues[0][1] = new StringBuilder("Hãy đến cửa phía Nam để\ntiếp tục!");

            eventMaster.submitDialogue(eventMaster, 0);
            map.player.getEnvironmentManager().lighting.transit = true;
            map.player.getEnvironmentManager().lighting.fadeOut = true;
        };

        onCreateLevel.run();
    }
    public void update() {
        eventManager.update();
        if(enterFirstRoom) onEnterFirstRoom.run();
    }
    public void render(Graphics2D g2){}

    public void dispose() {
        super.dispose();
    }

    private void setup(){
        levelFinished = false;
        canChangeMap = false;
        changeMapEventRect1 = new EventRectangle(1536 , 1888 , 128 , 32 , false);

        eventManager = new EventManager();
        remainingTime = 5000;

        EventRectangle panicModeRect_1 = new EventRectangle(704, 1974, 128, 10, false);
        EventRectangle panicModeRect_2 = new EventRectangle(1088, 1974, 128, 10,false);
        EventRectangle panicModeRect_3 = new EventRectangle(320, 1974, 128, 10, false);

        onEnterFirstRoom = () -> {
            remainingTime--;
            int r = (int) (remainingTime * 0.4f);
            map.player.getEnvironmentManager().lighting.setLightRadius(r);

            if(remainingTime <= 0){
                map.player.kill();
            }
        };
        configureRoom(
                "Room1",
                List.of("DoorA001"),
                List.of(),
                new EventRectangle(896, 1408, 128, 1, true),
                new EventRectangle(896, 1024, 128, 64, true),
                List.of()
                );

        eventManager.register(new Event(
                () -> {
                    RoomTask currentRoom = getRoom("Room1");
                    return currentRoom.isPending() && currentRoom.getTriggerZone().isTriggered(map.player);
                },
                () -> {
                    enterFirstRoom = true;
                    getRoom("Room1").start();
                }
        ));

        eventManager.register(new Event(
                () -> {
                    RoomTask currentRoom = getRoom("Room1");
                    return currentRoom.isRunning() && currentRoom.getFinishZone().isTriggered(map.player);
                },
                () -> getRoom("Room1").finish()
        ));

        configureRoom(
                "Room2",
                List.of(),
                List.of(),
                new EventRectangle(896, 960, 128, 1, true),
                new EventRectangle(384, 512, 128, 128, true),
                List.of()
        );

        eventManager.register(new Event(
                () -> {
                    RoomTask currentRoom = getRoom("Room2");
                    return currentRoom.isPending() && currentRoom.getTriggerZone().isTriggered(map.player);
                },
                () -> getRoom("Room2").start()
        ));

        eventManager.register(new Event(
                () -> {
                    RoomTask currentRoom = getRoom("Room2");
                    return currentRoom.isRunning() && currentRoom.getFinishZone().isTriggered(map.player);
                },
                () -> getRoom("Room2").finish()
        ));

        configureRoom(
                "Room3",
                List.of(),
                List.of(),
                new EventRectangle(384, 320, 128, 1,true),
                new EventRectangle(1536, 448, 128, 64, true),
                List.of()
        );

        eventManager.register(new Event(
                () -> {
                    RoomTask currentRoom = getRoom("Room3");
                    return currentRoom.isPending() && currentRoom.getTriggerZone().isTriggered(map.player);
                },
                () -> getRoom("Room3").start()
        ));

        eventManager.register(new Event(
                () -> {
                    RoomTask currentRoom = getRoom("Room3");
                    return currentRoom.isRunning() && currentRoom.getFinishZone().isTriggered(map.player);
                },
                () -> getRoom("Room3").finish()
        ));

        configureRoom(
                "Room4",
                List.of(),
                List.of(),
                new EventRectangle(1536, 578, 128, 1,true),
                new EventRectangle(1536, 768, 128, 64, true),
                List.of()
        );

        eventManager.register(new Event(
                () -> {
                    RoomTask currentRoom = getRoom("Room4");
                    return currentRoom.isPending() && currentRoom.getTriggerZone().isTriggered(map.player);
                },
                () -> getRoom("Room4").start()
        ));

        eventManager.register(new Event(
                () -> {
                    RoomTask currentRoom = getRoom("Room4");
                    return currentRoom.isRunning() && currentRoom.getFinishZone().isTriggered(map.player);
                },
                () -> getRoom("Room4").finish()
        ));

        configureRoom(
                "Room5",
                List.of(),
                List.of(),
                new EventRectangle(1536, 1600, 128, 1,true),
                new EventRectangle(1536, 1792, 128, 64, true),
                List.of()
        );

        eventManager.register(new Event(
                () -> {
                    RoomTask currentRoom = getRoom("Room5");
                    return currentRoom.isPending() && currentRoom.getTriggerZone().isTriggered(map.player);
                },
                () -> getRoom("Room5").start()
        ));

        eventManager.register(new Event(
                () -> {
                    RoomTask currentRoom = getRoom("Room5");
                    return currentRoom.isRunning() && currentRoom.getFinishZone().isTriggered(map.player);
                },
                () -> {
                    getRoom("Room5").finish();
                    onFinishLevel.run();
                }
        ));

        configureRoomOrder(List.of(
                getRoom("Room1"),
                getRoom("Room2"),
                getRoom("Room3"),
                getRoom("Room4"),
                getRoom("Room5")
        ));

        eventManager.register( new Event(
                () -> panicModeRect_1.isTriggered(map.player) || panicModeRect_2.isTriggered(map.player),
                () -> {
                    map.player.setPosition(336, 1856);
                    map.player.setConfusedMode(true);
                },
                true
        ));

        eventManager.register( new Event(
                () -> panicModeRect_3.isTriggered(map.player),
                () -> {
                    map.player.setPosition(926, 1608);
                    map.player.setConfusedMode(false);
                },
                true
        ));

        eventManager.register(new Event(
                () -> entityManager.get("ChestA002", Obj_Chest.class).isOpened(),
                () -> entityManager.get("DoorA001", Obj_Door.class).activate()
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
}