package org.kat.app.level.progress.level03;

import org.kat.app.entity.object.Obj_Chest;
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

import java.awt.*;
import java.util.List;

import static org.kat.app.main.GamePanel.*;

public class Level03 extends Level {
    private int remainingTime;
    private Runnable onEnterFirstRoom;
    private boolean enterFirstRoom = false;

    public Level03() {
        super();
    }

    @Override
    public void onLoad(){

    }

    @Override
    public void onCreate() {
        currentRoomTask = getNextRoomTask();
        setLevelState(LevelState.CUTSCENE);
        map.player.setGoal(926, 1608);
    }

    @Override
    public void onBegin() {
        map.addObject(new Obj_Door(
                "big",
                "inactive",
                "Temporary",
                896, 1856
        ), map.activeObj);

        eventMaster.setDialogueAt(0, 0, "Player: Chuyện gì vậy?");
        eventMaster.setDialogueAt(0, 1, "Boss: Chào mừng ngươi đến với tầng hầm đặc biệt của BK.");
        eventMaster.setDialogueAt(0, 2, "Boss: Một khi ngươi bước vào thì gần như ngươi không thể thoát ra ngoài...");
        eventMaster.setDialogueAt(0, 3, "Boss: trừ khi ngươi có thể vượt qua các nhiệm vụ đặc biệt ở mỗi cửa ngươi bước vào!");
        eventMaster.setDialogueAt(0, 4, "Player: Haha! Ngươi đang trêu ngươi ta phải không?");
        eventMaster.setDialogueAt(0, 5, "Boss: Không đơn giản như ngươi nghĩ đâu, căn phòng này được thiết kế đặc biệt.");
        eventMaster.setDialogueAt(0, 6, "Boss: Ánh sáng càng ngày càng giảm sau 1 khoảng thời gian nhất định.");
        eventMaster.setDialogueAt(0, 7, "Boss: từ đó nếu ngươi không thể thoát khi ánh sáng còn, ngươi sẽ bị nhốt vĩnh viễn ở nơi này.");
        eventMaster.setDialogueAt(0, 8, "Boss: Vì vậy ta chúc ngươi may mắn, tên nhóc liều mạng của ta ….");

        eventMaster.setDialogueAt(1, 0, "Bạn đã hoàn thành thử thách thứ ba");
        eventMaster.setDialogueAt(1, 1, "Hãy đến cửa phía Nam để tiếp tục!");
        eventMaster.buildDialogue();

        eventMaster.submitDialogue(0);
    }

    @Override
    public void onFinish() {
        onEnterFirstRoom = null;
        eventMaster.submitDialogue(1);
        map.player.getEnvironmentManager().lighting.transit = true;
        map.player.getEnvironmentManager().lighting.fadeOut = true;
    }

    @Override
    public String getMapPath() {
        return "/data/map/map3.tmx";
    }

    @Override
    public String getObjectJsonPath() {
        return "/data/level/level03/object_level03.json";
    }

    @Override
    public String getEnemyJsonPath() {
        return "/data/level/level03/enemy_level03.json";
    }

    @Override
    public String getNPCJsonPath() {
        return "/data/level/level03/npc_level03.json";
    }

    @Override
    public int getMusicFile() {
        return 6;
    }

    public void update() {
        eventManager.update();
        if(enterFirstRoom && onEnterFirstRoom != null) onEnterFirstRoom.run();
    }

    public void dispose() {
        super.dispose();
    }

    @Override
    public void setup(){
        levelFinished = false;
        canChangeMap = false;
        changeMapEventRect1 = new EventRectangle(1536 , 1888 , 128 , 32 , false);

        eventManager = new EventManager();
        remainingTime = 4000;

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
                    onFinish();
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
                    LoadingService.loadLevel();
                    map.player.storeValue();
                }
        ));
    }
}