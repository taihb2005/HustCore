package level;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

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

            List<GameObject> activeObjects = data.get("active");
            List<GameObject> inactiveObjects = data.get("inactive");

            for (GameObject obj : inactiveObjects) {
                int X = obj.getX();
                int Y = obj.getY();
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
                        Obj_Chest chest = new Obj_Chest(mp);
                        chest.worldX = obj.getX();
                        chest.worldY = obj.getY();
                        for (ItemStat item : obj.getItems()) {
                            switch (item.getName()) {
                                case "Item_Kit":
                                    chest.setLoot(new Item_Kit(), item.getQuantity());
                                    chest.setDialogue();
                                    break;
                                case "Item_Battery":
                                    chest.setLoot(new Item_Battery(), item.getQuantity());
                                    chest.setDialogue();
                                    break;
                                case "Item_SpeedGem":
                                    chest.setLoot(new Item_SpeedGem() , item.getQuantity());
                                    chest.setDialogue();
                                    break;
                                case "Item_Potion":
                                    chest.setLoot(new Item_Potion() , item.getQuantity());
                                    chest.setDialogue();
                                    break;
                            }
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
                int X = enemy.getX();
                int Y = enemy.getY();
                switch (enemy.getEnemy()) {
                    case "Obj_Cyborgon":
                        mp.addObject(new Mon_Cyborgon(mp  , X , Y), mp.enemy);
                        break;
                    case "Obj_HustGuardian":
                        mp.addObject(new Mon_HustGuardian(mp , X , Y), mp.enemy);
                        break;
                    case "Obj_Spectron":
                        mp.addObject(new Mon_Spectron(mp , X , Y), mp.enemy);
                        break;
                    case "Obj_Shooter":
                        String directions = enemy.getDirection();
                        mp.addObject( new Mon_Shooter(mp, directions, enemy.getType(), enemy.isAlwaysUp(), enemy.getAttackCycle(), enemy.getX(), enemy.getY()
                        ), mp.enemy);
                }
            }

        } catch (Exception e) {
            System.out.println("Không sao hết, chỉ là bàn này không có quái thôi");
        }
    }

    public void loadAll(){
        setObject();
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
