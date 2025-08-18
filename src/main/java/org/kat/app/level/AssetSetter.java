package org.kat.app.level;

import org.kat.app.ai.PathFinder2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.kat.app.entity.Entity;
import org.kat.app.entity.effect.Effect;
import org.kat.app.entity.effect.type.Blind;
import org.kat.app.entity.effect.type.Slow;
import org.kat.app.entity.effect.type.Speed;
import org.kat.app.entity.effect.type.Strength;
import org.kat.app.entity.items.Item_Battery;
import org.kat.app.entity.items.Item_Kit;
import org.kat.app.entity.items.Item_Potion;
import org.kat.app.entity.items.Item_StrengthGem;
import org.kat.app.entity.json_serializable.EnemyStat;
import org.kat.app.entity.json_serializable.GameObject;
import org.kat.app.entity.json_serializable.ItemStat;
import org.kat.app.entity.json_serializable.NpcStat;
import org.kat.app.entity.mob.*;
import org.kat.app.entity.npc.Npc_CorruptedHustStudent;
import org.kat.app.entity.object.*;
import org.kat.app.graphics.AssetPool;
import org.kat.app.util.ResourceLoader;
import org.kat.app.map.GameMap;

import java.awt.*;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.kat.app.main.GamePanel.sManager;

public class AssetSetter {
    GameMap mp;
    Level lvl;
    StringBuilder filePathObject;
    StringBuilder filePathEnemy;
    StringBuilder filePathNpc;

    public AssetSetter(GameMap mp, Level lvl)
    {
        this.mp = mp;
        this.lvl = lvl;
    }

    public void setObject() throws IOException
    {
        try (Reader reader = ResourceLoader.getReader(filePathObject.toString())) {
            Gson gson = new Gson();

            Map<String, List<GameObject>> data = gson.fromJson(reader, new TypeToken<Map<String, List<GameObject>>>() {
            }.getType());

            List<GameObject> player = data.get("player");
            List<GameObject> activeObjects = data.get("active");
            List<GameObject> inactiveObjects = data.get("inactive");

            mp.player.setPosition(player.get(0).getX(), player.get(0).getY());
            sManager.setPos(player.get(0).getX(), player.get(0).getY());

            for (GameObject obj : inactiveObjects) {
                int X = obj.getPosition().getX();
                int Y = obj.getPosition().getY();
                Entity entity = switch (obj.getObject()) {
                    case "Obj_Tank" -> new Obj_Tank(obj.getState(),
                            obj.getType(),
                            obj.getName(),
                            X, Y);
                    case "Obj_Television" -> new Obj_Television(
                            obj.getState(),
                            obj.getSize(),
                            obj.getFrame(),
                            obj.getType(),
                            obj.getName(),
                            X, Y);
                    case "Obj_Chair" -> new Obj_Chair(obj.getDirection(),
                            obj.getType(),
                            obj.getName(),
                            X, Y);
                    case "Obj_PasswordAuth" -> new Obj_PasswordAuth(
                            obj.getState(),
                            obj.getName(),
                            X, Y);
                    case "Obj_Bin" -> new Obj_Bin(
                            obj.getType(),
                            obj.getName(),
                            X, Y);
                    case "Obj_Computer" -> new Obj_Computer(
                            obj.getState(),
                            obj.getDirection(),
                            obj.getName(),
                            X, Y);
                    case "Obj_Vault" -> new Obj_Vault(
                            obj.getState(),
                            obj.getType(),
                            obj.getName(),
                            X, Y);
                    default -> null;
                };
                mp.addObject(entity, mp.inactiveObj);
                assert entity != null;
                lvl.entityManager.add(entity.idName, entity);
            }
//
            for (GameObject obj : activeObjects) {
                int X = obj.getX();
                int Y = obj.getY();
                Entity entity = null;
                switch (obj.getObject()) {
                    case "Obj_Door":
                        entity = new Obj_Door(
                                obj.getSize(),
                                obj.getState(),
                                obj.getName(),
                                X, Y);
                        break;
                    case "Obj_Chest":
                        Obj_Chest chest = new Obj_Chest(
                                mp,
                                obj.getName(),
                                X, Y);
                        for (ItemStat item : obj.getItems()) {
                            switch (item.getName()) {
                                case "Item_Kit":
                                    chest.setLoot(new Item_Kit(), item.getQuantity());
                                    break;
                                case "Item_Battery":
                                    chest.setLoot(new Item_Battery(), item.getQuantity());
                                    break;
                                case "Item_StrengthGem":
                                    chest.setLoot(new Item_StrengthGem(), item.getQuantity());
                                    break;
                                case "Item_Potion":
                                    chest.setLoot(new Item_Potion(), item.getQuantity());
                                    break;
                            }
                        }
                        chest.setDialogue();
                        entity = chest;
                        break;
                }
                mp.addObject(entity, mp.activeObj);
                assert entity != null;
                lvl.entityManager.add(entity.idName, entity);
            }
        } catch (Exception e) {
            System.out.println("Nào, sai tên file hay sai gì đó trong file json rồi kìa!");
        }
    }

