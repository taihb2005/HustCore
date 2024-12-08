package level;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entity.effect.Effect;
import entity.effect.type.Blind;
import entity.effect.type.Slow;
import entity.effect.type.SpeedBoost;
import entity.items.Item_Battery;
import entity.items.Item_Kit;
import entity.items.Item_Potion;
import entity.items.Item_SpeedGem;
import entity.json_stat.GameObject;
import entity.json_stat.ItemStat;
import entity.json_stat.EnemyStat;
import entity.json_stat.NpcStat;
import entity.mob.*;
import entity.npc.Npc_CorruptedHustStudent;
import entity.object.*;
import map.GameMap;

import java.awt.*;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import static main.GamePanel.currentMap;
import static main.GamePanel.sManager;

public class AssetSetter {
    GameMap mp;
    String filePathObject;
    String filePathEnemy;
    String filePathNpc;
    int phaseCount;
    public AssetSetter(GameMap mp)
    {
        this.mp = mp;
    }

    public void setObject()
    {
        try {
            Reader reader = new FileReader(filePathObject);
            Gson gson = new Gson();

            Map<String, List<GameObject>> data = gson.fromJson(reader, new TypeToken<Map<String, List<GameObject>>>() {}.getType());

            List<GameObject> player = data.get("player");
            List<GameObject> activeObjects = data.get("active");
            List<GameObject> inactiveObjects = data.get("inactive");

            mp.player.setPosition(player.get(0).getX() , player.get(0).getY());
            sManager.setPos(player.get(0).getX() , player.get(0).getY());

            for (GameObject obj : inactiveObjects) {
                int X = obj.getPosition().getX();
                int Y = obj.getPosition().getY();
                switch(obj.getObject()) {
                    case "Obj_Tank":
                        mp.addObject(new Obj_Tank(obj.getState() ,obj.getType() , X , Y), mp.inactiveObj);
                        break;
                    case "Obj_Television":
                        mp.addObject(new Obj_Television(obj.getState() , obj.getSize(), obj.getFrame(),obj.getType() , X , Y), mp.inactiveObj);
                        break;
                    case "Obj_Chair":
                        mp.addObject(new Obj_Chair(obj.getDirection(), obj.getType() , X , Y), mp.inactiveObj);
                        break;
                    case "Obj_PasswordAuth":
                        mp.addObject(new Obj_PasswordAuth(obj.getState() , X , Y) , mp.inactiveObj);
                        break;
                    case "Obj_Bin":
                        mp.addObject(new Obj_Bin(obj.getType() , X , Y) , mp.inactiveObj);
                        break;
                    case "Obj_Computer":
                        mp.addObject(new Obj_Computer(obj.getState() , obj.getDirection() , X ,  Y) , mp.inactiveObj);
                        break;
                    case "Obj_Vault":
                        mp.addObject(new Obj_Vault(obj.getState() , obj.getType() , X , Y) , mp.inactiveObj);
                        break;
                }
            }
//
            for (GameObject obj : activeObjects) {
                int X = obj.getX();
                int Y = obj.getY();
                switch (obj.getObject()) {
                    case "Obj_Door":
                        Obj_Door door = new Obj_Door(obj.getSize(), obj.getState() , obj.getName() ,X , Y);
                        mp.addObject(door, mp.activeObj);
                        break;
                    case "Obj_Chest":
                        Obj_Chest chest = new Obj_Chest(mp , X , Y , obj.getName());
                        for (ItemStat item : obj.getItems()) {
                            switch (item.getName()) {
                                case "Item_Kit":
                                    chest.setLoot(new Item_Kit(), item.getQuantity());
                                    break;
                                case "Item_Battery":
                                    chest.setLoot(new Item_Battery(), item.getQuantity());
                                    break;
                                case "Item_SpeedGem":
                                    chest.setLoot(new Item_SpeedGem() , item.getQuantity());
                                    break;
                                case "Item_Potion":
                                    chest.setLoot(new Item_Potion() , item.getQuantity());
                                    break;
                            }
                            chest.setDialogue();
                        }
                        chest.setDialogue();
                        mp.addObject(chest, mp.activeObj);
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nào, sai tên file hay sai gì đó trong file json rồi kìa!");
            e.printStackTrace();
        }
    }

    public void setNpc()
    {
        try{
            Reader reader = new FileReader(filePathNpc);
            Gson gson = new Gson();
            Map<String, List<NpcStat>> data = gson.fromJson(reader, new TypeToken<Map<String, List<NpcStat>>>() {}.getType());

            List<NpcStat> Npc = data.get("root");
            for(NpcStat npc : Npc){
                int X = npc.getX();
                int Y = npc.getY();
                mp.addObject(new Npc_CorruptedHustStudent(mp , npc.getName() ,npc.getDirection() , npc.getDialogue() , X , Y), mp.npc);
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setEnemy(){
        try {
            Reader reader = new FileReader(filePathEnemy);
            Gson gson = new Gson();

            Map<String, List<EnemyStat>> data = gson.fromJson(reader, new TypeToken<Map<String, List<EnemyStat>>>() {}.getType());

            List<EnemyStat> Enemies = data.get("root");

            for (EnemyStat enemy : Enemies) {
                int X = enemy.getPosition().getX();
                int Y = enemy.getPosition().getY();
                switch (enemy.getEnemy()) {
                    case "Mon_Cyborgon":
                        mp.addObject(new Mon_Cyborgon(mp  , X , Y , enemy.getName()), mp.enemy);
                        break;
                    case "Mon_HustGuardian":
                        mp.addObject(new Mon_HustGuardian(mp , X , Y , enemy.getName()), mp.enemy);
                        break;
                    case "Mon_Spectron":
                        mp.addObject(new Mon_Spectron(mp , X , Y , enemy.getName()), mp.enemy);
                        break;
                    case "Mon_Shooter":
                        if(enemy.getDetection() == null) {
                            mp.addObject(new Mon_Shooter(mp, enemy.getDirection(), enemy.getType(), enemy.isAlwaysUp(), enemy.getAttackCycle(), enemy.getName() ,enemy.getX(), enemy.getY()
                            ), mp.enemy);
                        } else
                        {
                            Rectangle detect = new Rectangle(enemy.getDetection().getX() , enemy.getDetection().getY() , enemy.getDetection().getWidth() , enemy.getDetection().getHeight());
                            mp.addObject(new Mon_Shooter(mp, enemy.getDirection(), enemy.getType(), enemy.isAlwaysUp(), enemy.getAttackCycle(), detect , enemy.getName() ,enemy.getX(), enemy.getY()
                            ), mp.enemy);
                        };
                        break;
                    case "Mon_EffectDealer":
                        Effect effect = null;
                        String effectType = enemy.getEffect().getEffectType();
                        int duration = enemy.getEffect().getDuration();
                        switch(effectType){
                            case "Slow": effect = new Slow(mp.player , duration); break;
                            case "SpeedBoost": effect = new SpeedBoost(mp.player , duration); break;
                            case "Blind": effect = new Blind(mp.player , duration);
                        }
                        mp.addObject(new Mon_EffectDealer(mp , effect ,enemy.getX() , enemy.getY()) , mp.enemy);
                }
            }

        } catch (Exception e) {
            System.out.println("Không sao hết, chỉ là bàn này không có quái thôi");
            e.printStackTrace();
        }
    }

    public void loadAll(){
        if(filePathObject != null) setObject();
        if(filePathNpc != null) setNpc();
        if(filePathEnemy != null) setEnemy();
    }

    public void setFilePathObject(String filePathObject){this.filePathObject = filePathObject;}
    public String getFilePathObject(){return filePathObject;}
    public void setFilePathEnemy(String filePathEnemy){this.filePathEnemy = filePathEnemy;}
    public String getFilePathEnemy(){return filePathEnemy;}
    public void setFilePathNpc(String filePathNpc){this.filePathNpc = filePathNpc;}
    public String getFilePathNpc(){return filePathNpc;}

    public void setPhase(int p){this.phaseCount = p;}
    public int getPhase(){return phaseCount;}
}
