package level.progress.level02;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
    private final String filePathEnemy;
    private String filePathNpc;
    private int enemiesDefeated = 0;
    private int rewardIndex = 0;
    private final int[] rewards = {1, 0, 5, 6};

    public EventHandler02(Level lvl, String filePathEnemy, String filePathNpc) {
        super(lvl);
        this.filePathEnemy = filePathEnemy;
        this.filePathNpc = filePathNpc;
    }

    public void onEnemyDefeated() {
        enemiesDefeated++;
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

    public void setEnemy() {
        try (Reader reader = new FileReader(filePathEnemy)) {
            Gson gson = new Gson();

            Map<String, List<EnemyStat>> data = gson.fromJson(reader, new TypeToken<Map<String, List<EnemyStat>>>() {}.getType());
            List<EnemyStat> enemies = data.get("root");

            for (EnemyStat enemy : enemies) {
                int X = enemy.getX();
                int Y = enemy.getY();

                switch (enemy.getEnemy()) {
                    case "Mon_Cyborgon":
                        lvl.map.addObject(new Mon_Cyborgon(lvl.map, X, Y, enemy.getName()) {
                            public void onDeath() {
                                onEnemyDefeated();
                            }
                        }, lvl.map.enemy);
                        break;

                    case "Mon_HustGuardian":
                        lvl.map.addObject(new Mon_HustGuardian(lvl.map, X, Y, enemy.getName()) {
                            public void onDeath() {
                                onEnemyDefeated();
                            }
                        }, lvl.map.enemy);
                        break;

                    case "Mon_Spectron":
                        lvl.map.addObject(new Mon_Spectron(lvl.map, X, Y, enemy.getName()) {
                            public void onDeath() {
                                onEnemyDefeated();
                            }
                        }, lvl.map.enemy);
                        break;

                    case "Mon_Shooter":
                        if (enemy.getDetection() == null) {
                            lvl.map.addObject(new Mon_Shooter(lvl.map, enemy.getDirection(), enemy.getType(),
                                    enemy.isAlwaysUp(), enemy.getAttackCycle(), enemy.getName(), X, Y) {
                                public void onDeath() {
                                    onEnemyDefeated();
                                }
                            }, lvl.map.enemy);
                        } else {
                            Rectangle detect = new Rectangle(enemy.getDetection().getX(), enemy.getDetection().getY(),
                                    enemy.getDetection().getWidth(), enemy.getDetection().getHeight());
                            lvl.map.addObject(new Mon_Shooter(lvl.map, enemy.getDirection(), enemy.getType(),
                                    enemy.isAlwaysUp(), enemy.getAttackCycle(), detect, enemy.getName(), X, Y) {
                                public void onDeath() {
                                    onEnemyDefeated();
                                }
                            }, lvl.map.enemy);
                        }
                        break;
                }
            }

        } catch (Exception e) {
            System.out.println("Không tìm thấy quái trong bản đồ.");
            e.printStackTrace();
        }
    }
    public void setNpc() {
        try {
            Reader reader = new FileReader(filePathNpc);
            Gson gson = new Gson();

            Map<String, List<NpcStat>> data = gson.fromJson(reader, new TypeToken<Map<String, List<NpcStat>>>() {}.getType());
            List<NpcStat> npcs = data.get("root");

            for (NpcStat npcStat : npcs) {
                int x = npcStat.getX();
                int y = npcStat.getY();
                String[][] dialogues = npcStat.getDialogue();

                Npc_CorruptedHustStudent npc = new Npc_CorruptedHustStudent(lvl.map, npcStat.getName(), npcStat.getDirection(), dialogues, x, y);

                lvl.map.addObject(npc, lvl.map.npc);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo NPC từ file JSON.");
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
    }
}
