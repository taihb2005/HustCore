package level.progress.level02;

import entity.Entity;
import entity.npc.Npc_CorruptedHustStudent;
import entity.object.Obj_Door;
import level.LevelState;
import level.event.EventManager;
import level.event.EventRectangle;
import level.Level;
import level.event.Event;
import main.GamePanel;
import main.GameState;
import map.MapParser;
import thread.LoadingService;

import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;

import static main.GamePanel.*;

public class Level02 extends Level {
    private int hintNums = 0;
    public Level02() {
        map = MapParser.loadMap("/map/map2.tmx");
        init();
        setter.setFilePathObject("/level/level02/object_level02.json");
        setter.setFilePathNpc("/level/level02/npc_level02.json");
        setter.setFilePathEnemy("/level/level02/enemy_level02.json");
        setter.loadAll();
        stopMusic();
        playMusic(6);

        setup();

        onCreateLevel = () -> {
            currentRoomTask = getNextRoomTask();
            setLevelState(LevelState.CUTSCENE);
            map.player.setGoal(828, 128);
        };

        onBeginLevel = () -> {
            eventMaster.dialogues[0][0] = new StringBuilder("Player: Lại gặp một gã nữa...");
            eventMaster.dialogues[0][1] = new StringBuilder("Player: Đến nói chuyện xem sao!");

            map.addObject(new Obj_Door(
                    "small",
                    "inactive",
                    "Temporary",
                    832, 0
            ), map.activeObj);

            eventMaster.submitDialogue(eventMaster, 0);
        };

        onFinishLevel = () -> {
            eventMaster.dialogues[0][0] = new StringBuilder("Bạn đã hoàn thành thử thách\nthứ hai");
            eventMaster.dialogues[0][1] = new StringBuilder("Hãy đến cửa phía Bắc để\ntiếp tục!");
            eventMaster.submitDialogue(eventMaster, 0);
        };

        onCreateLevel.run();
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

    public void update(){
        eventManager.update();
        if(!isLevelFinished()) {
            if (currentRoomTask.isRunning()) currentRoomTask.update();
        } else if(currentRoomTask.isFinished()){
            currentRoomTask = getNextRoomTask();
        }
    }
    public void render(Graphics2D g2){

    }

    private void setup(){
        eventManager = new EventManager();
        eventMaster = new Entity();
        changeMapEventRect1 = new EventRectangle(192, 0, 128, 32 , true);
        changeMapEventRect2 = new EventRectangle(1280 , 0 , 120 , 9 , true);
        generatePassword();
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
                () -> currentRoomTask.checkEnemyDifferent(5),
                () -> {
                    eventMaster.dialogues[0][1] = null;
                    eventMaster.dialogues[0][0] = new StringBuilder("Gọi ý cho mật khẩu:\n" + correctPassword.charAt(hintNums));
                    eventMaster.submitDialogue(eventMaster, 0);
                    hintNums++;
                }
        ,true ));

        eventManager.register(new Event(
                () -> getRoom("Room2").getTriggerZone().isTriggered(map.player),
                () -> currentState = LevelState.PASSWORD
        ));

        eventManager.register((new Event(
                () -> {
                    if(canCheckPassword){
                        isCorrect = checkPassword();
                        canCheckPassword = false;
                        return isCorrect;
                    }
                    return false;
                },
                () -> getRoom("Room2").finish()
        )));

        eventManager.register(new Event(
                () -> changeMapEventRect1.isTriggered(map.player) || changeMapEventRect2.isTriggered(map.player),
                () -> {
                    GamePanel.gameState = GameState.LOADING;
                    levelProgress++;
                    LoadingService.loadMap();
                    map.player.storeValue();
                }
        ));
    }

    private boolean checkPassword(){
        return enteredPassword.toString().equals(correctPassword);
    }
}
