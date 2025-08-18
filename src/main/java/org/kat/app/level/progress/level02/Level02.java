package org.kat.app.level.progress.level02;

import org.kat.app.entity.Entity;
import org.kat.app.entity.npc.Npc_CorruptedHustStudent;
import org.kat.app.entity.object.Obj_Door;
import org.kat.app.level.Level;
import org.kat.app.level.LevelState;
import org.kat.app.level.event.Event;
import org.kat.app.level.event.EventManager;
import org.kat.app.level.event.EventRectangle;
import org.kat.app.main.GamePanel;
import org.kat.app.main.GameState;
import org.kat.app.main.UI;
import org.kat.app.thread.LoadingService;
import org.kat.app.ui.hustcore.PasswordInput;

import java.awt.*;
import java.util.List;
import java.util.Random;

import static org.kat.app.main.GamePanel.*;

public class Level02 extends Level {
    private String correctPassword ;
    public Level02() {
        super();
    }

    private void generatePassword(){
        Random random = new Random();
        StringBuilder correctPassword_tmp = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            int nextChar = random.nextInt(10);
            correctPassword_tmp.append(nextChar);
        }
        correctPassword = correctPassword_tmp.toString();
    }

    @Override
    public void onLoad(){

    }

    @Override
    public void onCreate() {
        currentRoomTask = getNextRoomTask();
        setLevelState(LevelState.CUTSCENE);
        map.player.setGoal(828, 128);

        eventMaster.buildDialogue();
    }

    @Override
    public void onBegin() {
        eventMaster.setDialogueAt(0, 0, "Player: Lại gặp một gã nữa...");
        eventMaster.setDialogueAt(0, 1, "Player: Đến nói chuyện xem sao!");

        eventMaster.setDialogueAt(1, 0, "Bạn đã hoàn thành thử thách thứ hai");
        eventMaster.setDialogueAt(1, 1, "Hãy đến cửa phía Bắc để tiếp tục!");
        for(int i = 0 ; i < correctPassword.length(); i++){
            char c = correctPassword.charAt(i);
            eventMaster.setDialogueAt(2, i,
                    "Gợi ý cho mật khẩu: " + c);
        }
        eventMaster.buildDialogue();

        map.addObject(new Obj_Door(
                "small",
                "inactive",
                "Temporary",
                832, 0
        ), map.activeObj);

        eventMaster.submitDialogue(0);
    }

    @Override
    public void onFinish() {
        eventMaster.submitDialogue(1);
    }

    @Override
    public String getMapPath() {
        return "/data/map/map2.tmx";
    }

    @Override
    public String getObjectJsonPath() {
        return "/data/level/level02/object_level02.json";
    }

    @Override
    public String getEnemyJsonPath() {
        return "/data/level/level02/enemy_level02.json";
    }

    @Override
    public String getNPCJsonPath() {
        return "/data/level/level02/npc_level02.json";
    }

    @Override
    public int getMusicFile() {
        return 6;
    }

    public void update(){
        eventManager.update();
        if(!isLevelFinished()) {
            if (currentRoomTask.isRunning()) currentRoomTask.update();
        } else if(currentRoomTask.isFinished()){
            currentRoomTask = getNextRoomTask();
        }
    }

    @Override
    public void setup(){
        eventManager = new EventManager();
        eventMaster = new Entity();
        changeMapEventRect1 = new EventRectangle(192, 0, 128, 32 , true);
        changeMapEventRect2 = new EventRectangle(1280 , 0 , 120 , 9 , true);
        generatePassword();
        ((PasswordInput) UI._UIManager.findUIScreenByName("password_input")).setCorrectPassword(correctPassword);
        enteredPassword = new StringBuffer();

        configureRoom("Room1",
                List.of(),
                List.of("DoorA003"),
                new EventRectangle(0, 0 ,0 ,0),
                List.of("Cyborgon001", "Cyborgon002", "Cyborgon003", "Cyborgon004", "Cyborgon005",
                        "Cyborgon006", "HustGuardian007", "HustGuardian008", "HustGuardian009", "HustGuardian010",
                        "HustGuardian011", "HustGuardian012", "Spectron013", "Spectron014", "Spectron015", "Shooter016",
                        "Shooter017", "Shooter018", "Shooter019", "Shooter020")
                );

        configureRoom("Room2",
                List.of(),
                List.of("DoorA001", "DoorA002"),
                new EventRectangle(822, 694, 74, 74, false),
                List.of()
        );

        configureRoomOrder(List.of(
                getRoom("Room1"),
                getRoom("Room2")
        ));

        eventManager.register(new Event(
                () -> entityManager.get("NPC001", Npc_CorruptedHustStudent.class).hasTalkYet(),
                () -> currentRoomTask.start()
        ));

        eventManager.register(new Event(
                () -> currentRoomTask.checkEnemyDifference(20),
                () -> {
                    eventMaster.submitDialogue(2);
                }
        ,true ));

        eventManager.register(new Event(
                () -> getRoom("Room2").getTriggerZone().isTriggered(map.player),
                () -> {
                    currentLevel.setLevelState(LevelState.PASSWORD);
                    UI._UIManager.setCurrentScreen("password_input");
                }
        ));

        eventManager.register((new Event(
                () -> ((PasswordInput) UI._UIManager.findUIScreenByName("password_input")).isCorrect(),
                () -> {
                    getRoom("Room2").finish();
                    UI._UIManager.clearFromScreenStack();
                    currentLevel.setLevelState(LevelState.RUNNING);
                }
        )));

        eventManager.register(new Event(
                () -> changeMapEventRect1.isTriggered(map.player) || changeMapEventRect2.isTriggered(map.player),
                () -> {
                    GamePanel.gameState = GameState.LOADING;
                    levelProgress++;
                    LoadingService.loadLevel();
                    map.player.storeValue();
                }
        ));
    }

    private boolean checkPassword(){
        return enteredPassword.toString().equals(correctPassword);
    }
}
