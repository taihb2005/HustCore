package level.progress.level02;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entity.json_stat.EnemyStat;
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
    private int enemiesDefeated = 0;
    private int rewardIndex = 0;
    private final int[] rewards = {1, 2, 3, 4};
    private final Npc_CorruptedHustStudent npc;

    public EventHandler02(Level lvl, String filePathEnemy, Npc_CorruptedHustStudent npc) {
        super(lvl);
        this.filePathEnemy = filePathEnemy;
        this.npc = npc;
    }

    public void onEnemyDefeated() {
        enemiesDefeated++;
        if (enemiesDefeated % 5 == 0 && rewardIndex < rewards.length) {
            int reward = rewards[rewardIndex];
            rewardIndex++;
            npc.addNumberToProvide(reward);
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
    public void loadDialoguesFromJson(String filePath) {
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader(filePath);
            Map<String, List<String>> data = gson.fromJson(reader, new TypeToken<Map<String, List<String>>>() {}.getType());

            List<String> dialoguesFromJson = data.get("dialogues");
            for (int i = 0; i < dialoguesFromJson.size(); i++) {
                if (i < dialogues.length) {
                    dialogues[i][0] = dialoguesFromJson.get(i);
                }
            }
        } catch (Exception e) {
            System.out.println("Không thể tải hội thoại cho NPC.");
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
    }
}
