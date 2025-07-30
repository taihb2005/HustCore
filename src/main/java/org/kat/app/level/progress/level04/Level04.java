package org.kat.app.level.progress.level04;

import org.kat.app.entity.mob.Mon_Boss;
import org.kat.app.entity.object.Obj_Door;
import org.kat.app.level.Level;
import org.kat.app.level.LevelState;
import org.kat.app.level.event.Event;
import org.kat.app.level.event.EventManager;

import static org.kat.app.main.GamePanel.*;

public class Level04 extends Level {
    //public EventHandler04 eventHandler04;
    Mon_Boss boss;

    public Level04() {
        super();
    }

    @Override
    public void onCreate() {
        stopMusic();
        playMusic(5);
        currentRoomTask = getNextRoomTask();
        setLevelState(LevelState.CUTSCENE);
        map.player.setGoal(448, 640);
    }

    @Override
    public void onBegin() {
        map.addObject(new Obj_Door(
                "big",
                "inactive",
                "Temporary",
                192, 64
        ), map.activeObj);

        eventMaster.dialogues[0][0] = new StringBuilder("Boss: Ngươi giỏi lắm mới đến \nđược đây");
        eventMaster.dialogues[0][1] = new StringBuilder("Boss: Ngắm gà khoả thân mau!");

        eventMaster.dialogues[1][0] = new StringBuilder("Boss: Không ngờ ngươi lại mạnh\nđến vậy!");

        eventMaster.dialogues[2][0] = new StringBuilder("Boss: Ta sẽ còn quay lại.");
        eventMaster.dialogues[2][1] = new StringBuilder("Boss: Hãy đợi đấy!!!!!");
        eventMaster.submitDialogue(eventMaster, 0);

        getRoom("Room1").start();
    }

    @Override
    public void onFinish() {
        gameCompleted = true;
    }

    @Override
    public String getMapPath() {
        return "/data/map/map4.tmx";
    }

    @Override
    public String getObjectJsonPath() {
        return "/data/level/level04/object_level04.json";
    }

    @Override
    public String getEnemyJsonPath() {
        return "/data/level/level04/enemy_level04.json";
    }

    @Override
    public int getMusicFile() {
        return 5;
    }

    public void update() {
        eventManager.update();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void setup(){
        levelFinished = false;
        canChangeMap = false;

        eventManager = new EventManager();

        eventManager.register(new Event(
                () -> entityManager.get("Boss000", Mon_Boss.class).checkHalfHealth(),
                () -> {
                    eventMaster.submitDialogue(eventMaster, 1);
                    getRoom("Room2").start();
                }
        ));

        eventManager.register(new Event(
                () -> entityManager.get("Boss000", Mon_Boss.class).isDying,
                () -> {
                    getRoom("Room1").finish();
                    getRoom("Room2").finish();
                    onFinish();
                }
        ));
    }
}