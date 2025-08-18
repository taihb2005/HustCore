package org.kat.app.main;

import org.kat.app.level.Level;
import org.kat.app.map.GameMap;
import org.kat.app.map.MapManager;
import org.kat.app.status.StatusManager;
import org.kat.app.util.Camera;

import javax.swing.*;
import java.awt.*;

import static org.kat.app.main.KeyHandler.disableKey;

public class GamePanel extends JPanel implements Runnable {
    public static final int FPS = 60;
    public int currentFPS;

    public static final int TILE_SIZE = 48;
    public static final int maxWindowCols = 16;
    public static final int maxWindowRows = 12;
    public static final int windowWidth = maxWindowCols * 48;
    public static final int windowHeight = maxWindowRows * 48;

    public static final Sound music = new Sound();
    public static final Sound se = new Sound();
    public static final KeyHandler keyHandler = new KeyHandler();
    public static final Camera camera = new Camera();
    public static final StatusManager sManager = new StatusManager();
    public static final UI ui = new UI();

    public static int previousLevelProgress = 0;
    public static int levelProgress = 0;
    public static GameState gameState;
    public static Level currentLevel;
    public static GameMap currentMap;
    public static boolean gameCompleted;

    Thread gameThread;

    public GamePanel() {
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        stopMusic();
        setup();
    }


    public void setup()
    {
        playMusic(0);
        se.setFile(1);
    }

    public void startGameThread() {
        gameState = GameState.MENU;
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();

                drawCount++;
                delta--;
            }

            if (timer >= 1000000000) {
                currentFPS = drawCount;
                drawCount = 0;
                timer = 0;
            }
        }

        dispose();
    }


    public void update() {
        ui.update();

        switch (gameState) {
            case PLAY -> {
                if (!music.clip.isRunning() && !gameCompleted) {
                    resumeMusic();
                }
                currentMap.update();
                currentLevel.update();
            }

            case PAUSE -> pauseMusic();
            case CREDIT -> disableKey();
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            if (gameState == GameState.PLAY ||
                    gameState == GameState.PAUSE ||
                    gameState == GameState.CREDIT
            ) {
                if(currentMap != null) currentMap.render(g2);
            }
            ui.render(g2);
            g2.dispose();
    }


    public static void playMusic(int index)
    {
        music.setFile(index);
        music.play();
        music.loop();
    }

    public static void pauseMusic(){
        music.pause();
    }
    public static void resumeMusic(){
        music.resume();
    }
    public static void stopMusic()
    {
        music.stop();
    }
    public static void playSE(int index)
    {
        se.setFile(index);
        se.play();
    }

    private void dispose()
    {
        MapManager.dispose();
    }
}
