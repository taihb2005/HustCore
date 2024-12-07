package level.progress.level02;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entity.Entity;
import entity.json_stat.EnemyStat;
import entity.json_stat.NpcStat;
import entity.mob.*;
import entity.npc.Npc_CorruptedHustStudent;
import level.EventHandler;
import level.Level;
import map.GameMap;

import java.awt.*;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public class EventHandler02 extends EventHandler {
    private int enemiesDefeated = 0;
    private int rewardIndex = 0;
    private final int[] rewards = {1, 0, 5, 6};

    public EventHandler02(Level lvl) {
        super(lvl);
    }

    public void onEnemyDefeated() {
        enemiesDefeated++;
        System.out.println("Enemy defeated: " + enemiesDefeated);
        if (enemiesDefeated % 5 == 0 && rewardIndex < rewards.length) {
            int reward = rewards[rewardIndex];
            rewardIndex++;

            for (Object npcObj : lvl.map.npc) {
                if (npcObj instanceof Npc_CorruptedHustStudent) {
                    Npc_CorruptedHustStudent npc = (Npc_CorruptedHustStudent) npcObj;
                    npc.addDialogue("Bạn nhận được con số: " + reward);
                }
            }
        }
    }



    @Override
    public void update() {
    }
}
