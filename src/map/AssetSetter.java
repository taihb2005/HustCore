package map;

import entity.mob.Mon_Shooter;
import entity.mob.Mon_Spectron;
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

    }

    public void setNpc()
    {
        int index = 0;
        Npc_CorruptedHustStudent npc1 = new Npc_CorruptedHustStudent(mp);
        npc1.worldX = 1300;
        npc1.worldY = 1700;
        npc1.dialogues[0][0] = "Xin chào!";
        npc1.dialogues[0][1] = "Nhìn bạn trông quen lắm mà \ntôi không nhận ra ai...";
        npc1.dialogues[0][2] = "Anyway, nơi này đã từng là \nmột Đại học danh giá của Việt Nam\n" +
                            "bằng một thế lực nào đó mà \nmọi người biến thành hết!";
        npc1.dialogues[0][3] = "Những sinh viên ở đây để bị \nbiến thành những con robot giống\n" +
                            "nhau. Cơ mà để phân biệt được \nthì chỉ còn cách dựa vào giọng.";
        npc1.dialogues[0][4] = "Chúng minh cũng muốn biến lại \nnhư cũ lắm nhưng đó là điều khó.";
        npc1.dialogues[0][5] = "Chúng minh đã từng nhờ nhiều \nngười nhưng kết cục đều là bị biến\n" +
                            "thành như thế này!";
        npc1.dialogues[0][6] = "Nếu bạn biến được chúng \nmình về như cũ thì sẽ tốt biết bao...";

        npc1.dialogues[1][0] = "Bạn biết con rùa rụt cổ không\nvà chem chép không?";
        npc1.dialogues[1][1] = "Nó trú ngụ ở trung tâm của phòng\nđiều khiển";
        npc1.dialogues[1][2] = "Tiêu diệt nó sẽ khiến bạn có\nthêm manh mối!";
        npc1.dialogues[1][3] = "Chúc bạn may mắn!";
        mp.addObject(npc1 , mp.npc);

        Npc_CorruptedHustStudent npc2 = new Npc_CorruptedHustStudent(mp);
        npc2.worldX = 1500;
        npc2.worldY = 1400;
        npc2.dialogues[0][0] = "Có vẻ bạn là người mới ở đây...";
        npc2.dialogues[0][1] = "Nơi này rất nguy hiểm.\n Nó đầy rẫy những con robot cảnh vệ...";
        npc2.dialogues[0][2] = "Chúc bạn may mắn sống sót trở \nvề...";
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


        Mon_Shooter shooter = new Mon_Shooter(mp);
        shooter.worldX = 1400;
        shooter.worldY = 1500;
        shooter.newWorldX = 1400;
        shooter.newWorldY = 1500;
        mp.enemy[index] = shooter;
        index++;
    }
}
