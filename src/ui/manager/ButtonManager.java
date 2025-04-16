package ui.manager;

import java.awt.*;
import java.util.ArrayList;

import ui.components.Button;

import static main.GamePanel.windowWidth;

public class ButtonManager {
    private final ArrayList<Button> buttons = new ArrayList<>();
    private static final String cursor = ">";
    private int position;

    public ButtonManager(){
        this.position = 0;
    }

    public ButtonManager(int initialPosition){
        this.position = initialPosition;
    }

    public void addButton(Button button) {
        buttons.add(button);
    }

    public void removeButton(Button button) {
        buttons.remove(button);
    }

    public void clearButtons() {
        buttons.clear();
    }

    public void update(boolean keyPressed, boolean keyReleased){
        for(Button b: buttons){
            b.update(keyPressed, keyReleased);
        }
    }

    public void render(Graphics2D g2) {
        for (Button b : buttons) {
            b.draw(g2);
        }

        Button currentHoveredButton = buttons.get(position);
        int length = (int)g2.getFontMetrics().getStringBounds(currentHoveredButton.text , g2).getWidth();
        int x = windowWidth / 2 - length / 2;
        int y = (currentHoveredButton.y + currentHoveredButton.height) / 2;

        g2.drawString(cursor, x, y);
    }


    public ArrayList<Button> getButtons() {
        return buttons;
    }
}
