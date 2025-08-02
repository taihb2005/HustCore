package org.kat.app.ui.hustcore;

import org.kat.app.main.GamePanel;
import org.kat.app.main.UI;
import org.kat.app.ui.UIComponentListener;
import org.kat.app.ui.components.Button;
import org.kat.app.ui.components.ValueAdjuster;
import org.kat.app.ui.views.Cursor;
import org.kat.app.ui.views.UIScreen;
import org.kat.app.ui.views.View;
import org.kat.app.util.Tree;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SettingsMenu extends UIScreen {
    public SettingsMenu(String id, Tree<View> viewTree) {
        super(id, viewTree);
    }

    @Override
    protected void onCreate() {
        ValueAdjuster volumeAdjusterMusic =  (ValueAdjuster) findViewById("volumeAdjusterMusic");
        volumeAdjusterMusic.setValue(GamePanel.music.volumePercentage);
        volumeAdjusterMusic.setListener(new UIComponentListener() {
            @Override
            public void onPress() {

            }

            @Override
            public void onIncrease(){
                if (GamePanel.music.volumePercentage < volumeAdjusterMusic.getUpperThreshold()) {
                    GamePanel.music.volumePercentage += 10;
                    GamePanel.music.checkVolume(GamePanel.music.volumePercentage);

                    volumeAdjusterMusic.setValue(GamePanel.music.volumePercentage);
                }
            }

            @Override
            public void onDecrease(){
                if (GamePanel.music.volumePercentage > volumeAdjusterMusic.getLowerThreshold()) {
                    GamePanel.music.volumePercentage -= 10;
                    GamePanel.music.checkVolume(GamePanel.music.volumePercentage);

                    volumeAdjusterMusic.setValue(GamePanel.music.volumePercentage);
                }
            }
        });

        ValueAdjuster volumeAdjusterSFX =  (ValueAdjuster) findViewById("volumeAdjusterSFX");
        volumeAdjusterSFX.setValue(GamePanel.se.volumePercentage);
        volumeAdjusterSFX.setListener(new UIComponentListener() {
            @Override
            public void onPress() {

            }

            @Override
            public void onIncrease(){
                if (GamePanel.se.volumePercentage < volumeAdjusterSFX.getUpperThreshold()) {
                    GamePanel.se.volumePercentage += 10;
                    GamePanel.se.checkVolume(GamePanel.se.volumePercentage);

                    volumeAdjusterSFX.setValue(GamePanel.se.volumePercentage);
                }
            }

            @Override
            public void onDecrease(){
                if (GamePanel.se.volumePercentage > volumeAdjusterSFX.getLowerThreshold()) {
                    GamePanel.se.volumePercentage -= 10;
                    GamePanel.se.checkVolume(GamePanel.se.volumePercentage);

                    volumeAdjusterSFX.setValue(GamePanel.se.volumePercentage);
                }
            }
        });

        Button instruction =  (Button) findViewById("instructionButton");
        instruction.setListener(new UIComponentListener() {
            @Override
            public void onPress() {
                if(!(UI._UIManager.getCurrentScreen() instanceof MainMenu)) {
                    UI._UIManager.setCurrentScreen("instruction_menu");
                }
            }
        });

        Button goBack =  (Button) findViewById("goBackButton");
        goBack.setListener(new UIComponentListener() {
            @Override
            public void onPress() {
                if(!(UI._UIManager.getCurrentScreen() instanceof MainMenu)) {
                    UI._UIManager.setCurrentScreen("main_menu");
                }
            }
        });
    }

    @Override
    protected void onLeave(){
        UI._UIManager.setCurrentScreen("main_menu");
    }

    @Override
    public void render(Graphics2D g2){
        super.render(g2);
    }
}
