package level;

import entity.Entity;
import entity.mob.Monster;
import level.event.EventManager;
import level.event.EventRectangle;
import map.GameMap;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

import static main.GamePanel.*;

public class Level{
    public GameMap map;
    protected AssetSetter setter;
    protected EntityManager entityManager;
    protected EventManager eventManager;

    public EventRectangle changeMapEventRect1;
    public EventRectangle changeMapEventRect2;
    protected LevelState currentState;
    public boolean canChangeMap;

    protected Entity eventMaster = new Entity();

    protected HashMap<String, RoomTask> rooms = new HashMap<>();
    protected Queue<RoomTask> roomTaskQueue = new LinkedList<>();

    protected RoomTask previousRoomTask;
    protected RoomTask currentRoomTask;
    protected RoomTask nextRoomTask;

    public Runnable onCreateLevel;
    public Runnable onBeginLevel;
    public Runnable onFinishLevel;

    public boolean levelFinished;
    public boolean finishedBeginningDialogue = false;

    public String enteredPassword = "";
    public String correctPassword ;

    public void init(){
        camera.setCamera(windowWidth , windowHeight , map.getMapWidth() ,map.getMapHeight());
        setter = new AssetSetter(map, this);
        entityManager = new EntityManager();
        canChangeMap = false;
        levelFinished = false;
    };

    public boolean triggerEvent(EventRectangle e){
        int newSolidAreaX1 = map.player.worldX + map.player.solidArea1.x;
        int newSolidAreaY1 = map.player.worldY + map.player.solidArea1.y;

        Rectangle tmp1 = new Rectangle(newSolidAreaX1 , newSolidAreaY1 , map.player.solidArea1.width , map.player.solidArea1.height);
        try {
            if (tmp1.intersects(e)) {
                if (e.oneTimeOnlyEvent) e.eventFinished = true;
                return true;
            }
        } catch(NullPointerException exception){
            return false;
        }
        return false;
    }

    public void update(){}
    public void render(Graphics2D g2){};

    public void setLevelState(LevelState state){
        this.currentState = state;
    }

    public LevelState getLevelState(){
        return currentState;
    }


    public void addEnemy(Monster monster){
        map.addObject(monster, map.enemy);
    }

    public boolean checkState(LevelState state){
        return state == currentState;
    }

    public RoomTask getNextRoomTask(){
        return roomTaskQueue.poll();
    }

    public void addRoom(String roomName, RoomTask roomTask){
        rooms.put(roomName, roomTask);
    }

    public RoomTask getRoom(String roomName){
        return rooms.get(roomName);
    }

    protected void configureRoom(String roomName,
                                 List<String> deactivate,
                                 List<String> activate,
                                 EventRectangle zone,
                                 List<String> enemyIds){
        RoomTask room = rooms.get(roomName);
        room.addDoorNeedDeactivate(deactivate);
        room.addDoorNeedActivate(activate);
        room.setTriggerZone(zone);
        for (String id : enemyIds) {
            room.addTargetedEnemy(entityManager.get(id, Monster.class));
        }
    }

    protected void configureRoom(String roomName,
                                 List<String> deactivate,
                                 List<String> activate,
                                 List<String> entityIds,
                                 List<String> enemyIds){
        RoomTask room = rooms.get(roomName);
        room.addDoorNeedDeactivate(deactivate);
        room.addDoorNeedActivate(activate);
        for (String id : entityIds) {
            room.addTargetedEntity(entityManager.get(id, Entity.class));
        }
        for (String id : enemyIds) {
            room.addTargetedEnemy(entityManager.get(id, Monster.class));
        }
    }

    protected void configureRoom(String roomName,
                                 List<String> deactivate,
                                 List<String> activate,
                                 EventRectangle zone,
                                 List<String> entityIds,
                                 List<String> enemyIds){
        RoomTask room = rooms.get(roomName);
        room.addDoorNeedDeactivate(deactivate);
        room.addDoorNeedActivate(activate);
        room.setTriggerZone(zone);
        for (String id : entityIds) {
            room.addTargetedEntity(entityManager.get(id, Entity.class));
        }
        for (String id : enemyIds) {
            room.addTargetedEnemy(entityManager.get(id, Monster.class));
        }
    }

    protected void configureRoom(String roomName,
                                 List<String> deactivate,
                                 List<String> activate,
                                 List<String> entityIds,
                                 List<String> enemyIds,
                                 StringBuilder mission){
        RoomTask room = rooms.get(roomName);
        room.addDoorNeedDeactivate(deactivate);
        room.addDoorNeedActivate(activate);
        for (String id : entityIds) {
            room.addTargetedEntity(entityManager.get(id, Entity.class));
        }
        for (String id : enemyIds) {
            room.addTargetedEnemy(entityManager.get(id, Monster.class));
        }
        room.createTaskBoard(mission);
    }

    protected void configureRoom(String roomName,
                                 List<String> deactivate,
                                 List<String> activate,
                                 EventRectangle zone,
                                 List<String> entityIds,
                                 List<String> enemyIds,
                                 StringBuilder mission){
        RoomTask room = rooms.get(roomName);
        room.addDoorNeedDeactivate(deactivate);
        room.addDoorNeedActivate(activate);
        room.setTriggerZone(zone);
        for (String id : entityIds) {
            room.addTargetedEntity(entityManager.get(id, Entity.class));
        }
        for (String id : enemyIds) {
            room.addTargetedEnemy(entityManager.get(id, Monster.class));
        }
        room.createTaskBoard(mission);
    }


    protected void configureRoom(String roomName,
                                 List<String> deactivate,
                                 List<String> activate,
                                 EventRectangle zone,
                                 List<String> enemyIds,
                                 StringBuilder mission){
        RoomTask room = rooms.get(roomName);
        room.addDoorNeedDeactivate(deactivate);
        room.addDoorNeedActivate(activate);
        room.setTriggerZone(zone);
        for (String id : enemyIds) {
            room.addTargetedEnemy(entityManager.get(id, Monster.class));
        }
        room.createTaskBoard(mission);
    }

    protected void configureRoom(String roomName,
                                 List<String> deactivate,
                                 List<String> activate,
                                 List<String> enemyIds,
                                 StringBuilder mission){
        RoomTask room = rooms.get(roomName);
        room.addDoorNeedDeactivate(deactivate);
        room.addDoorNeedActivate(activate);
        for (String id : enemyIds) {
            room.addTargetedEnemy(entityManager.get(id, Monster.class));
        }
        room.createTaskBoard(mission);
    }

    protected void configureRoomOrder(List<RoomTask> roomList){
        roomTaskQueue.addAll(roomList);
    }

    protected boolean isLevelFinished(){
        return roomTaskQueue.isEmpty();
    }

    public void dispose() {
        if (map != null) {
            map.dispose();
            map = null;
        }

        if (eventManager != null) {
            eventManager.clear();
            eventManager = null;
        }

        if (entityManager != null) {
            entityManager.clear();
            entityManager = null;
        }

        setter = null;

        changeMapEventRect1 = null;
        changeMapEventRect2 = null;

        for(RoomTask room: rooms.values()){
            room.dispose();
        }
        rooms.clear();
        roomTaskQueue.clear();
        previousRoomTask = null;
        currentRoomTask = null;
        nextRoomTask = null;

        onCreateLevel = null;
        onBeginLevel = null;
        onFinishLevel = null;

        eventMaster.dispose();
        eventMaster = null;

        levelFinished = false;
        finishedBeginningDialogue = false;
        enteredPassword = "";
        correctPassword = null;

        currentState = null;
    }
}

