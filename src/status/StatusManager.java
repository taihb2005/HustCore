package status;

import entity.items.Item;

import java.util.Arrays;

public class StatusManager {
    private int worldX;
    private int worldY;
    private int savedHP;
    private int savedMana;
    private int savedLevel;
    private int savedExp;
    private final Item [] savedInventory = new Item[100];
    private String direction;

    public StatusManager(){
        worldX = 0; worldY = 0;
        savedHP = 100;
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
    public void setInventory(Item[] item){
        Arrays.fill(savedInventory , null);
        for(int i = 0 ; i < item.length ; i++){
            if(item[i] != null) savedInventory[i] = item[i];
        }
    }
    public void setDirection(String dir){direction = dir;}

    public int getWorldX(){return worldX;}
    public int getWorldY(){return worldY;}
    public int getSavedHP(){return savedHP;}
    public int getSavedMana(){return savedMana;}
    public int getSavedLevel(){return savedLevel;}
    public int getSavedExp(){return savedExp;}
    public Item[] getSavedInventory(){return savedInventory;}
    public String getDirection(){return direction;}

}
