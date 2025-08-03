package org.kat.app.main;

import org.kat.app.level.LevelState;
import org.kat.app.thread.LoadingService;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.exit;
import static org.kat.app.main.GamePanel.*;

public class KeyHandler implements KeyListener{

    GamePanel gp;
    public static boolean upPressed;
    public static boolean downPressed;
    public static boolean rightPressed;
    public static boolean leftPressed;
    public static boolean enterPressed;
    public static boolean enterReleased;
    public static boolean showDebugMenu = false;
    public static boolean showHitbox = false;
    public static boolean godModeOn = false;
    public static boolean keyEscpressed;
    public static boolean key0pressed;
    public static boolean key1pressed;
    public static boolean key2pressed;
    public static boolean key3pressed;
    public static boolean key4pressed;
    public static boolean key5pressed;
    public static boolean key6pressed;
    public static boolean key7pressed;
    public static boolean key8pressed;
    public static boolean key9pressed;
    public static boolean keyEpressed = false;

    public static boolean keyBackspacepressed;
    private final Timer timer = new Timer();


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
        if (gameCompleted) return;

        switch (GamePanel.gameState) {
            case MENU, PAUSE, LOSE -> handleMenuLikeStates(keyCode);
            case PLAY -> handlePlayState(keyCode);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode)
        {
            case KeyEvent.VK_S, KeyEvent.VK_DOWN: downPressed = false; break;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT: leftPressed = false; break;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT: rightPressed = false; break;
            case KeyEvent.VK_W, KeyEvent.VK_UP: upPressed = false; break;
            case KeyEvent.VK_ENTER , KeyEvent.VK_SPACE: enterPressed = false; enterReleased = true; break;
            case KeyEvent.VK_E: keyEpressed = false ; break;
            case KeyEvent.VK_ESCAPE:keyEscpressed = false; break;
            case KeyEvent.VK_0: key0pressed = true; break;
            case KeyEvent.VK_1: key1pressed = false; break;
            case KeyEvent.VK_2: key2pressed = false; break;
            case KeyEvent.VK_3: key3pressed = false; break;
            case KeyEvent.VK_4: key4pressed = false; break;
            case KeyEvent.VK_5: key5pressed = false; break;
            case KeyEvent.VK_6: key6pressed = false; break;
            case KeyEvent.VK_7: key7pressed = false; break;
            case KeyEvent.VK_8: key8pressed = false; break;
            case KeyEvent.VK_9: key9pressed = false; break;
            case KeyEvent.VK_BACK_SPACE: keyBackspacepressed = false; break;
        }
    }

    private void handleMenuLikeStates(int keyCode) {
        if (isUp(keyCode)) upPressed = true;
        if (isDown(keyCode)) downPressed = true;
        if (isLeft(keyCode)) leftPressed = true;
        if (isRight(keyCode)) rightPressed = true;
        if (keyCode == KeyEvent.VK_ESCAPE) keyEscpressed = true;
        if (isConfirm(keyCode)) enterPressed = true;
    }

    private void handlePlayState(int keyCode) {
        switch (currentLevel.getLevelState()) {
            case RUNNING -> {
                if (isUp(keyCode)) upPressed = true;
                if (isDown(keyCode)) downPressed = true;
                if (isLeft(keyCode)) leftPressed = true;
                if (isRight(keyCode)) rightPressed = true;
                if (keyCode == KeyEvent.VK_ESCAPE) keyEscpressed = true;
                if (isConfirm(keyCode)) enterPressed = true;
                if (keyCode == KeyEvent.VK_E) keyEpressed = true;

                switch (keyCode) {
                    case KeyEvent.VK_1 -> key1pressed = true;
                    case KeyEvent.VK_2 -> key2pressed = true;
                    case KeyEvent.VK_3 -> key3pressed = true;
                    case KeyEvent.VK_4 -> key4pressed = true;
                    case KeyEvent.VK_5 -> key5pressed = true;
                    case KeyEvent.VK_F3 -> showDebugMenu = true;
                    case KeyEvent.VK_F2 -> godModeOn = true;
                }
            }
            case DIALOGUE -> {
                if (isConfirm(keyCode)) enterPressed = true;
                if (isUp(keyCode)) upPressed = true;
                if (isDown(keyCode)) downPressed = true;
            }
            case PASSWORD -> {
                switch (keyCode) {
                    case KeyEvent.VK_0 -> key0pressed = true;
                    case KeyEvent.VK_1 -> key1pressed = true;
                    case KeyEvent.VK_2 -> key2pressed = true;
                    case KeyEvent.VK_3 -> key3pressed = true;
                    case KeyEvent.VK_4 -> key4pressed = true;
                    case KeyEvent.VK_5 -> key5pressed = true;
                    case KeyEvent.VK_6 -> key6pressed = true;
                    case KeyEvent.VK_7 -> key7pressed = true;
                    case KeyEvent.VK_8 -> key8pressed = true;
                    case KeyEvent.VK_9 -> key9pressed = true;
                    case KeyEvent.VK_BACK_SPACE -> keyBackspacepressed = true;
                    case KeyEvent.VK_ESCAPE -> keyEscpressed = true;
                    case KeyEvent.VK_ENTER -> enterPressed = true;
                }
            }
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
        keyEpressed = false;
        keyEscpressed = false;
        key0pressed = false;
        key1pressed = false;
        key2pressed = false;
        key3pressed = false;
        key4pressed = false;
        key5pressed = false;
        key6pressed = false;
        key7pressed = false;
        key8pressed = false;
        key9pressed = false;
    }

    private boolean isUp(int key) {
        return key == KeyEvent.VK_W || key == KeyEvent.VK_UP;
    }

    private boolean isDown(int key) {
        return key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN;
    }

    private boolean isLeft(int key) {
        return key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT;
    }

    private boolean isRight(int key) {
        return key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT;
    }

    private boolean isConfirm(int key) {
        return key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE;
    }

    private boolean isExit(int key){
        return key == KeyEvent.VK_ESCAPE;
    }

    private boolean isNumber(int key){
        return (key >= KeyEvent.VK_0 && key <= KeyEvent.VK_9) ||
                (key >= KeyEvent.VK_NUMPAD0 && key <= KeyEvent.VK_NUMPAD9);
    }

    public boolean isChar(char c) {
        return Character.isLetterOrDigit(c) || isVietnameseChar(c) || isSymbol(c) || c == ' ';
    }


    private static boolean isSymbol(char c) {
        return "!@#$%^&*()-_=+[{]}|;:'\",<.>/?`~".indexOf(c) != -1;
    }

    private static boolean isVietnameseChar(char c) {
        String vietnamese = "ăâđêôơưĂÂĐÊÔƠƯáàảãạấầẩẫậắằẳẵặ" +
                "éèẻẽẹếềểễệ" +
                "íìỉĩị" +
                "óòỏõọốồổỗộớờởỡợ" +
                "úùủũụứừửữự" +
                "ýỳỷỹỵ" +
                "ÁÀẢÃẠẤẦẨẪẬẮẰẲẴẶ" +
                "ÉÈẺẼẸẾỀỂỄỆ" +
                "ÍÌỈĨỊ" +
                "ÓÒỎÕỌỐỒỔỖỘỚỜỞỠỢ" +
                "ÚÙỦŨỤỨỪỬỮỰ" +
                "ÝỲỶỸỴ";

        return vietnamese.indexOf(c) != -1;
    }


}