package environment;

import main.GamePanel;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Lighting {
    GameMap mp;
    BufferedImage darknessFilter;
    public boolean transit = false;
    public boolean fadeIn = false;
    public boolean fadeOut = false;
    private int transitCounter = 0;

    public Lighting(GameMap mp) {
        this.mp = mp;
    }

    public void setLightSource(int lightRadius) {
            darknessFilter = new BufferedImage(GamePanel.windowWidth, GamePanel.windowHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

            int centerX = mp.player.screenX + 32;
            int centerY = mp.player.screenY + 32;

            Color[] color = new Color[3];
            float[] fraction = new float[3];
            color[0] = new Color(0, 0, 0.08f, 0.1f);
            color[1] = new Color(0, 0, 0.08f, 0.5f);
            color[2] = new Color(0, 0, 0.08f, 1f);

            fraction[0] = 0f;
            fraction[1] = 0.9f;
            fraction[2] = 1f;

            RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, lightRadius, fraction, color);

            g2.setPaint(gPaint);

            g2.fillRect(0, 0, GamePanel.windowWidth, GamePanel.windowHeight);

            g2.dispose();
    }

    public void update(){
        if(fadeIn) blindFadein(); else
        if(fadeOut) blindFadeOut();
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(darknessFilter, 0, 0, null);
    }

    private void blindFadein(){
        transitCounter++;
        int radius;
        radius = mp.getBestLightingRadius() - 50 * transitCounter;
        setLightSource(radius);
        if(radius < mp.player.blindRadius){
            transit = false;
            fadeIn = false;
            setLightSource(mp.player.blindRadius);
            transitCounter = 0;
        }
    }

    private void blindFadeOut(){
        transitCounter++;
        int radius;
        if(transitCounter >= 0 && transitCounter <= 15){
            radius = mp.player.blindRadius - transitCounter;
        } else
        {
            if(transitCounter > 15 && transitCounter <= 40) radius = mp.player.blindRadius - 15; else
                radius = mp.player.blindRadius - 15 + (transitCounter - 40) * 100;
        }
        setLightSource(radius);

        if (radius > mp.getBestLightingRadius()) {
            transit = false;
            fadeOut = false;
            setLightSource(mp.getBestLightingRadius());
            transitCounter = 0;
        }
    }
}
