package ui.manager;

import java.awt.*;
import ui.components.Button;

import static main.GamePanel.*;
import static main.UI.joystix;
import static main.UI.titleBackground;

public class TitleScreen {
    private final ButtonManager titleScreen = new ButtonManager();

    public TitleScreen(){
        Button startButton = new Button("Bắt đầu", 500, windowHeight*3/5 - tileSize, 100, 100);
        startButton.setTextColor(Color.WHITE);
        startButton.setFont(joystix);
        startButton.setTextAlign(Button.Align.CENTER);
        startButton.setTextSize(32);

        titleScreen.addButton(startButton);
    }

    public void update(boolean keyPressed, boolean keyReleased){
        titleScreen.update(keyPressed, keyReleased);
    }

    public void render(Graphics2D g2){
        g2.drawImage(titleBackground,0, 0, windowWidth, windowHeight, null);

        titleScreen.render(g2);
    }
}
