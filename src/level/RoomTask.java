package level;

import entity.Entity;
import entity.mob.Monster;
import entity.object.Obj_Door;
import level.event.EventHandler;
import level.event.EventRectangle;

import java.util.ArrayList;
import java.util.List;

public class RoomTask {
    Level lvl;

    String nameId;
    RoomState currentState = RoomState.PENDING;

    int remainingEnemies;
    int lastRemainingEnemies;

    private Entity eventMaster;
    private EventRectangle triggerZone;

    private final ArrayList<Monster> enemy = new ArrayList<>();
    private final ArrayList<Monster> targetedEnemy = new ArrayList<>();
    private final ArrayList<Entity>  targetedEntity = new ArrayList<>();
    private final ArrayList<String> doorDeactivate = new ArrayList<>();
    private final ArrayList<String> doorActivate = new ArrayList<>();

    Runnable onStart;
    Runnable onComplete;

    RoomTask(Level lvl){
        this.lvl = lvl;
        eventMaster = new Entity();

        onStart = () -> {
            for(Monster monster: enemy){
                lvl.addEnemy(monster);
            }

            for(String idName: doorDeactivate){
                lvl.entityManager.get(idName, Obj_Door.class).close();
            }

            if(eventMaster.dialogues != null)
                eventMaster.submitDialogue(eventMaster, 0);
        };

        onComplete= () -> {
            for(String idName: doorActivate){
                lvl.entityManager.get(idName, Obj_Door.class).activate();
            }
            for(Monster mon: enemy){
                mon.kill();
            }
        };
    }

    public void update() {
        if(targetedEnemy.isEmpty()){
           if(isRunning()) finish();
        } else {
            targetedEnemy.removeIf(enemy -> enemy.canbeDestroyed);
        }
    }

    boolean trigger(EventHandler handler) {
        return !triggerZone.eventFinished && handler.triggerEvent(triggerZone);
    }

    public void addEnemy(Monster monster){
        enemy.add(monster);
    }

    public void addTargetedEntity(Entity entity){
        targetedEntity.add(entity);
    }

    public void addTargetedEnemy(Monster enemy){
        targetedEnemy.add(enemy);
    }

    public void addTargetedEnemy(List<Monster> enemy){
        targetedEnemy.addAll(enemy);
    }

    public void addDoorNeedDeactivate(List<String> idName){
        doorDeactivate.addAll(idName);
    }

    public void addDoorNeedActivate(List<String> idName){
        doorActivate.addAll(idName);
    }

    public void setTriggerZone(EventRectangle triggerZone){
        this.triggerZone = triggerZone;
    }

    public EventRectangle getTriggerZone(){
        return triggerZone;
    }

    public void createTaskBoard(StringBuilder task){
        eventMaster.dialogues[0][0] = task;
    }

    public boolean checkEnemyDifferent(int difference){
        remainingEnemies = targetedEnemy.size();
        if((lastRemainingEnemies - remainingEnemies) == difference){
            lastRemainingEnemies = remainingEnemies;
            return true;
        }
        return false;
    }

    public void start(){
        currentState = RoomState.START;
        onStart.run();
        currentState = RoomState.RUNNING;
    }

    public void finish(){
        onComplete.run();
        currentState = RoomState.COMPLETED;
    }

    public boolean isPending(){
        return currentState == RoomState.PENDING;
    }

    public boolean isRunning(){
        return currentState == RoomState.RUNNING;
    }

    public boolean isFinished(){
        return currentState == RoomState.COMPLETED;
    }

    public void setFinish(){
        currentState = RoomState.COMPLETED;
    }

    public void dispose() {
        for (Monster mon : enemy) {
            mon.kill();
            mon.dispose();
        }
        enemy.clear();

        for (Monster mon : targetedEnemy) {
            mon.kill();
            mon.dispose();
        }
        targetedEnemy.clear();

        for (Entity entity : targetedEntity) {
            entity = null;
        }
        targetedEntity.clear();

        doorDeactivate.clear();
        doorActivate.clear();

        triggerZone = null;

        eventMaster.dispose();
        eventMaster = null;

        System.gc();
    }


    private enum RoomState{
        PENDING, START, RUNNING, COMPLETED
    }
}
