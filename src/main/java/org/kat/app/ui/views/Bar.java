package org.kat.app.ui.views;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bar extends ImageView {

    private int barX, barY, barWidth, barHeight;
    private int maxValue;
    private int currentValue;
    private Color fillColor = Color.RED;;

    public Bar(int x, int y, int width, int height,
               int barX, int barY, int barWidth, int barHeight,
               BufferedImage image) {
        super(x, y, width, height, image);
        this.barX = barX;
        this.barY = barY;
        this.barWidth = barWidth;
        this.barHeight = barHeight;
        this.maxValue = 100;
        this.currentValue = 100;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = Math.max(0, Math.min(currentValue, maxValue));
    }

    public void setFillColor(Color color) {
        this.fillColor = color;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(image, x, y, width, height, null);

        int fillWidth = (int) ((double) currentValue / maxValue * barWidth);

        g2.setColor(fillColor);
        g2.fillRect(barX, barY, fillWidth, barHeight);
    }
}
