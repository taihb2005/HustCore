package level.progress;

import level.EventRectangle;
import level.Level;
import level.AssetSetter;
import level.progress.level02.EventHandler02;
import entity.npc.Npc_CorruptedHustStudent;
import main.GamePanel;
import map.MapManager;
import map.MapParser;

public class Level02 extends Level {
    private Npc_CorruptedHustStudent npc;
    private EventHandler02 eventHandler;

    public Level02(GamePanel gp) {
        this.gp = gp;
        MapParser.loadMap("map_special", "res/map/map_special.tmx");
        map = MapManager.getGameMap("map_special");
        map.gp = gp;
        init();
        createNpc();
        eventHandler = new EventHandler02(this, "res/level/level02/enemy_level02.json", npc);
        setter.setFilePathObject("res/level/level02/object_level02.json");
        setter.setFilePathNpc("res/level/level02/npc_level02.json");
        setter.setFilePathEnemy("res/level/level02/enemy_level02.json");

        eventHandler.setEnemy();
        changeMapEventRect = new EventRectangle(0, 0, 0, 0);
    }

    private void createNpc() {
        npc = new Npc_CorruptedHustStudent(map, "Corrupted Hust Student", 2, new String[]{"Tôi sẽ cho bạn số khi bạn tiêu diệt đủ kẻ thù!"}, 100, 200);
        map.addObject(npc, map.npc);
    }

    @Override
    public void update() {
        super.update();
        eventHandler.update();
    }
}
