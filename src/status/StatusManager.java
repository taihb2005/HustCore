package status;

import entity.items.*;

import java.util.Arrays;

public class StatusManager {
    private int worldX;
    private int worldY;
    private int savedHP;
    private int savedMana;
    private int savedLevel;
    private int savedExp;
    private final Item [] savedInventory = new Item[5];
    private String direction;

    public StatusManager(){
        worldX = 0; worldY = 0;
        savedHP = 150;
        savedMana = 100;
        savedLevel = 1;
        savedExp = 0;
        direction = "right";
    }

    public void reset(){
        worldX = 0; worldY = 0;
        savedHP = 150;
        savedMana = 100;
        savedLevel = 1;
        savedExp = 0;
        direction = "right";
    }

    public void setPos(int x , int y){worldX = x; worldY = y;};
    public void setSavedHP(int HP){savedHP = HP;}
    public void setSavedMana(int mana){savedMana = mana;}
    public void setLevel(int level){savedLevel = level;}
    public void setExp(int exp){savedExp = exp;}
    public void setInventory(Item[] item)
    {
        for(int i = 0 ; i < item.length ; i++){
            if(item[i] == null) continue;
            Item i_tmp;
            StringBuilder name = item[i].getName();
            if(name.compareTo(new StringBuilder("Pin năng lượng")) == 0){
                i_tmp = new Item_Battery();
            } else
            if(name.compareTo(new StringBuilder("Bộ cứu thương")) == 0){
                i_tmp = new Item_Kit();
            } else
            if(name.compareTo(new StringBuilder("Thuốc giải")) == 0){
                i_tmp = new Item_Potion();
            } else i_tmp = new Item_SpeedGem();
            i_tmp.setQuantity(item[i].getQuantity());
            savedInventory[i] = i_tmp;
        }
    }
    public void setDirection(String dir){direction = dir;}

    public int getWorldX(){return worldX;}
    public int getWorldY(){return worldY;}
    public int getSavedHP(){return savedHP;}
    public int getSavedMana(){return savedMana;}
    public int getSavedLevel(){return savedLevel;}
    public int getSavedExp(){return savedExp;}
    public void getSavedInventory(Item[] item){
        for(int i = 0 ; i < savedInventory.length ; i++){
            if(savedInventory[i] == null) continue;
            Item i_tmp;
            StringBuilder name = savedInventory[i].getName();
            if(name.compareTo(new StringBuilder("Pin năng lượng")) == 0){
                i_tmp = new Item_Battery();
            } else
            if(name.compareTo(new StringBuilder("Bộ cứu thương")) == 0){
                i_tmp = new Item_Kit();
            } else
            if(name.compareTo(new StringBuilder("Thuốc giải")) == 0){
                i_tmp = new Item_Potion();
            } else i_tmp = new Item_SpeedGem();
            i_tmp.setQuantity(savedInventory[i].getQuantity());
            item[i] = i_tmp;
        }
    }
    public String getDirection(){return direction;}

}
