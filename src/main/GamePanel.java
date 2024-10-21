package main;

// awt library
import entity.Entity;
import entity.Player;
import graphics.Sprite;
import map.GameMap;
import map.MapManager;
import map.MapParser;
import util.Camera;

import java.awt.*;

// swing library
import javax.swing.JPanel;


public class GamePanel extends JPanel implements Runnable {
    final private int FPS = 60;

    final static public int originalTileSize = 16; // A character usually has 16x16 size
    final static public int scale = 3; // Use to scale the objects which appear on the screen
    final static public int tileSize = originalTileSize * scale;

    final static public int maxWindowCols = 16;
    final static public int maxWindowRows = 12;

    final static public int windowWidth = maxWindowCols * tileSize;
    final static public int windowHeight = maxWindowRows * tileSize;

    Camera camera = new Camera(windowWidth, windowHeight, tileSize);

    final static public KeyHandler keyHandler = new KeyHandler();


    public static GameMap currentMap;

    Thread gameThread;


    public GamePanel() {
        // Set the size of the window and background color
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        this.setBackground(Color.WHITE); // Ensure the background is white
        this.setDoubleBuffered(true); // Enable double buffering for smoother rendering
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        loadMap();
    }

    private void loadMap()
    {
        MapParser.loadMap( "map_test" ,"res/map/map_test_64.tmx");
        currentMap = MapManager.getGameMap("map_test");
    }

    public void loadSound()
    {

    }

    public void loadMusic()
    {

    }


    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public void run() {

        //loadCharacter();

        double drawInterval = (double) 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();   // Update the game state
            repaint();  // Repaint the panel

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        MapManager.dispose();
    }

    public void update() {
        currentMap.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);

        currentMap.render(g2 , camera);

        g2.dispose();
    }
}