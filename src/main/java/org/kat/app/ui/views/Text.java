package org.kat.app.ui.views;
import org.kat.app.ui.Alignment;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.kat.app.main.UI.joystix;

public class Text{
    protected static final int DEFAULT_FONT_SIZE = 18;
    protected static final int DEFAULT_LINE_SPACING = 20;
    protected static final float DEFAULT_FACTOR = 1.23f;
    private static final Font DEFAULT_FONT = joystix;
    public static final Color DEFAULT_COLOR = Color.WHITE;
    public static final float DEFAULT_ALPHA = 1f;

    protected int parentX, parentY;
    protected int parentWidth, parentHeight;
    protected StringBuilder content;
    protected Color color = DEFAULT_COLOR;
    protected Font baseFont = DEFAULT_FONT;
    protected float alpha = DEFAULT_ALPHA;
    protected float fontSize = DEFAULT_FONT_SIZE;

    protected int lineNums;
    protected float factor = DEFAULT_FACTOR;
    protected int lineSpacing = DEFAULT_LINE_SPACING;

    protected Alignment hAlign = Alignment.HORIZONTAL_CENTER;
    protected Alignment vAlign = Alignment.VERTICAL_CENTER;

    public Text(){
        build();
    }

    public Text(String content){
        this.content = new StringBuilder(content);

        build();
    }

    public Text(String content, Color color, Font baseFont, float fontSize) {
        this.content = new StringBuilder(content);
        this.color = color;
        this.baseFont = baseFont;
        this.fontSize = fontSize;

        build();
    }

    // ===== Setter =====
    public void setText(String content) {
        this.content.delete(0, this.content.length());
        this.content.append(content);

    }

    public void setProperties(Text text){
        this.baseFont = text.baseFont;
        this.color = text.color;
        this.fontSize = text.fontSize;

        this.lineNums = text.lineNums;
        setAlignment(text.hAlign, text.vAlign);
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

    public void setAlpha(float alpha) {this.alpha = alpha;}

    public void setLineSpacing(float factor) {
        this.factor = factor;
    }

    public void append(char c){
        this.content.append(c);
    }
    public void append(Text text){
        content.append(text.getText());
    }
    public void append(String textContent){
        this.content.append(textContent);
    }
    public void delete(int start, int end){
        content.delete(start, end);
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
    public void setLineAt(int line){
        this.lineNums = line;
    }
    public Alignment getHorizontalAlignment(){
        return hAlign;
    }
    public Alignment getVerticalAlignment(){
        return vAlign;
    }

    public void attach(View view){
        this.parentX = view.x + view.padding.pLeft;
        this.parentY = view.y + view.padding.pTop;
        this.parentWidth = view.width - view.padding.pLeft - view.padding.pRight;
        this.parentHeight = view.height - view.padding.pTop - view.padding.pBottom;
    }

    private void build(){
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        g2.setFont(baseFont.deriveFont(fontSize));
        lineSpacing = (int) (g2.getFontMetrics().getHeight() * factor);

        g2.dispose();
        image.flush();
    }

    public void clear(){
        content.delete(0, this.content.length());
    }

    public boolean isEmpty(){
        return content.isEmpty();
    }

    public boolean equals(Text others){
        return content.toString()
                .equals(others.getText());
    }

    // ===== Getter =====
    public int length(){
        return content.length();
    }
    public char getTextAt(int index){
        return content.charAt(index);
    }
    public String subtext(int start, int end){
        return content.substring(start, end);
    }
    public String getText() { return content.toString(); }
    public Color getColor() { return color; }
    public Font getFont() { return baseFont.deriveFont(fontSize); }
    public float getFontSize() { return fontSize; }

    public int getParentX() { return parentX; }
    public int getParentY() { return parentY; }
    public int getParentWidth() { return parentWidth; }
    public int getParentHeight() { return parentHeight; }

    // ===== Update and Render=====
    public void render(Graphics2D g2) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(color);
        g2.setFont(baseFont.deriveFont(fontSize));
        int textWidth = g2.getFontMetrics().stringWidth(content.toString());
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

        drawY += lineNums * lineSpacing;

        g2.drawString(content.toString(), drawX, drawY);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}


