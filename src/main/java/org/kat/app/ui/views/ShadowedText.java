package org.kat.app.ui.views;

import java.awt.*;

final public class ShadowedText extends Text{
    private static final int OFFSET_X = 6;
    private static final int OFFSET_Y = 4;
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 150);

    public ShadowedText(String content) {
        super(content);
    }

    public ShadowedText(String content, int x, int y, Color color, Font baseFont, float fontSize) {
        super(content, color, baseFont, fontSize);
    }

    @Override
    public void render(Graphics2D g2) {
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

        g2.setColor(SHADOW_COLOR);
        g2.drawString(content, drawX + OFFSET_X, drawY + OFFSET_Y);

        g2.setColor(color);
        g2.drawString(content, drawX, drawY);
    }
}
