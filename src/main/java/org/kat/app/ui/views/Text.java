package org.kat.app.ui.views;
import org.kat.app.ui.Alignment;

import java.awt.*;

import static org.kat.app.main.UI.joystix;

public class Text{
    private static final Font DEFAULT_FONT = joystix;
    private static final Color DEFAULT_COLOR = Color.WHITE;

    protected int parentX, parentY;
    protected int parentWidth, parentHeight;
    protected String content;
    protected Color color;
    protected Font baseFont;
    protected float fontSize;

    protected Alignment hAlign = Alignment.HORIZONTAL_CENTER;
    protected Alignment vAlign = Alignment.VERTICAL_CENTER;

    public Text(){

    }

    public Text(String content){
        this.content = content;

        baseFont = DEFAULT_FONT;
        color = DEFAULT_COLOR;

    }

    public Text(String content, Color color, Font baseFont, float fontSize) {
        this.content = content;
        this.color = color;
        this.baseFont = baseFont;
        this.fontSize = fontSize;
    }

    // ===== Setter =====
    public void setText(String content) {
        this.content = content;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFont(Font baseFont) {
        this.baseFont = baseFont;
    }

    public void setFontSize(float size) {
        this.fontSize = size;
    }

    public void setHorizontalAlignment(Alignment hAlignment) {
        this.hAlign = hAlignment;
    }

    public void setVerticalAlignment(Alignment vAlignment) {
        this.vAlign = vAlignment;
    }

    public void setAlignment(Alignment hAlignment, Alignment vAlignment){
        this.hAlign = hAlignment;
        this.vAlign = vAlignment;
    }

    public void attach(View view){
        this.parentX = view.x + view.padding.pLeft;
        this.parentY = view.y + view.padding.pTop;
        this.parentWidth = view.width - view.padding.pLeft - view.padding.pRight;
        this.parentHeight = view.height - view.padding.pTop - view.padding.pBottom;
    }

    // ===== Getter =====
    public String getText() { return content; }
    public Color getColor() { return color; }
    public Font getFont() { return baseFont.deriveFont(fontSize); }
    public float getFontSize() { return fontSize; }

    // ===== Update and Render=====
    public void render(Graphics2D g2) {
        g2.setColor(color);
        g2.setFont(baseFont.deriveFont(fontSize));
        int textWidth = g2.getFontMetrics().stringWidth(content);
        int textHeight = g2.getFontMetrics().getAscent();

        int drawX = switch (hAlign) {
            case HORIZONTAL_LEFT-> parentX;
            case HORIZONTAL_CENTER -> parentX + (parentWidth - textWidth) / 2;
            case HORIZONTAL_RIGHT -> parentX + parentWidth - textWidth;
            default -> 0;
        };

        int drawY = switch (vAlign) {
            case VERTICAL_TOP -> parentY + textHeight;
            case VERTICAL_CENTER -> parentY + (parentHeight - textHeight) / 2 + textHeight;
            case VERTICAL_BOTTOM -> parentY + parentHeight - textHeight;
            default -> 0;
        };

        g2.drawString(content, drawX, drawY);
    }
}


