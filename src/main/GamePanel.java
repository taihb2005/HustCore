package main;

// awt library
import entity.Effect;
import environment.EnvironmentManager;
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

    final static public int windowWidth = maxWindowCols * 48;
    final static public int windowHeight = maxWindowRows * 48;

    public static Sound music = new Sound();
    public static Sound se = new Sound();
    public static EnvironmentManager environmentManager;
    final public KeyHandler keyHandler = new KeyHandler(this);
    public static Camera camera = new Camera();
    public static GameState gameState;
    public static GameState lastGameState;

    public GameMap currentMap;

    Thread gameThread;

    public static UI ui;

    public GamePanel() {
        // Set the size of the window and background color
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        this.setBackground(Color.WHITE); // Ensure the background is white
        this.setDoubleBuffered(true); // Enable double buffering for smoother rendering
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        loadMap();
        setup();
        currentMap.gp = this;
        ui = new UI(this);

    }

    private void loadMap()
    {
        MapParser.loadMap( "map_test" ,"res/map/map_test_64.tmx");
        currentMap = MapManager.getGameMap("map_test");
        camera.setCamera(windowWidth , windowHeight , currentMap.getMapWidth() ,currentMap.getMapHeight());
        environmentManager = new EnvironmentManager(currentMap);
        environmentManager.setup();
        environmentManager.lighting.setLightSource(2000);
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

        double drawInterval = (double) 1000000000 /FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        //long timer = 0;
        //int drawCount = 0;


        while(gameThread != null)
        {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            //timer += currentTime - lastTime;
            lastTime = currentTime;
            if(delta >= 1)
            {
                update();
                repaint();
//                drawToTempScreen(); //FOR FULL SCREEN - Draw everything to the buffered image
//                drawToScreen();     //FOR FULL SCREEN - Draw the buffered image to the screen
                delta--;
                //drawCount++;
            }

        }

        dispose();
    }

    public void update() {
        if(gameState == GameState.PLAY_STATE ) {
            resumeMusic(0);
            currentMap.update();
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
