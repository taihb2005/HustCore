package map;

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
        chair_no1.worldX = 1527 ; chair_no1.worldY = 1563;
        mp.inactiveObj.add(chair_no1);
    }

    public void setNpc()
    {

    }
}