    public void setNpc() throws IOException
    {
        try(Reader reader = ResourceLoader.getReader(filePathNpc.toString())){
            Gson gson = new Gson();
            Map<String, List<NpcStat>> data = gson.fromJson(reader, new TypeToken<Map<String, List<NpcStat>>>() {}.getType());

            List<NpcStat> Npc = data.get("root");
            for(NpcStat npc : Npc){
                int X = npc.getX();
                int Y = npc.getY();
                Npc_CorruptedHustStudent entity;
                entity = new Npc_CorruptedHustStudent(mp,
                        npc.getName(),
                        npc.getDirection(),
                        npc.getDialogue(),
                        X , Y);
                mp.addObject(entity, mp.npc);
                lvl.entityManager.add(entity.idName, entity);
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setEnemy() throws IOException {
        try (Reader reader = ResourceLoader.getReader(filePathEnemy.toString())) {
            Gson gson = new Gson();

            Map<String, List<List<EnemyStat>>> data = gson.fromJson(reader,
                    new TypeToken< Map<String,List<List<EnemyStat>>> >() {}.getType());

            List<List<EnemyStat>> roomList = data.get("room");

            StringBuilder roomName = new StringBuilder();

            int roomCount = 0;

            for (List<EnemyStat> room : roomList) {
                roomCount++;

                roomName.setLength(0);
                roomName.append("Room")
                        .append(roomCount);

                RoomTask roomTask = new RoomTask(lvl);
                for (EnemyStat enemy : room) {
                    int X = enemy.getPosition().getX();
                    int Y = enemy.getPosition().getY();
                    Monster mon = null;

                    switch (enemy.getEnemy()) {
                        case "Mon_Cyborgon":
                            mon = new Mon_Cyborgon(mp, enemy.getName(), X, Y);
                            roomTask.addEnemy(mon);
                            break;
                        case "Mon_HustGuardian":
                            mon = new Mon_HustGuardian(mp, enemy.getName(), X, Y);
                            roomTask.addEnemy(mon);
                            break;
                        case "Mon_Spectron":
                            mon = new Mon_Spectron(mp, enemy.getName(), X, Y);
                            roomTask.addEnemy(mon);
                            break;
                        case "Mon_Shooter":
                            if (enemy.getDetection() == null) {
                                mon = new Mon_Shooter(mp, enemy.getDirection(),
                                        enemy.getType(), enemy.isAlwaysUp(),
                                        enemy.getAttackCycle(), enemy.getName(),
                                        X, Y);
                                roomTask.addEnemy(mon);
                            } else {
                                Rectangle detect = new Rectangle(
                                        enemy.getDetection().getX(),
                                        enemy.getDetection().getY(),
                                        enemy.getDetection().getWidth(),
                                        enemy.getDetection().getHeight()
                                );
                                mon = new Mon_Shooter(mp, enemy.getDirection(),
                                        enemy.getType(), enemy.isAlwaysUp(),
                                        enemy.getAttackCycle(), detect,
                                        enemy.getName(), X, Y);
                                roomTask.addEnemy(mon);
                            }
                            break;
                        case "Mon_EffectDealer":
                            Effect effect = null;
                            int W = enemy.getSizeWidth();
                            int H = enemy.getSizeHeight();
                            String effectType = enemy.getEffect().getEffectType();
                            int duration = enemy.getEffect().getDuration();
                            effect = switch (effectType) {
                                case "Slow" -> new Slow(mp.player, duration);
                                case "SpeedBoost" -> new Speed(mp.player, duration);
                                case "Strength" -> new Strength(mp.player, duration, 2);
                                case "Blind" -> new Blind(mp.player, duration);
                                default -> effect;
                            };
                            mon = new Mon_EffectDealer(mp, effect, enemy.getName(), X, Y, W, H);
                            roomTask.addEnemy(mon);
                            break;
                        case "Mon_Boss":
                            PathFinder2 finder = new PathFinder2(mp);
                            Mon_Boss boss = new Mon_Boss(mp, enemy.getName(), X, Y);
                            mon = boss;
                            boss.setPathFinder(finder);
                            roomTask.addEnemy(boss);
                            break;
                    }
                    assert mon != null;
                    lvl.entityManager.add(mon.idName, mon);
                }

                lvl.addRoom(roomName.toString(), roomTask);
            }

        } catch (Exception e) {
            System.out.println("Không sao hết, chỉ là bàn này không có quái thôi");
            e.printStackTrace();
        }
    }

    public void loadAll(){
        try {
            if (filePathObject != null) setObject();
            if (filePathNpc != null) setNpc();
            if (filePathEnemy != null) setEnemy();
        } catch(IOException ioe){
            throw new RuntimeException("Unknown error in AssetSetter");
        }
    }

    public void setFilePathObject(String filePathObject){
        if(this.filePathObject == null){
            this.filePathObject = new StringBuilder();
        } else {
            this.filePathObject.setLength(0);
        }
        this.filePathObject.append(filePathObject);
    }
    public String getFilePathObject(){return filePathObject.toString();}
    public void setFilePathEnemy(String filePathEnemy){
        if(this.filePathEnemy == null){
            this.filePathEnemy = new StringBuilder();
        } else {
            this.filePathEnemy.setLength(0);
        }
        this.filePathEnemy.append(filePathEnemy);
    }
    public String getFilePathEnemy(){return filePathEnemy.toString();}
    public void setFilePathNpc(String filePathNpc){
        if(this.filePathNpc == null){
            this.filePathNpc = new StringBuilder();
        } else {
            this.filePathNpc.setLength(0);
        }
        this.filePathNpc.append(filePathNpc);
    }

    public void dispose(){
        this.filePathObject = null;
        this.filePathNpc = null;
        this.filePathEnemy = null;
        mp = null;
    }
}
