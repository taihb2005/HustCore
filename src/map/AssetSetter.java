package map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entity.items.Item_Battery;
import entity.items.Item_Kit;
import entity.json_stat.GameObject;
import entity.json_stat.ItemStat;
import entity.mob.*;
import entity.npc.Npc_CorruptedHustStudent;
import entity.object.*;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public class AssetSetter {
    GameMap mp;

    public AssetSetter(GameMap mp)
    {
        this.mp = mp;
    }

    public void setObject()
    {
        try {
            Reader reader = new FileReader("res/entity/object.json");
            Gson gson = new Gson();

            Map<String, List<GameObject>> data = gson.fromJson(reader, new TypeToken<Map<String, List<GameObject>>>() {}.getType());

            List<GameObject> activeObjects = data.get("active");
            List<GameObject> inactiveObjects = data.get("inactive");

            for (GameObject obj : inactiveObjects) {
                System.out.println();
                switch(obj.getObject()) {
                    case "Obj_FilledTank":
                        Obj_FilledTank filledTank = new Obj_FilledTank(obj.getType());
                        filledTank.worldX = obj.getX(); filledTank.worldY = obj.getY();
                        mp.addObject(filledTank, mp.inactiveObj);
                        break;
                    case "Obj_EmptyTank":
                        Obj_EmptyTank emptyTank = new Obj_EmptyTank();
                        emptyTank.worldX = obj.getX(); emptyTank.worldY = obj.getY();
                        mp.addObject(emptyTank, mp.inactiveObj);
                        break;
                    case "Obj_Television":
                        Obj_Television tv = new Obj_Television(obj.getType());
                        tv.worldX = obj.getX(); tv.worldY = obj.getY();
                        mp.addObject(tv, mp.inactiveObj);
                        break;
                    case "Obj_Desk":
                        Obj_Desk desk = new Obj_Desk(obj.getType());
                        desk.worldX = obj.getX(); desk.worldY = obj.getY();
                        mp.addObject(desk, mp.inactiveObj);
                        break;
                    case "Obj_Chair":
                        Obj_Chair chair = new Obj_Chair(obj.getType());
                        chair.worldX = obj.getX(); chair.worldY = obj.getY();
                        mp.addObject(chair, mp.inactiveObj);
                        break;
                }
            }
//
            for (GameObject obj : activeObjects) {
                switch (obj.getObject()) {
                    case "Obj_Door":
                        Obj_Door door = new Obj_Door(obj.getDoorType(), obj.getType());
                        door.worldX = obj.getX();
                        door.worldY = obj.getY();
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
                            }
                        }
                        chest.setDialogue();
                        mp.addObject(chest, mp.activeObj);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNpc()
    {
//        int index = 0;
//        Npc_CorruptedHustStudent npc1 = new Npc_CorruptedHustStudent(mp);
//        npc1.worldX = 1300;
//        npc1.worldY = 1700;
//        npc1.dialogues[0][0] = "Xin chào!";
//        npc1.dialogues[0][1] = "Nhìn bạn trông quen lắm mà \ntôi không nhận ra ai...";
//        npc1.dialogues[0][2] = "Anyway, nơi này đã từng là \nmột Đại học danh giá của Việt Nam\n" +
//                            "bằng một thế lực nào đó mà \nmọi người biến thành hết!";
//        npc1.dialogues[0][3] = "Những sinh viên ở đây để bị \nbiến thành những con robot giống\n" +
//                            "nhau. Cơ mà để phân biệt được \nthì chỉ còn cách dựa vào giọng.";
//        npc1.dialogues[0][4] = "Chúng minh cũng muốn biến lại \nnhư cũ lắm nhưng đó là điều khó.";
//        npc1.dialogues[0][5] = "Chúng minh đã từng nhờ nhiều \nngười nhưng kết cục đều là bị biến\n" +
//                            "thành như thế này!";
//        npc1.dialogues[0][6] = "Nếu bạn biến được chúng \nmình về như cũ thì sẽ tốt biết bao...";
//
//        npc1.dialogues[1][0] = "Bạn biết con rùa rụt cổ không\nvà chem chép không?";
//        npc1.dialogues[1][1] = "Nó trú ngụ ở trung tâm của phòng\nđiều khiển";
//        npc1.dialogues[1][2] = "Tiêu diệt nó sẽ khiến bạn có\nthêm manh mối!";
//        npc1.dialogues[1][3] = "Chúc bạn may mắn!";
//        mp.addObject(npc1 , mp.npc);
//
//        Npc_CorruptedHustStudent npc2 = new Npc_CorruptedHustStudent(mp);
//        npc2.worldX = 1500;
//        npc2.worldY = 1400;
//        npc2.dialogues[0][0] = "Có vẻ bạn là người mới ở đây...";
//        npc2.dialogues[0][1] = "Nơi này rất nguy hiểm.\n Nó đầy rẫy những con robot cảnh vệ...";
//        npc2.dialogues[0][2] = "Chúc bạn may mắn sống sót trở \nvề...";
//        mp.addObject(npc2 , mp.npc);
    }

    public void setEnemy()
    {
        Mon_Shooter shooter = new Mon_Shooter(mp , "left" , 0 , true, 120 , 1700 , 1600);
        mp.addObject(shooter , mp.enemy);

        Mon_Cyborgon x = new Mon_Cyborgon(mp , 1500 , 1600);
        mp.addObject(x , mp.enemy);

    }

    public void loadAll(){
        setObject();
        setNpc();
        setEnemy();
    }
}
