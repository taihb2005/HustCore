package level.progress.level02;

import entity.json_stat.EnemyStat;
import entity.mob.*;
import entity.npc.Npc_CorruptedHustStudent;
import map.GameMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import level.EventHandler;
import level.Level;

import java.awt.*;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public class EventHandler02 extends EventHandler {
    private String filePathEnemy;
    private int enemiesDefeated = 0;
    private int rewardIndex = 0;
    private int[] rewards = {1, 2, 3, 4};
    private Npc_CorruptedHustStudent npc;

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
        try {
            Reader reader = new FileReader(filePathEnemy); // Đọc file JSON chứa enemy
            Gson gson = new Gson();

            Map<String, List<EnemyStat>> data = gson.fromJson(reader, new TypeToken<Map<String, List<EnemyStat>>>() {}.getType());

            List<EnemyStat> Enemies = data.get("root");

            for (EnemyStat enemy : Enemies) {

                int X = enemy.getX();
                int Y = enemy.getY();


                switch (enemy.getEnemy()) {
                    case "Mon_Cyborgon":
                        lvl.map.addObject(new Mon_Cyborgon(lvl.map, X, Y, enemy.getName()) {
                            @Override
                            public void onDeath() {
                                onEnemyDefeated();
                            }
                        }, lvl.map.enemy);
                        break;
                    case "Mon_HustGuardian":
                        lvl.map.addObject(new Mon_HustGuardian(lvl.map, X, Y, enemy.getName()) {
                            @Override
                            public void onDeath() {
                                onEnemyDefeated();
                            }
                        }, lvl.map.enemy);
                        break;
                    case "Mon_Spectron":
                        lvl.map.addObject(new Mon_Spectron(lvl.map, X, Y, enemy.getName()) {
                            @Override
                            public void onDeath() {
                                onEnemyDefeated();
                            }
                        }, lvl.map.enemy);
                        break;
                    case "Mon_Shooter":
                        if (enemy.getDetection() == null) {
                            lvl.map.addObject(new Mon_Shooter(lvl.map, enemy.getDirection(), enemy.getType(), enemy.isAlwaysUp(), enemy.getAttackCycle(), enemy.getName(), enemy.getX(), enemy.getY()) {
                                @Override
                                public void onDeath() {
                                    onEnemyDefeated();
                                }
                            }, lvl.map.enemy);
                        } else {
                            Rectangle detect = new Rectangle(enemy.getDetection().getX(), enemy.getDetection().getY(), enemy.getDetection().getWidth(), enemy.getDetection().getHeight());
                            lvl.map.addObject(new Mon_Shooter(lvl.map, enemy.getDirection(), enemy.getType(), enemy.isAlwaysUp(), enemy.getAttackCycle(), detect, enemy.getName(), enemy.getX(), enemy.getY()) {
                                @Override
                                public void onDeath() {
                                    onEnemyDefeated();
                                }
                            }, lvl.map.enemy);
                        }
                        break;
                }

            }

        } catch (Exception e) {
            System.out.println("Không sao hết, chỉ là bàn này không có quái thôi");
            e.printStackTrace();
        }
    }

    @Override
    public void update() {

    }
}
