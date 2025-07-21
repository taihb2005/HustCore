package org.kat.app.level.progress.dev_test;

import org.kat.app.level.Level;
import org.kat.app.level.LevelState;
import org.kat.app.level.event.Event;
import org.kat.app.level.event.EventManager;
import org.kat.app.level.event.EventRectangle;
import org.kat.app.map.MapParser;

import java.util.List;

import static org.kat.app.main.GamePanel.*;

public class DevTestLevel extends Level {

    public DevTestLevel() {
        ;
        map = MapParser.loadMap("/textures/map/map0.tmx");
        //map.gp = gp;

        init();
        setter.setFilePathObject("/textures/level/dev_test/enemy_dev.json");
        setter.setFilePathObject("/textures/level/dev_test/object_dev.json");
        setter.setFilePathEnemy("/textures/level/dev_test/enemy_dev.json");
        setter.setFilePathNpc(null);
        setter.loadAll();

        setup();

        stopMusic();
        playMusic(6);

        onCreateLevel = () -> {
          currentState = LevelState.RUNNING;
        };

        onCreateLevel.run();
    }


    public void update(){
        eventManager.update();
    }

    @Override
    public void dispose() {
        super.dispose();
        changeMapEventRect1 = null;
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
                () -> true,
                () -> {
                    getRoom("Room1").start();
                    System.out.println("Started");
                }
        ));

    }
}
