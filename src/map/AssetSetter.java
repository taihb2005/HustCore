package map;

import entity.Entity;
import entity.items.Inventory;
import entity.mob.Mon_HustGuardian;
import entity.mob.Mon_Shooter;
import entity.mob.Mon_Spectron;
import entity.npc.Npc_CorruptedHustStudent;
import entity.object.*;

public class AssetSetter {
    GameMap mp;
    public AssetSetter(GameMap mp)
    {
        this.mp = mp;
    }

    public void setObject()
    {
//        Obj_FilledTank filledTank_no1 = new Obj_FilledTank(1);
//        filledTank_no1.worldX = 22 * 64; filledTank_no1.worldY = 25 * 64;
//        mp.addObject(filledTank_no1 , mp.inactiveObj);

//        Obj_FilledTank filledTank_no2 = new Obj_FilledTank(2);
//        filledTank_no2.worldX = 26 * 64; filledTank_no2.worldY = 25 * 64;
//        mp.addObject(filledTank_no2 , mp.inactiveObj);
//
//        Obj_EmptyTank emptyTank_no1 = new Obj_EmptyTank();
//        emptyTank_no1.worldX = 23 * 64 ; emptyTank_no1.worldY = 26 * 64;
//        mp.addObject(emptyTank_no1 , mp.inactiveObj);
//
//        Obj_Television tv_no1 = new Obj_Television(1);
//        tv_no1.worldX = 1344 ; tv_no1.worldY = 1990;
//        mp.addObject(tv_no1 , mp.inactiveObj);
//
//        Obj_Desk desk_no1 = new Obj_Desk(1);
//        desk_no1.worldX = 1537; desk_no1.worldY = 1553;
//        mp.addObject(desk_no1 , mp.inactiveObj);
//
//        Obj_Chair chair_no1 = new Obj_Chair(1);
//        chair_no1.worldX = 1527 ; chair_no1.worldY = 1573;
//        mp.addObject(chair_no1 , mp.inactiveObj);
//
//        //INTERACTIVE OBJ
        int index = 0;
        Obj_Door small_door = new Obj_Door(Obj_Door.BIG_DOOR , 1);
        small_door.worldX = 22 * 64; small_door.worldY = 21 * 64;
        mp.addObject(small_door , mp.activeObj);

//        Obj_Chest chest_no1 = new Obj_Chest(mp);
//        chest_no1.worldX = 1500; chest_no1.worldY = 1900;
//        mp.addObject(chest_no1 , mp.activeObj);

    }

    public void setNpc()
    {
        int index = 0;
        Npc_CorruptedHustStudent npc1 = new Npc_CorruptedHustStudent(mp);
        npc1.worldX = 1300;
        npc1.worldY = 1700;
        npc1.dialogues[0] = "Xin chào!";
        npc1.dialogues[1] = "Nhìn bạn trông quen lắm mà \ntôi không nhận ra ai...";
        npc1.dialogues[2] = "Anyway, nơi này đã từng là \nmột Đại học danh giá của Việt Nam\n" +
                            "bằng một thế lực nào đó mà \nmọi người biến thành hồ ly tinh hết!";
        npc1.dialogues[3] = "Những sinh viên ở đây để bị \nbiến thành những con robot giống\n" +
                            "nhau. Cơ mà để phân biệt được \nthì chỉ còn cách dựa vào giọng.";
        npc1.dialogues[4] = "Chúng minh cũng muốn biến lại \nnhư cũ lắm nhưng đó là điều khó.";
        npc1.dialogues[5] = "Chúng minh đã từng nhờ nhiều \nngười nhưng kết cục đều là bị biến\n" +
                            "thành như thế này!";
        npc1.dialogues[6] = "Nếu bạn biến được chúng \nmình về như cũ thì sẽ tốt biết bao...";
        mp.addObject(npc1 , mp.npc);

        Npc_CorruptedHustStudent npc2 = new Npc_CorruptedHustStudent(mp);
        npc2.worldX = 1500;
        npc2.worldY = 1400;
        npc2.dialogues[0] = "Có vẻ bạn là người mới ở đây...";
        npc2.dialogues[1] = "Nơi này rất nguy hiểm.\n Nó đầy rẫy những con robot cảnh vệ...";
        npc2.dialogues[2] = "Chúc bạn may mắn sống sót trở \nvề...";
        mp.addObject(npc2 , mp.npc);
    }

    public void setEnemy()
    {
        int index = 0;
        Mon_Spectron sptr = new Mon_Spectron(mp);
        sptr.worldX = 1600;
        sptr.worldY = 1800;
        sptr.newWorldX = 1600;
        sptr.newWorldY = 1800;
        mp.enemy[index] = sptr;
        index++;



    }
}
