package level.progress;

import entity.Entity;
import entity.npc.Npc_CorruptedHustStudent;
import entity.object.Obj_Door;
import level.EventRectangle;
import level.Level;
import main.GamePanel;
import map.MapManager;
import map.MapParser;

public class Level00 extends Level {
    private boolean finishedTutorialDialogue = false;
    public Level00(GamePanel gp){
        this.gp = gp;
        MapParser.loadMap( "map0" ,"res/map/map_special.tmx");
        map = MapManager.getGameMap("map0");
        map.gp = gp;
        init();
        setter.setFilePathObject("res/level/level00/object_level00.json");
        setter.setFilePathNpc("res/level/level00/npc_level00.json");
        setter.setFilePathEnemy(null);
        //setter.loadAll();

        eventRect = new EventRectangle(0 , 0 , 128 , 32);
    }

    private void checkForTutorialEvent(){
        for(Entity npc : map.npc){
            if(npc != null && npc.idName.equals("Chill Guy")){
                Npc_CorruptedHustStudent npc_tmp = (Npc_CorruptedHustStudent) npc;
                finishedTutorialDialogue = npc_tmp.hasTalkYet();
            }
        }
    }

    private void openTutorialDoor(){
        for(Entity object: map.activeObj){
            if(object != null && (object.idName.equals("Begin DoorMap 01") || object.idName.equals("Begin DoorMap 02") || object.idName.equals("Begin DoorMap 03"))){
                Obj_Door door = (Obj_Door) object;
                door.canChangeState = true;
            }
        }
    }

    public void updateProgress(){
        checkForTutorialEvent();
        if(finishedTutorialDialogue)openTutorialDoor();
        if(levelFinished) eventHandler.detectEvent();
    }
}
