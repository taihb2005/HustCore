package org.kat.app.level.progress.dev_test;

import org.kat.app.level.Level;
import org.kat.app.level.LevelState;
import org.kat.app.level.event.Event;
import org.kat.app.level.event.EventManager;
import org.kat.app.level.event.EventRectangle;

import java.util.List;

public class DevTestLevel extends Level {

    public DevTestLevel() {
        super();
    }

    @Override
    public void onLoad(){

    }

    @Override
    public void onCreate() {
        currentState = LevelState.RUNNING;
    }

    @Override
    public void onBegin() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public String getMapPath() {
        return "/data/map/map0.tmx";
    }

    @Override
    public String getObjectJsonPath() {
        return "/data/level/dev_test/object_dev.json";
    }

    @Override
    public String getEnemyJsonPath() {
        return "/data/level/dev_test/enemy_dev.json";
    }

    @Override
    public String getNPCJsonPath() {
        return "/data/level/dev_test/npc_dev.json";
    }

    @Override
    public int getMusicFile() {
        return 6;
    }

    public void update(){
        eventManager.update();
    }

    @Override
    public void dispose() {
        super.dispose();
        changeMapEventRect1 = null;
    }

    @Override
    public void setup(){
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
                () -> true,
                () -> {
                    getRoom("Room1").start();
                    System.out.println("Started");
                }
        ));

    }
}
