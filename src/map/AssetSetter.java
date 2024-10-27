package map;

import entity.object.Obj_FilledTank;

public class AssetSetter {
    GameMap mp;

    public AssetSetter(GameMap mp)
    {
        this.mp = mp;
    }

    public void setObject()
    {
        Obj_FilledTank filledTank_no1 = new Obj_FilledTank();
        filledTank_no1.worldX = 22 * 64; filledTank_no1.worldY = 25 * 64;
        mp.inactiveObj.add(filledTank_no1);
    }

    public void setNpc()
    {

    }
}
