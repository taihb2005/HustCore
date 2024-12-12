package level.progress.level01;

import entity.Entity;
import entity.object.Obj_Door;
import level.EventHandler;
import level.EventRectangle;
import level.Level;

import java.util.Timer;

public class EventHandler01 extends EventHandler {
    private EventRectangle closeDoorBeginLevel;
    //Room1
    private EventRectangle beginRoom1;
    private int countEnemyRoom1;
    private boolean room1Completed;
    //Room2
    private EventRectangle beginRoom2;
    private int countEnemyRoom2;
    private boolean room2Completed;
    //Room3
    private EventRectangle beginRoom3;
    private int countEnemyRoom3;
    private boolean room3ChestOpened;
    private boolean room3Completed;


    private final Entity[] eventEntity = new Entity[10];
    public EventHandler01(Level lvl) {
        super(lvl);
        timer = new Timer();
        setEventRect();
        setEventEntity();

        countEnemyRoom1 = 2;
        countEnemyRoom2 = 1;
        countEnemyRoom3 = 2;
    }

    private void setEventEntity(){
        try {
            eventEntity[0] = new Obj_Door("small", "inactive", "Room1 Door", 320, 832);
            eventEntity[1] = new Obj_Door("big", "inactive", "Room3 Door", 1408, 1024);
        } catch(Exception e){
            System.out.println("Lỗi ở trong class EventHandler01!");
        }
    }

    private void setEventRect(){
        beginRoom1 = new EventRectangle(320 , 903 , 64 , 32 , true);
        beginRoom2 = new EventRectangle(320 , 1230 , 64 , 32 , true);
        beginRoom3 = new EventRectangle(1408 , 992 , 128 , 32 , true);
    }

    private void checkForCompletingRoom1(){
        for(Entity e : lvl.map.enemy){
            if(e != null && e.idName.equals("Room1Spectron") && e.canbeDestroyed) countEnemyRoom1--;
        }
        if(countEnemyRoom1 <= 0) {
            room1Completed = true;
            for(Entity e : lvl.map.activeObj)
                if(e != null && e.idName.equals("Room2 Door")){
                    e.canChangeState = true;
                }
        }
    }

    private void anounceTaskRoom1(){
        eventMaster.dialogues[0][0] = new StringBuilder("Nhiệm vụ:\n\nTiêu diệt 2 con Spectron!");
        eventMaster.startDialogue(eventMaster , 0);
    }

    private void killAllRoom1Shooter(){
        for(int i = 0 ; i < lvl.map.enemy.length ; i++){
            if(lvl.map.enemy[i] != null && lvl.map.enemy[i].idName.equals("room1")){
                lvl.map.enemy[i].currentHP = 0;
            }
        }
    }

    private void checkForCompletingRoom2(){
        for(Entity e : lvl.map.enemy){
            if(e != null && e.idName.equals("Room2Enemy") && e.canbeDestroyed) countEnemyRoom2--;
        }
        if(countEnemyRoom2 <= 0) {
            room2Completed = true;
            for(Entity e : lvl.map.activeObj) {
                if (e != null && e.idName.equals("Room1 Door")) {
                    Obj_Door tmp = (Obj_Door) e;
                    tmp.canChangeState = true;
                }
                if(e != null && e.idName.equals("Room2 Door")){
                    Obj_Door tmp = (Obj_Door) e;
                    tmp.canChangeState = true;
                }
                if(e != null && e.idName.equals("Room3 Door")){
                    Obj_Door tmp = (Obj_Door) e;
                    tmp.canChangeState = true;
                }
            }
            killAllRoom1Shooter();
        }
    }

    private void anounceTaskRoom2(){
        eventMaster.dialogues[0][0] = new StringBuilder("Nhiệm vụ:\n\nTiêu diệt con Cyborgon bên dưới!");
        eventMaster.startDialogue(eventMaster , 0);
    }

    //ROOM3

    private void anounceTaskRoom3(){
        eventMaster.dialogues[0][0] = new StringBuilder("Nhiệm vụ:\n\nMở rương ở giữa phòng!\nTiêu diệt toàn bộ trụ súng.!");
        eventMaster.startDialogue(eventMaster , 0);
    }

    private void checkForCompletingRoom3(){
        for(Entity e : lvl.map.activeObj) {
            if (e != null && e.idName.equals("chest_room3")) {
                room3ChestOpened = true;
                break;
            }
        }
        for(Entity e : lvl.map.enemy) {
            if (e != null && e.idName.equals("Room3Shooter") && e.canbeDestroyed) countEnemyRoom3--;
        }
        if(room3ChestOpened && countEnemyRoom3 == 0){
            for(Entity e : lvl.map.activeObj) {
                if (e != null && e.idName.equals("Room3 Door")) {
                    Obj_Door tmp = (Obj_Door) e;
                    tmp.canChangeState = true;
                }
                if (e != null && e.idName.equals("Room4 Door")) {
                    Obj_Door tmp = (Obj_Door) e;
                    tmp.canChangeState = true;
                }
            }
            room3Completed = true;
            lvl.levelFinished = true;
        }
    }

    private void checkForCompletingLevel(){
        for(Entity e : lvl.map.activeObj) {
            if (e != null && e.idName.equals("EndDoor")) {
                Obj_Door tmp = (Obj_Door) e;
                tmp.canChangeState = true;
                lvl.levelFinished = true;
            }
        }
    }

    public void update(){
        //ROOM1
        if(!beginRoom1.eventFinished && triggerEvent(beginRoom1)) {
            lvl.map.addObject(eventEntity[0] , lvl.map.activeObj);
            anounceTaskRoom1();
        }
        if(!room1Completed) checkForCompletingRoom1();

        //ROOM2
        if(room1Completed && !beginRoom2.eventFinished && triggerEvent(beginRoom2)) {
            anounceTaskRoom2();
        }
        if(!room2Completed && room1Completed) checkForCompletingRoom2();

        //ROOM3

        if(!beginRoom3.eventFinished && triggerEvent(beginRoom3)) {
            lvl.map.addObject(eventEntity[1] , lvl.map.activeObj);
            anounceTaskRoom3();
        }
        if(!room3Completed && room1Completed && room2Completed) checkForCompletingRoom3();
        if(room3Completed) {
            checkForCompletingLevel();
            lvl.map.player.storeValue();
        }

        //LEVEL COMPLETED
        lvl.canChangeMap = triggerEvent(lvl.changeMapEventRect1);
    }
}
