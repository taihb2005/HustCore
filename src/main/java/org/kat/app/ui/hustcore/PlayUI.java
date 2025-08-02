package org.kat.app.ui.hustcore;

import org.kat.app.entity.items.Item;
import org.kat.app.main.GameState;
import org.kat.app.main.KeyHandler;
import org.kat.app.main.UI;
import org.kat.app.ui.views.Bar;
import org.kat.app.ui.views.Inventory;
import org.kat.app.ui.views.UIScreen;
import org.kat.app.ui.views.View;
import org.kat.app.util.Tree;

import java.awt.*;

import static org.kat.app.main.GamePanel.gameState;
import static org.kat.app.main.KeyHandler.*;

public class PlayUI extends UIScreen {
    private Bar playerHPBar;
    private Bar playerManaBar;
    private Inventory playerInventory;
    public PlayUI(String id, Tree<View> viewTree) {
        super(id, viewTree);
    }

    @Override
    protected void onCreate() {
        playerHPBar = (Bar) findViewById("hpBar");
        playerManaBar = (Bar) findViewById("manaBar");
        playerInventory =(Inventory) findViewById("inventory");
    }

    @Override
    public void onLeave(){
        UI._UIManager.setCurrentScreen("setting_pause");
        gameState = GameState.PAUSE;
    }

    @Override
    public void onShow(){

    }

    @Override
    public void handleKeyNavigation(){
        if (key1pressed) {
            key1pressed = false;
            playerInventory.setSelectedSlot(0);
        } else if (key2pressed) {
            key2pressed = false;
            playerInventory.setSelectedSlot(1);
        } else if (KeyHandler.key3pressed) {
            KeyHandler.key3pressed = false;
            playerInventory.setSelectedSlot(2);
        } else if (KeyHandler.key4pressed) {
            KeyHandler.key4pressed = false;
            playerInventory.setSelectedSlot(3);
        } else if (KeyHandler.key5pressed) {
            KeyHandler.key5pressed = false;
            playerInventory.setSelectedSlot(4);
        } else if(keyEpressed){
            keyEpressed = false;
            playerInventory.toggle();
        }
        super.handleKeyNavigation();
    }


    public void setMaxPlayerHP(int maxHP){
        playerHPBar.setMaxValue(maxHP);
    }

    public void setCurrentPlayerHP(int currentHP){
        playerHPBar.setCurrentValue(currentHP);
    }

    public void setMaxPlayerMana(int maxMana){
        playerManaBar.setMaxValue(maxMana);
    }

    public void setCurrentPlayerMana(int currentMana){
        playerManaBar.setCurrentValue(currentMana);
    }

    public void setPlayerInventory(Item[] inventory){
        playerInventory.setInventory(inventory);
    }

    @Override
    public void render(Graphics2D g2){
        if(playerHPBar != null) playerHPBar.render(g2);
        if(playerManaBar != null) playerManaBar.render(g2);
        if(playerInventory != null) playerInventory.render(g2);
    }
}
