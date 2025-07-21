package org.kat.app.status;

import org.kat.app.entity.items.*;
import org.kat.app.util.Vector2D;

public class StatusManager {
    private Vector2D position;
    private int savedHP;
    private int savedMana;
    private int savedLevel;
    private int savedExp;
    private final Item [] savedInventory = new Item[5];
    private String direction;

    public StatusManager(){
        position = new Vector2D();
        savedHP = 150;
        savedMana = 100;
        savedLevel = 1;
        savedExp = 0;
        direction = "right";
    }

    public void reset(){
        position = new Vector2D();
        savedHP = 150;
        savedMana = 100;
        savedLevel = 1;
        savedExp = 0;
        direction = "right";
    }

    public void setPos(Vector2D pos){
        this.position = pos.copy();
    };

    public void setPos(float x, float y){
        position = new Vector2D(x, y);
    };

    public void setSavedHP(int HP){savedHP = HP;}
    public void setSavedMana(int mana){savedMana = mana;}
    public void setLevel(int level){savedLevel = level;}
    public void setExp(int exp){savedExp = exp;}
    public void setInventory(Item[] item)
    {
        for(int i = 0 ; i < item.length ; i++){
            if(item[i] == null) continue;

            int quantity = item[i].getQuantity();
            Item i_tmp;

            if (item[i] instanceof Item_Battery) {
                i_tmp = new Item_Battery();
            } else if (item[i] instanceof Item_Kit) {
                i_tmp = new Item_Kit();
            } else if (item[i] instanceof Item_Potion) {
                i_tmp = new Item_Potion();
            } else {
                i_tmp = new Item_StrengthGem();
            }

            i_tmp.setQuantity(quantity);
            savedInventory[i] = i_tmp;
        }
    }
    public void setDirection(String dir){direction = dir;}

    public float getWorldX(){return position.x;}
    public float getWorldY(){return position.y;}
    public Vector2D getPosition(){return position.copy();}
    public int getSavedHP(){return savedHP;}
    public int getSavedMana(){return savedMana;}
    public int getSavedLevel(){return savedLevel;}
    public int getSavedExp(){return savedExp;}
    public void getSavedInventory(Item[] item){
        for (int i = 0; i < savedInventory.length; i++) {
            if (savedInventory[i] == null) continue;

            Item original = savedInventory[i];
            Item copy;

            if (original instanceof Item_Battery) {
                copy = new Item_Battery();
            } else if (original instanceof Item_Kit) {
                copy = new Item_Kit();
            } else if (original instanceof Item_Potion) {
                copy = new Item_Potion();
            } else if (original instanceof Item_StrengthGem) {
                copy = new Item_StrengthGem();
            } else {
                copy = new Item_StrengthGem();
            }

            copy.setQuantity(original.getQuantity());
            item[i] = copy;
        }

    }
    public String getDirection(){return direction;}

}
