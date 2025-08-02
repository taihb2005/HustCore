package org.kat.app.ui.hustcore;

import org.kat.app.main.GamePanel;
import org.kat.app.main.GameState;
import org.kat.app.main.UI;
import org.kat.app.thread.LoadingService;
import org.kat.app.ui.UIComponentListener;
import org.kat.app.ui.components.Button;
import org.kat.app.ui.components.ValueAdjuster;
import org.kat.app.ui.views.UIScreen;
import org.kat.app.ui.views.View;
import org.kat.app.util.Tree;

import static org.kat.app.main.GamePanel.gameState;

public class SettingsPause extends UIScreen {
    public SettingsPause(String id, Tree<View> viewTree) {
        super(id, viewTree);
    }

    @Override
    protected void onCreate() {
        ValueAdjuster volumePauseAdjusterMusic =  (ValueAdjuster) findViewById("volumePauseAdjusterMusic");
        volumePauseAdjusterMusic.setValue(GamePanel.music.volumePercentage);
        volumePauseAdjusterMusic.setListener(new UIComponentListener() {
            @Override
            public void onPress() {

            }

            @Override
            public void onIncrease(){
                if (GamePanel.music.volumePercentage < volumePauseAdjusterMusic.getUpperThreshold()) {
                    GamePanel.music.volumePercentage += 10;
                    GamePanel.music.checkVolume(GamePanel.music.volumePercentage);

                    volumePauseAdjusterMusic.setValue(GamePanel.music.volumePercentage);
                }
            }

            @Override
            public void onDecrease(){
                if (GamePanel.music.volumePercentage > volumePauseAdjusterMusic.getLowerThreshold()) {
                    GamePanel.music.volumePercentage -= 10;
                    GamePanel.music.checkVolume(GamePanel.music.volumePercentage);

                    volumePauseAdjusterMusic.setValue(GamePanel.music.volumePercentage);
                }
            }
        });

        ValueAdjuster volumePauseAdjusterSFX =  (ValueAdjuster) findViewById("volumePauseAdjusterSFX");
        volumePauseAdjusterSFX.setValue(GamePanel.se.volumePercentage);
        volumePauseAdjusterSFX.setListener(new UIComponentListener() {
            @Override
            public void onPress() {

            }

            @Override
            public void onIncrease(){
                if (GamePanel.se.volumePercentage < volumePauseAdjusterSFX.getUpperThreshold()) {
                    GamePanel.se.volumePercentage += 10;
                    GamePanel.se.checkVolume(GamePanel.se.volumePercentage);

                    ((ValueAdjuster) volumePauseAdjusterSFX).setValue(GamePanel.se.volumePercentage);
                }
            }

            @Override
            public void onDecrease(){
                if (GamePanel.se.volumePercentage > volumePauseAdjusterSFX.getLowerThreshold()) {
                    GamePanel.se.volumePercentage -= 10;
                    GamePanel.se.checkVolume(GamePanel.se.volumePercentage);

                    ((ValueAdjuster) volumePauseAdjusterSFX).setValue(GamePanel.se.volumePercentage);
                }
            }
        });

        Button instruction =  (Button) findViewById("tryAgainButton");
        instruction.setListener(new UIComponentListener() {
            @Override
            public void onPress() {
                LoadingService.restart();
            }
        });

        Button resumeBack =  (Button) findViewById("resumeButton");
        resumeBack.setListener(new UIComponentListener() {
            @Override
            public void onPress() {
                UI._UIManager.clearFromScreenStack();
                gameState = GameState.PLAY;
            }
        });
    }

    @Override
    public void onLeave(){
        UI._UIManager.clearFromScreenStack();
        gameState = GameState.PLAY;
    }
}
