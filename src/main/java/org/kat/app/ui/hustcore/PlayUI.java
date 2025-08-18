package org.kat.app.ui.hustcore;

import org.kat.app.entity.items.Item;
import org.kat.app.level.LevelState;
import org.kat.app.level.progress.level04.Level04;
import org.kat.app.main.GamePanel;
import org.kat.app.main.GameState;
import org.kat.app.main.KeyHandler;
import org.kat.app.main.UI;
import org.kat.app.ui.views.*;
import org.kat.app.util.Tree;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.kat.app.main.GamePanel.currentLevel;
import static org.kat.app.main.GamePanel.gameState;
import static org.kat.app.main.KeyHandler.*;

public class PlayUI extends UIScreen {
    private Bar playerHPBar;
    private Bar playerManaBar;
    private Bar bossHPBar;
    private TextView bossBarName;
    private Inventory playerInventory;

    public PlayUI(String id, Tree<View> viewTree) {
        super(id, viewTree);
    }

    @Override
    protected void onCreate() {
        playerHPBar = (Bar) findViewById("hpBar");
        playerManaBar = (Bar) findViewById("manaBar");
        bossHPBar = (Bar) findViewById("bossHpBar");
        bossBarName = (TextView) findViewById("bossBarName");
        playerInventory = (Inventory) findViewById("inventory");

        setDarknessFilter(true);
    }

    @Override
    public void onLeave(){
        if(currentLevel.checkState(LevelState.RUNNING) && gameState == GameState.PLAY) {
            gameState = GameState.PAUSE;
            UI._UIManager.setCurrentScreen("setting_pause");
        }
    }

    @Override
    public void onShow(){

    }

    @Override
    public void handleKeyNavigation(){
        if(keyEpressed){
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

    public void setCurrentBossHP(int bossHP){
        bossHPBar.setCurrentValue(bossHP);
    }

    public void setMaxBossHP(int maxHP){
        bossHPBar.setMaxValue(maxHP);
    }

    public void setPlayerInventory(Item[] inventory){
        playerInventory.setInventory(inventory);
    }

    @Override
    public void render(Graphics2D g2){
        if(playerHPBar != null) playerHPBar.render(g2);
        if(playerManaBar != null) playerManaBar.render(g2);
        if(playerInventory != null) playerInventory.render(g2);
        if(currentLevel instanceof Level04) {
            bossBarName.render(g2);
            bossHPBar.render(g2);
        }
    }
}
