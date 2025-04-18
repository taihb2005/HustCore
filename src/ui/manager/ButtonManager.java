package ui.manager;

import java.awt.*;
import java.util.ArrayList;

import ui.components.Button;

import static main.GamePanel.windowWidth;

public class ButtonManager {
    private final ArrayList<Button> buttons = new ArrayList<>();
    private static final String cursor = ">";
    public int position;

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

        g2.setFont(currentHoveredButton.font.deriveFont(Font.PLAIN, 32));
        FontMetrics fm = g2.getFontMetrics();

        int textWidth = fm.stringWidth(currentHoveredButton.text);
        int cursorWidth = fm.stringWidth(cursor);

        int drawX = currentHoveredButton.x + currentHoveredButton.margin.left;
        int buttonTextX = switch (currentHoveredButton.textAlign) {
            case LEFT -> drawX + currentHoveredButton.padding.left;
            case RIGHT -> drawX + currentHoveredButton.width - currentHoveredButton.padding.right - textWidth;
            default -> drawX + (currentHoveredButton.width - textWidth) / 2;
        };

        int drawY = currentHoveredButton.y + currentHoveredButton.margin.top;
        int textY = drawY + currentHoveredButton.padding.top + fm.getAscent();

        g2.setColor(currentHoveredButton.textColor);
        g2.drawString(cursor, buttonTextX - cursorWidth - 10, textY);
    }



    public ArrayList<Button> getButtons() {
        return buttons;
    }
}
