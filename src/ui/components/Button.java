package ui.components;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import static main.GamePanel.tileSize;

public class Button {
    public String id;
    public String text;
    private int textSize;
    public int x, y, width, height;

    private Insets padding = new Insets(10, 10, 10, 10); // top, left, bottom, right
    private Insets margin = new Insets(5, 5, 5, 5);

    private Runnable onHover;
    private Runnable onSelect;
    private Runnable onRelease;

    private boolean hovered = false;
    private boolean selected = false;

    private Consumer<Button> onClick;

    private Font font = new Font("Arial", Font.BOLD, 20);
    private Color bgColor = Color.DARK_GRAY;
    private Color hoverColor = Color.GRAY;
    private Color textColor = Color.WHITE;

    public enum Align { LEFT, CENTER, RIGHT }
    private Align textAlign = Align.CENTER;

    public Button(String text, int x, int y, int width, int height, Consumer<Button> onClick) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.onClick = onClick;
    }

    public Button(String text, int x, int y, int width, int height) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Button(String text, int x, int y, int width, int height, Align textAlign) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.textAlign = textAlign;
    }

    public void update(boolean keyboardPressed, boolean keyboardReleased) {
        if (hovered && keyboardPressed) {
            selected = true;
        }

        if (selected && hovered && keyboardPressed) {
            if (onClick != null) onSelect.run();
        }

        if (hovered && onHover != null) {
            onHover.run();
        }

        if (selected && keyboardReleased) {
            selected = false;
            if (onRelease != null) onRelease.run();
        }
    }

    public void draw(Graphics2D g2) {
        g2.setFont(font.deriveFont(Font.PLAIN, textSize));
        g2.setColor(textColor);

        int drawX = x + margin.left;
        int drawY = y + margin.top;

        int length    = (int)g2.getFontMetrics().getStringBounds(text , g2).getWidth();
        g2.drawRoundRect(x - tileSize / 2, y - tileSize, length + 40 , tileSize + 10, 30, 30);

        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = (int)fm.getStringBounds(text, g2).getWidth();
        int textHeight = 26;

        int textX = switch (textAlign) {
            case LEFT -> drawX + padding.left;
            case RIGHT -> drawX + width - padding.right - textWidth;
            default -> drawX + (width - textWidth) / 2;
        };
        int textY = drawY + (height + textHeight) / 2 - padding.bottom;

        System.out.println(textX);

        g2.setColor(textColor);
        g2.drawString(text, textX, textY);

        g2.setColor(textColor);
        g2.drawString(text , textX + 5 , textY + 5 );
    }

    public void setOnSelectListener(Runnable onSelect){

    }

    public void setOnHoverListener(Runnable onHover){

    }

    public void setOnReleaseListener(Runnable onRelease){
        hovered = false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x + margin.left, y + margin.top, width, height);
    }

    public void setPadding(int top, int left, int bottom, int right) {
        this.padding = new Insets(top, left, bottom, right);
    }

    public void setMargin(int top, int left, int bottom, int right) {
        this.margin = new Insets(top, left, bottom, right);
    }

    public void setTextAlign(Align align) {
        this.textAlign = align;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setTextSize(int textSize){
        this.textSize = textSize;
    }


    public void setBackgroundColor(Color c) {
        this.bgColor = c;
    }

    public void setHoverColor(Color c) {
        this.hoverColor = c;
    }

    public void setTextColor(Color c) {
        this.textColor = c;
    }
}
