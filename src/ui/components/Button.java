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

    public Insets padding = new Insets(10, 10, 10, 10); // top, left, bottom, right
    public Insets margin = new Insets(5, 5, 5, 5);

    private Runnable onHover;
    private Runnable onSelect;
    private Runnable onRelease;

    private boolean hovered = false;
    private boolean selected = false;

    private Consumer<Button> onClick;

    public Font font = new Font("Arial", Font.BOLD, 20);
    private Color bgColor = Color.DARK_GRAY;
    private Color hoverColor = Color.GRAY;
    public Color textColor = Color.WHITE;

    public enum Align { LEFT, CENTER, RIGHT }
    public Align textAlign = Align.CENTER;

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
        g2.setFont(font.deriveFont(Font.PLAIN, 32));
        g2.setColor(textColor);

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();

        int contentWidth = textWidth + padding.left + padding.right;
        int contentHeight = textHeight + padding.top + padding.bottom;

        int drawX = x + margin.left;
        int drawY = y + margin.top;

        // Vẽ khung nền
        g2.setColor(Color.WHITE); // Ví dụ: màu nền có độ mờ
        g2.drawRoundRect(drawX, drawY, contentWidth, contentHeight, 20, 20);

        // Tính toạ độ text theo căn lề
        int textX = switch (textAlign) {
            case LEFT -> drawX + padding.left;
            case RIGHT -> drawX + contentWidth - padding.right - textWidth;
            default -> drawX + (contentWidth - textWidth) / 2;
        };
        int textY = drawY + padding.top + textHeight; // baseline text

        // Vẽ text
        g2.setColor(textColor);
        g2.drawString(text, textX, textY);
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
