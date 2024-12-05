package graphics.environment;

import main.GamePanel;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.Timer;

import static main.GamePanel.*;

public class Lighting {
    GameMap mp;
    BufferedImage darknessFilter;
    public boolean transit = false;
    public boolean fadeIn = false;
    public boolean fadeOut = false;
    public boolean spotLightIn = false;
    public boolean spotLightOut = false;
    private final Color [] color;
    private final float [] fraction;
    private int radius;
    private int transitCounter = 0;
    private int spotLightCounter = 0;
    private int shrinkingRate = 40;
    private int smallestRadius = 20;
    private Timer timer;
    int screenX , screenY;

    public Lighting(GameMap mp) {
        this.mp = mp;
        color = new Color[3];
        fraction = new float[3];

        color[0] = new Color(0, 0, 0.08f, 0.1f);
        color[1] = new Color(0, 0, 0.08f, 0.5f);
        color[2] = new Color(0, 0, 0.08f, 1f);

        fraction[0] = 0.8f;
        fraction[1] = 0.9f;
        fraction[2] = 1f;

        screenX = windowWidth / 2 + 32;
        screenY = windowHeight / 2 + 32;
        radius = mp.getBestLightingRadius();
    }

    public void setLightRadius(int r){this.radius = r;}

    public void setLightSource() {
            darknessFilter = new BufferedImage(GamePanel.windowWidth, GamePanel.windowHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();
            g2.setColor(Color.BLACK);

            if(radius > 0) {
                RadialGradientPaint gPaint = new RadialGradientPaint(screenX, screenY, radius, fraction, color);
                g2.setPaint(gPaint);
            }

            g2.fillRect(0, 0, GamePanel.windowWidth, GamePanel.windowHeight);

            g2.dispose();
    }

    public void update(){
        if(transit) {
            if (fadeIn) blindFadein();
            else if (fadeOut) blindFadeOut();
        }else if(spotLightIn) startSpotLight();
         else if(spotLightOut) endSpotLight();
        screenX = mp.player.worldX - camera.getX() + 32;
        screenY = mp.player.worldY - camera.getY() + 32;
    }

    public void draw(Graphics2D g2) {
        setLightSource();
        g2.drawImage(darknessFilter, 0, 0, null);
    }

    private void blindFadein(){
        transitCounter++;
        int radius;
        radius = mp.getBestLightingRadius() - 50 * transitCounter;
        setLightRadius(radius);
        if(radius < mp.player.blindRadius){
            transit = false;
            fadeIn = false;
            setLightRadius(mp.player.blindRadius);
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
        setLightRadius(radius);

        if (radius > mp.getBestLightingRadius()) {
            transit = false;
            fadeOut = false;
            setLightRadius(mp.getBestLightingRadius());
            transitCounter = 0;
        }
    }

    public void startSpotLight(){
        if (radius > 0) {
            radius -= shrinkingRate;
            setLightRadius(radius);
        } else {
            radius = 0;
            spotLightIn = false;
        }
    }

    public void endSpotLight(){
        if(radius < mp.getBestLightingRadius()){
            radius += shrinkingRate;
            setLightRadius(radius);
        } else {
            radius = mp.getBestLightingRadius();
            spotLightIn = false;
        }
    }
}
