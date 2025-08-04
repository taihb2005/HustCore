package org.kat.app.level.progress.level04;

import org.kat.app.entity.mob.Mon_Boss;
import org.kat.app.entity.npc.Npc_CorruptedHustStudent;
import org.kat.app.entity.object.Obj_Door;
import org.kat.app.level.Level;
import org.kat.app.level.LevelState;
import org.kat.app.level.event.Event;
import org.kat.app.level.event.EventManager;
import org.kat.app.main.GameState;
import org.kat.app.main.UI;

import static org.kat.app.main.GamePanel.*;

public class Level04 extends Level {
    public Level04() {
        super();
    }

    @Override
    public void onLoad(){

    }

    @Override
    public void onCreate() {
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

        eventMaster.setDialogueAt(0, 0, "Ngươi giỏi lắm mới đến được đây");
        eventMaster.setDialogueAt(0, 1, "Ngắm gà khoả thân mau!");

        eventMaster.setDialogueAt(1, 0, "Không ngờ ngươi lại mạnh đến vậy!");

        eventMaster.setDialogueAt(2, 0, "Ta sẽ còn quay lại.");
        eventMaster.setDialogueAt(2, 1, "Hãy đợi đấy!!!");

        eventMaster.setDialogueAt(3, 0, "Phù... Quả là một đối thủ khó nhằn");
        eventMaster.setDialogueAt(3, 1, "Nhưng mình đã làm được");
        eventMaster.setDialogueAt(3, 2, "Chí ít là như vậy...");

        eventMaster.buildDialogue();

        eventMaster.submitDialogue(0);

        getRoom("Room1").start();
        stopMusic();
        playMusic(5);
    }

    @Override
    public void onFinish() {
        gameCompleted = true;
        gameState = GameState.CREDIT;
        UI._UIManager.setCurrentScreen("end_credit");
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
    public String getNPCJsonPath(){return "/data/level/level04/npc_level04.json";}

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
                    eventMaster.submitDialogue(1);
                    getRoom("Room2").start();
                }
        ));

        eventManager.register(new Event(
                () -> entityManager.get("Boss000", Mon_Boss.class).isDying,
                () -> {
                    getRoom("Room1").finish();
                    getRoom("Room2").finish();
                    map.player.isImmortal = true;
                    onFinish();
                }
        ));
    }
}