package main;

// awt library
import entity.Player;
import graphics.Sprite;
import tile.MapManager;
import tile.MapParser;

import java.awt.*;

// swing library
import javax.swing.JPanel;

import tile.MapManager;

public class GamePanel extends JPanel implements Runnable {
    final private int FPS = 60;

    final static public int originalTileSize = 16; // A character usually has 16x16 size
    final static public int scale = 3; // Use to scale the objects which appear on the screen
    final static public int tileSize = originalTileSize * scale;

    final static public int maxWindowCols = 16;
    final static public int maxWindowRows = 12;

    final static public int windowWidth = maxWindowCols * tileSize;
    final static public int windowHeight = maxWindowRows * tileSize;

    //final static public MapParser mapParser = new MapParser();

    final static public KeyHandler keyHandler = new KeyHandler();

    static public Player player1 ;


    Thread gameThread;


    public GamePanel() {
        // Set the size of the window and background color
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        this.setBackground(Color.WHITE); // Ensure the background is white
        this.setDoubleBuffered(true); // Enable double buffering for smoother rendering
        this.addKeyListener(keyHandler);
        this.setFocusable(true);


        player1 = new Player(new Sprite("/entity/player/player.png") , 100 , 100 , 5);
        MapParser.loadMap( "map_test" ,"res/tile/map_test.tmx");
    }

    public void loadCharacter()
    {
        //player1 = new Player(new Sprite("/entity/player/player.png") , 100 , 100);
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
        player1.update();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Set custom drawing color and draw shapes
        g2.setColor(Color.BLACK);
        MapManager.getGameMap("map_test").render(g2);

        player1.render(g2);


        g2.dispose();
    }
}
