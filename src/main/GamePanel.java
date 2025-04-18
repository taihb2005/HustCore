package main;

// awt library
import ai.PathFinder2;
import graphics.environment.EnvironmentManager;
import level.Level;
import map.*;
import status.StatusManager;
import util.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

// swing library
import javax.swing.JPanel;

import static main.KeyHandler.disableKey;

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
    public static Credit credit;
    public static EnvironmentManager environmentManager;
    public final KeyHandler keyHandler = new KeyHandler(this);
    public static Camera camera = new Camera();
    public static GameState gameState;

    public static StatusManager sManager = new StatusManager();
    //public LevelManager lvlManager = new LevelManager(this);
    public static int previousLevelProgress = 4;
    public static int levelProgress = 4;
    public static Level currentLevel;
    public static GameMap currentMap;
    public static boolean gameCompleted;

    public static BufferedImage darknessFilter;
    private float darknessOpacity = 0.0f;
    public boolean darker = false;
    public boolean lighter = false;

    Thread gameThread;

    public static UI ui;

    public GamePanel() {
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        stopMusic();
        setup();
        credit = new Credit(this);
        ui = new UI(this);
    }


    public void setup()
    {
        playMusic(0);
        se.setFile(1);
    }

    public void startGameThread() {
        //new LoadResourceThread().start();
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
        updateDarkness();
        if (gameState == GameState.PLAY_STATE || gameState == GameState.PASSWORD_STATE || gameState == GameState.WIN_STATE) {
            if (!music.clip.isRunning() && !gameCompleted) {
                resumeMusic();
            }
            currentMap.update();
            currentLevel.updateProgress();
            ui.update();
        } else if (gameState == GameState.PAUSE_STATE) {
            pauseMusic();
            ;
        }
        if (environmentManager != null) environmentManager.lighting.update();

    }

    @Override
    protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            if (gameState == GameState.PLAY_STATE || gameState == GameState.DIALOGUE_STATE || gameState == GameState.PAUSE_STATE || gameState == GameState.PASSWORD_STATE) {
                currentMap.render(g2);
                environmentManager.draw(g2);
            }
            if (currentLevel != null) currentLevel.render(g2);
            ui.render(g2);
            drawDarkness(g2);
            if (gameCompleted) {
                disableKey();
                credit.render(g2);
            }
            g2.dispose();
    }

    public void drawDarkness(Graphics2D g2) {
        BufferedImage darknessFilter = new BufferedImage(GamePanel.windowWidth, GamePanel.windowHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) darknessFilter.getGraphics();

        g.setColor(new Color(0, 0, 0, darknessOpacity)); // Black with transparency
        g.fillRect(0, 0, GamePanel.windowWidth, GamePanel.windowHeight);

        g2.drawImage(darknessFilter, 0, 0, null);
    }

    private void updateDarkness(){
        if(darker) increaseDarkness(); else
            if(lighter) decraseDarkness();
    }

    public void increaseDarkness() {
        darknessOpacity += 0.025f;
        if (darknessOpacity > 1.0f) {
            darker = false;
            darknessOpacity = 1.0f;
        }
    }

    public void decraseDarkness(){
        darknessOpacity -= 0.025f;
        if (darknessOpacity < 0.0f) {
            lighter = false;
            darknessOpacity = 0.0f;
        }
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
