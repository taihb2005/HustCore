package main;

// awt library
import ai.PathFinder;
import environment.EnvironmentManager;
import level.AssetSetter;
import level.Level;
import level.LevelManager;
import level.progress.Level01;
import level.progress.Level02;
import map.*;
import status.StatusManager;
import util.Camera;

import java.awt.*;

// swing library
import javax.swing.JPanel;
import javax.swing.text.AbstractDocument;

public class GamePanel extends JPanel implements Runnable {
    final private int FPS = 60;
    public int currentFPS;

    final static public int originalTileSize = 16; // A character usually has 16x16 size
    final static public int scale = 3; // Use to scale the objects which appear on the screen
    final static public int tileSize = originalTileSize * scale;

    final static public int maxWindowCols = 16;
    final static public int maxWindowRows = 12;

    final static public int windowWidth = maxWindowCols * 48;
    final static public int windowHeight = maxWindowRows * 48;

    public static Sound music = new Sound();
    public static Sound se = new Sound();
    public static PathFinder pFinder;
    public static EnvironmentManager environmentManager;
    final public KeyHandler keyHandler = new KeyHandler(this);
    public static Camera camera = new Camera();
    public static GameState gameState;

    public static StatusManager sManager = new StatusManager();
    public LevelManager lvlManager = new LevelManager(this);
    public static int previousLevelProgress = 2;
    public static int levelProgress = 2;
    public static Level currentLevel;
    public static GameMap currentMap;

    Thread gameThread;

    public static UI ui;

    public GamePanel() {
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        loadMap();
        currentMap.player.storeValue();
    }

    public void loadMap()
    {
        switch(levelProgress){
            case 1 : currentLevel = new Level01(this); break;
            case 2 : currentLevel = new Level02(this); break;
        }
        currentMap = currentLevel.map;
        ui = new UI(this);
    }

    public void restart(){
        currentLevel.map.player.resetValue();
        loadMap();
    }

    public void setup()
    {
        playMusic(0);
        se.setFile(1);
    }

    public void startGameThread() {
        gameState = GameState.MENU_STATE;
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
            timer += currentTime - lastTime; // Đếm thời gian đã trôi qua
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();

                drawCount++; // Đếm số khung hình được vẽ
                delta--;
            }

            if (timer >= 1000000000) { // Đã đủ 1 giây
                currentFPS = drawCount;
                drawCount = 0;  // Đặt lại số khung hình
                timer = 0;      // Đặt lại thời gian
            }
        }

        dispose();
    }


    public void update() {
        if(gameState == GameState.PLAY_STATE ) {
            resumeMusic(0);
            currentMap.update();
            currentLevel.updateProgress();
            if(currentLevel.canChangeMap) lvlManager.update();
            if(environmentManager.lighting.transit) environmentManager.lighting.update();
        } else
        if(gameState == GameState.PAUSE_STATE )
        {
            pauseMusic(0);;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if(gameState == GameState.PLAY_STATE || gameState == GameState.DIALOGUE_STATE || gameState == GameState.PAUSE_STATE) {
            currentMap.render(g2);
            environmentManager.draw(g2);
        }
        ui.render(g2);

        g2.dispose();
    }

    public void playMusic(int index)
    {
        music.setFile(index);
        music.play();
        music.loop();
    }
    public void pauseMusic(int index){
        music.pause();
    }
    public void resumeMusic(int index){
        music.resume();
    }
    public void stopMusic(int index)
    {
        music.stop();
    }
    public void playSE(int index)
    {
        se.setFile(index);
        se.play();
    }


    private void dispose()
    {
        MapManager.dispose();

    }
}
