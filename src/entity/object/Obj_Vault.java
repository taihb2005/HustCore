package entity.object;

import entity.Entity;
import graphics.AssetPool;
import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.camera;

public class Obj_Vault extends Entity {
    private BufferedImage obj_vault;

    public Obj_Vault(String state , int type, String idName, int x , int y){
        super(x , y);
        name = "Vault";
        this.idName = idName;
        width = 64;
        height = 64;
        solidArea1 = new Rectangle(6 , 37 , 50 , 23);
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        setDefaultSolidArea();

        obj_vault = AssetPool.getImage("vault_" + state + "_id" + type +".png");
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2) throws  ArrayIndexOutOfBoundsException{
        g2.drawImage(obj_vault , (int)position.x - camera.getX() , (int)position.y - camera.getY() , width , height , null);
    }

    public void dispose(){
        solidArea1 = null;
        solidArea2 = null;
        obj_vault.flush();
        obj_vault = null;

    }
}
