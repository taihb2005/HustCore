package org.kat.app.ui.views;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.kat.app.main.GamePanel.windowHeight;
import static org.kat.app.main.GamePanel.windowWidth;

public class ImageView extends View{
    private final static BufferedImage filter = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB);

    static{
        Graphics2D g2 = filter.createGraphics();
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRect(0, 0, windowWidth, windowHeight);
        g2.dispose();
    }
    protected final BufferedImage image;

    public ImageView(int x, int y, int width, int height, BufferedImage image){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        if(image != null) {
            this.image = image;
        } else {
            this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2 = this.image.createGraphics();
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRect(0, 0, width, height);
            g2.dispose();
        }
    }

    @Override
    public void render(Graphics2D g2){
        g2.drawImage(image, x, y, null);
    }
}
