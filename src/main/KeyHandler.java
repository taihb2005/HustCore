package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

    GamePanel gp;
    public static boolean upPressed;
    public static boolean downPressed;
    public static boolean rightPressed;
    public static boolean leftPressed;
    public static boolean enterPressed;
    public static boolean showDebugMenu = false;
    public static boolean showHitbox = false;

    public KeyHandler(GamePanel gp)
    {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e){

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (GamePanel.gameState == GameState.MENU_STATE) {
            if (keyCode == KeyEvent.VK_SPACE) {
                GamePanel.gameState = GameState.PLAY_STATE;
                System.out.println("Chuyển sang PLAY_STATE");
            }
        }

        if(GamePanel.gameState == GameState.PLAY_STATE) {
            if (keyCode == KeyEvent.VK_S) {
                downPressed = true;
            }

            if (keyCode == KeyEvent.VK_W) {
                upPressed = true;
            }

            if (keyCode == KeyEvent.VK_D) {
                rightPressed = true;
            }

            if (keyCode == KeyEvent.VK_A) {
                leftPressed = true;
            }

            if (keyCode == KeyEvent.VK_ESCAPE) {
                GamePanel.gameState = GameState.PAUSE_STATE;
            }
            if (keyCode == KeyEvent.VK_F3) {
                showDebugMenu = !showDebugMenu;
            }

            if (keyCode == KeyEvent.VK_F4) {
                showHitbox = !showHitbox;
            }
            if(keyCode == KeyEvent.VK_ENTER)
            {
                enterPressed = true;
            }
        } else
        if(GamePanel.gameState == GameState.PAUSE_STATE)
        {
            if(keyCode == KeyEvent.VK_ESCAPE)
                GamePanel.gameState = GameState.PLAY_STATE;

            // FOR OPTIONS
            int maxCommandNum = 0;
            switch(GamePanel.ui.subState) {
                case 0: maxCommandNum = 3;
            }

            if(keyCode == KeyEvent.VK_W)
            {
                GamePanel.ui.commandNum--;
                if (GamePanel.ui.commandNum < 0) {
                    GamePanel.ui.commandNum = maxCommandNum;
                }
            }
            if(keyCode == KeyEvent.VK_S)
            {
                GamePanel.ui.commandNum++;
                if (GamePanel.ui.commandNum > maxCommandNum) {
                    GamePanel.ui.commandNum = 0;
                }
            }
            if(keyCode == KeyEvent.VK_A)
            {
                if (GamePanel.ui.subState == 0) {
                    if (GamePanel.ui.commandNum == 0 && GamePanel.sound.volumePercentage > 0) {
                        GamePanel.sound.volumePercentage-=10;
                        GamePanel.sound.checkVolume(GamePanel.sound.volumePercentage);
                    }
                }
            }
            if(keyCode == KeyEvent.VK_D)
            {
                if (GamePanel.ui.subState == 0) {
                    if (GamePanel.ui.commandNum == 0 && GamePanel.sound.volumePercentage < 100) {
                        GamePanel.sound.volumePercentage+=10;
                        GamePanel.sound.checkVolume(GamePanel.sound.volumePercentage);
                    }
                }
            }
            if(keyCode == KeyEvent.VK_ENTER)
            {
                if (GamePanel.ui.subState == 0) {
                    if (GamePanel.ui.commandNum == 2) {
                        GamePanel.gameState = GameState.MENU_STATE;
                    }
                    if (GamePanel.ui.commandNum == 3) {
                        System.exit(0);
                    }
                }
            }
        }
        else if(GamePanel.gameState == GameState.DIALOGUE_STATE)
        {
            if(keyCode == KeyEvent.VK_ENTER)
            {
                enterPressed = true;
            }
        } else
        if(GamePanel.gameState == GameState.LOSE_STATE)
        {
            if(keyCode == KeyEvent.VK_SPACE)
            {
                GamePanel.gameState = GameState.MENU_STATE;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode)
        {
            case KeyEvent.VK_S: downPressed = false; break;
            case KeyEvent.VK_A: leftPressed = false; break;
            case KeyEvent.VK_D: rightPressed = false; break;
            case KeyEvent.VK_W: upPressed = false; break;
            case KeyEvent.VK_ENTER: enterPressed = false; break;
        }
    }
}