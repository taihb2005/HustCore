package main;

// awt library
import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

// swing library
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import graphics.Sprite;

public class GamePanel extends JPanel implements Runnable {
    final private int FPS = 60;

    final private int originalTileSize = 16; // A character usually has 16x16 size
    private int scale = 3; // Use to scale the objects which appear on the screen
    final private int tileSize = originalTileSize * scale;

    final private int maxWindowCols = 16;
    final private int maxWindowRows = 12;

    final private int windowWidth = maxWindowCols * tileSize;
    final private int windowHeight = maxWindowRows * tileSize;

    private KeyHandler keyHandler = new KeyHandler();

    public Sprite sprite;
    BufferedImage[][] test ;
    public BufferedImage hehe;
    public BufferedImage y;

    Thread gameThread;

    //playerPos
    private int posX = 100;
    private int posY = 100;
    public GamePanel() {
        // Set the size of the window and background color
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        this.setBackground(Color.BLACK); // Ensure the background is black
        this.setDoubleBuffered(true); // Enable double buffering for smoother rendering
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {


        BufferedImage[] o;
        try {
            sprite = new Sprite("/entity/player/player.png");

            o = new BufferedImage[sprite.getSpriteCols()];
            o = sprite.getSpriteArrayRow(0);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        hehe = o[0];


        //test = sprite.getSpriteArray();

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
    }

    public void update() {
        if(keyHandler.upPressed)
        {
            posY -= 3;
        }

        if(keyHandler.downPressed)
        {
            posY += 3;
        }

        if(keyHandler.leftPressed)
        {
            posX -= 3;
        }

        if(keyHandler.rightPressed)
        {
            posX += 3;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Set custom drawing color and draw shapes
        g2.setColor(Color.WHITE);
        g2.fillRect(posX, posY, tileSize, tileSize);

        g2.drawImage(hehe , 0  , 0 , 48 , 48 , null);

        g2.dispose();
    }
}
