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
    public static boolean godModeOn = false;
    public static boolean key1pressed;
    public static boolean key2pressed;
    public static boolean key3pressed;
    public static boolean key4pressed;
    public static boolean key5pressed;


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
            if(keyCode == KeyEvent.VK_W){
                GamePanel.ui.commandNum--;
                if(GamePanel.ui.commandNum < 0){
                    GamePanel.ui.commandNum = 0;
                }
            }
            if(keyCode == KeyEvent.VK_S){
                GamePanel.ui.commandNum++;
                if(GamePanel.ui.commandNum > 2){
                    GamePanel.ui.commandNum = 2;
                }
            }
            if (keyCode == KeyEvent.VK_ENTER) {
                if(GamePanel.ui.commandNum == 0){
                GamePanel.gameState = GameState.PLAY_STATE;

            }
                if(GamePanel.ui.commandNum == 1){
                    GamePanel.gameState = GameState.SETTING_STATE;
                }
                if(GamePanel.ui.commandNum == 2){
                    System.exit(0);
                }
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
            if(keyCode == KeyEvent.VK_F2){
                godModeOn = true;
            }
            if(keyCode == KeyEvent.VK_ENTER)
            {
                enterPressed = true;
            }
            if(keyCode == KeyEvent.VK_1)
            {
                key1pressed = true;
            }
            if(keyCode == KeyEvent.VK_2)
            {
                key2pressed = true;
            }
            if(keyCode == KeyEvent.VK_3)
            {
                key3pressed = true;
            }
            if(keyCode == KeyEvent.VK_4)
            {
                key4pressed = true;
            }
            if(keyCode == KeyEvent.VK_5)
            {
                key5pressed = true;
            }

        } else
        if(GamePanel.gameState == GameState.PAUSE_STATE||GamePanel.gameState == GameState.SETTING_STATE)
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
                    if (GamePanel.ui.commandNum == 0 && GamePanel.music.volumePercentage > 0) {
                        GamePanel.music.volumePercentage-=10;
                        GamePanel.music.checkVolume(GamePanel.music.volumePercentage);
                    }
                    if (GamePanel.ui.commandNum == 1 && GamePanel.se.volumePercentage > 0) {
                        GamePanel.se.volumePercentage-=10;
                        GamePanel.se.checkVolume(GamePanel.se.volumePercentage);
                    }
                }
            }
            if(keyCode == KeyEvent.VK_D)
            {
                if (GamePanel.ui.subState == 0) {
                    if (GamePanel.ui.commandNum == 0 && GamePanel.music.volumePercentage < 100) {
                        GamePanel.music.volumePercentage+=10;
                        GamePanel.music.checkVolume(GamePanel.music.volumePercentage);
                    }
                    if (GamePanel.ui.commandNum == 1 && GamePanel.se.volumePercentage < 100) {
                        GamePanel.se.volumePercentage+=10;
                        GamePanel.se.checkVolume(GamePanel.se.volumePercentage);
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
                        GamePanel.gameState = GameState.MENU_STATE;
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
            case KeyEvent.VK_1: key1pressed = false; break;
            case KeyEvent.VK_2: key2pressed = false; break;
            case KeyEvent.VK_3: key3pressed = false; break;
            case KeyEvent.VK_4: key4pressed = false; break;
            case KeyEvent.VK_5: key5pressed = false; break;
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