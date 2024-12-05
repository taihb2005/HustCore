package level.progress.level00;

import level.EventRectangle;
import level.Level;
import main.GamePanel;
import map.MapManager;
import map.MapParser;

public class Level00 extends Level {
    final EventHandler00 eventHandler00 = new EventHandler00(this);

    public Level00(GamePanel gp){
        this.gp = gp;
        MapParser.loadMap( "map0" ,"res/map/map0.tmx");
        map = MapManager.getGameMap("map0");
        map.gp = gp;

        init();
        setter.setFilePathObject("res/level/level00/object_level00.json");
        setter.setFilePathNpc("res/level/level00/npc_level00.json");
        setter.setFilePathEnemy("res/level/level01/enemy_level00.json");
        setter.loadAll();

        levelFinished = true;
        changeMapEventRect = new EventRectangle(1088 , 2280 , 64 , 32);
    }

    public void updateProgress(){
        eventHandler00.checkForTutorialEvent();
        if(!finishedBeginingDialogue &&!gp.darker && !gp.lighter) eventHandler00.startingDialogue();
        if(finishedTutorialDialogue) eventHandler00.openTutorialDoor();
        if(levelFinished) eventHandler00.detectForMapChange();
    }
}
