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
            if(keyCode == KeyEvent.VK_ESCAPE) GamePanel.gameState = GameState.PLAY_STATE;
        } else
        if(GamePanel.gameState == GameState.DIALOGUE_STATE)
        {
            if(keyCode == KeyEvent.VK_ENTER)
            {
                enterPressed = true;
            }
        } else
        if(GamePanel.gameState == GameState.LOSE_STATE)
        {
            if(keyCode == KeyEvent.VK_ENTER)
            {
                //do nothing
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

    public static void disableKey(){
        upPressed = false;
        downPressed = false;
        rightPressed = false;
        leftPressed = false;
        enterPressed = false;
        showHitbox = false;
        showDebugMenu = false;
    }
}