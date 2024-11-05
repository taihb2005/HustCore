package map;

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
        Obj_FilledTank filledTank_no1 = new Obj_FilledTank(1);
        filledTank_no1.worldX = 22 * 64; filledTank_no1.worldY = 25 * 64;
        mp.inactiveObj.add(filledTank_no1);

        Obj_FilledTank filledTank_no2 = new Obj_FilledTank(2);
        filledTank_no2.worldX = 26 * 64; filledTank_no2.worldY = 25 * 64;
        mp.inactiveObj.add(filledTank_no2);

        Obj_EmptyTank emptyTank_no1 = new Obj_EmptyTank();
        emptyTank_no1.worldX = 23 * 64 ; emptyTank_no1.worldY = 26 * 64;
        mp.inactiveObj.add(emptyTank_no1);

        Obj_Television tv_no1 = new Obj_Television(1);
        tv_no1.worldX = 1344 ; tv_no1.worldY = 1990;
        mp.inactiveObj.add(tv_no1);

        Obj_Desk desk_no1 = new Obj_Desk(1);
        desk_no1.worldX = 1537; desk_no1.worldY = 1553;
        mp.inactiveObj.add(desk_no1);

        Obj_Chair chair_no1 = new Obj_Chair(1);
        chair_no1.worldX = 1527 ; chair_no1.worldY = 1573;
        mp.inactiveObj.add(chair_no1);

        Obj_Door small_door = new Obj_Door(Obj_Door.SMALL_DOOR , 1);
        small_door.worldX = 22 * 64; small_door.worldY = 21 * 64;
        mp.activeObj.add(small_door);
    }

    public void setNpc()
    {
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
        mp.npc.add(npc1);

        Npc_CorruptedHustStudent npc2 = new Npc_CorruptedHustStudent(mp);
        npc2.worldX = 1500;
        npc2.worldY = 1400;
        npc2.dialogues[0] = "Có vẻ bạn là người mới ở đây...";
        npc2.dialogues[1] = "Nơi này rất nguy hiểm.\n Nó đầy rẫy những con robot cảnh vệ...";
        npc2.dialogues[2] = "Chúc bạn may mắn sống sót trở \nvề...";
        mp.npc.add(npc2);
    }

    public void setEnemy()
    {
        Mon_Spectron sptr = new Mon_Spectron(mp);
        sptr.worldX = 1400;
        sptr.worldY = 1800;
        sptr.newWorldX = 1400;
        sptr.newWorldY = 1800;
        mp.onAirEnemy.add(sptr);
    }
}
