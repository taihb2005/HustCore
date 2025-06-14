package main;

import level.Level;
import level.LevelState;
import thread.LoadingService;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.exit;
import static main.GamePanel.*;

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
    public static boolean keyEpressed = false;
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
        if(!gameCompleted) {
            if (GamePanel.gameState == GameState.MENU) {
                playSE(11);
                if (keyCode == KeyEvent.VK_W) {
                    GamePanel.ui.commandNum--;
                    if (GamePanel.ui.commandNum < 0) {
                        GamePanel.ui.commandNum = 2;
                    }
                }
                if (keyCode == KeyEvent.VK_S) {
                    GamePanel.ui.commandNum++;
                    if (GamePanel.ui.commandNum > 2) {
                        GamePanel.ui.commandNum = 0;
                    }
                }
                if (keyCode == KeyEvent.VK_ENTER) {
                    if (GamePanel.ui.commandNum == 0) {
                        disableKey();
                        LoadingService.loadResource();
                        LoadingService.loadMap();
//                        gp.darker = true;
//                        TimerTask startToPlayAnimation = new TimerTask() {
//                            @Override
//                            public void run() {
//                                gp.restart();
//                                GamePanel.gameState = GameState.PLAY;
//                                gp.lighter = true;
//                            }
//                        };
//                        timer.schedule(startToPlayAnimation, 1500);
                    }
                    if (GamePanel.ui.commandNum == 1) {
                        GamePanel.gameState = GameState.SETTING;
                    }

                    if (GamePanel.ui.commandNum == 2) {
                        System.exit(0);
                    }
                }
            }

            if (GamePanel.gameState == GameState.PLAY) {
                LevelState levelState = currentLevel.getLevelState();
                if(levelState == LevelState.RUNNING) {
                    if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) downPressed = true;
                    if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) upPressed = true;
                    if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) rightPressed = true;
                    if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) leftPressed = true;
                    if (keyCode == KeyEvent.VK_ESCAPE) GamePanel.gameState = GameState.PAUSE;
                    if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) enterPressed = true;
                    if (keyCode == KeyEvent.VK_E) keyEpressed = true;
                    if (keyCode == KeyEvent.VK_1) key1pressed = true;
                    if (keyCode == KeyEvent.VK_2) key2pressed = true;
                    if (keyCode == KeyEvent.VK_3) key3pressed = true;
                    if (keyCode == KeyEvent.VK_4) key4pressed = true;
                    if (keyCode == KeyEvent.VK_5) key5pressed = true;
                    if (keyCode == KeyEvent.VK_F3) showDebugMenu = true;
                    if (keyCode == KeyEvent.VK_F2) godModeOn = true;
                } else if (levelState == LevelState.DIALOGUE){
                    if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) enterPressed = true;
                } else if (levelState == LevelState.PASSWORD){
                    if (keyCode == KeyEvent.VK_0) key0pressed = true;
                    if (keyCode == KeyEvent.VK_1) key1pressed = true;
                    if (keyCode == KeyEvent.VK_2) key2pressed = true;
                    if (keyCode == KeyEvent.VK_3) key3pressed = true;
                    if (keyCode == KeyEvent.VK_4) key4pressed = true;
                    if (keyCode == KeyEvent.VK_5) key5pressed = true;
                    if (keyCode == KeyEvent.VK_6) key6pressed = true;
                    if (keyCode == KeyEvent.VK_7) key7pressed = true;
                    if (keyCode == KeyEvent.VK_8) key8pressed = true;
                    if (keyCode == KeyEvent.VK_9) key9pressed = true;
                    if (keyCode == KeyEvent.VK_ESCAPE) keyEscpressed = true;
                    if (keyCode == KeyEvent.VK_ENTER) enterPressed = true;
                    if (keyCode == KeyEvent.VK_BACK_SPACE) keyBackspacepressed = true;
                } else if (levelState == LevelState.QUIZ){
                    if (GamePanel.ui.selectedOption < 4) {
                        if (keyCode == KeyEvent.VK_A) {
                            GamePanel.ui.selectedOption = 0; // Đáp án A
                        } else if (keyCode == KeyEvent.VK_B) {
                            GamePanel.ui.selectedOption = 1; // Đáp án B
                        } else if (keyCode == KeyEvent.VK_C) {
                            GamePanel.ui.selectedOption = 2; // Đáp án C
                        } else if (keyCode == KeyEvent.VK_D) {
                            GamePanel.ui.selectedOption = 3; // Đáp án D
                        }
                    }

                    if (keyCode == KeyEvent.VK_ENTER && GamePanel.ui.selectedOption >= 4) {
                        GamePanel.gameState = GameState.PLAY;
                        Timer timer = new Timer();

                        TimerTask play = new TimerTask() {
                            @Override
                            public void run() {
                                stopMusic();
                                stopMusic();
                                playMusic(10);
                            }
                        };
                        timer.schedule(play , 200);
                    }

                    if (keyCode == KeyEvent.VK_ENTER && GamePanel.ui.selectedOption != -1 && GamePanel.ui.selectedOption < 4) {
                        stopMusic();
                        playSE(12);
                        if (GamePanel.ui.selectedOption == GamePanel.ui.correctAnswer) {
                            GamePanel.ui.selectedOption = 4;
                        } else {
                            GamePanel.ui.selectedOption = 5;
                        }
                    }
                }
            } else if (GamePanel.gameState == GameState.PAUSE) {
                if (keyCode == KeyEvent.VK_ESCAPE)
                    GamePanel.gameState = GameState.PLAY;
                // FOR OPTIONS
                int maxCommandNum = 0;
                switch (GamePanel.ui.subState) {
                    case 0:
                        maxCommandNum = 3;
                }

                if (keyCode == KeyEvent.VK_W) {
                    GamePanel.ui.commandNum--;
                    if (GamePanel.ui.commandNum < 0) {
                        GamePanel.ui.commandNum = maxCommandNum;
                    }
                }
                if (keyCode == KeyEvent.VK_S) {
                    GamePanel.ui.commandNum++;
                    if (GamePanel.ui.commandNum > maxCommandNum) {
                        GamePanel.ui.commandNum = 0;
                    }
                }
                if (keyCode == KeyEvent.VK_A) {
                    if (GamePanel.ui.subState == 0) {
                        if (GamePanel.ui.commandNum == 0 && GamePanel.music.volumePercentage > 0) {
                            GamePanel.music.volumePercentage -= 10;
                            GamePanel.music.checkVolume(GamePanel.music.volumePercentage);
                        }
                        if (GamePanel.ui.commandNum == 1 && GamePanel.se.volumePercentage > 0) {
                            GamePanel.se.volumePercentage -= 10;
                            GamePanel.se.checkVolume(GamePanel.se.volumePercentage);
                        }
                    }
                }
                if (keyCode == KeyEvent.VK_D) {
                    if (GamePanel.ui.subState == 0) {
                        if (GamePanel.ui.commandNum == 0 && GamePanel.music.volumePercentage < 100) {
                            GamePanel.music.volumePercentage += 10;
                            GamePanel.music.checkVolume(GamePanel.music.volumePercentage);
                        }
                        if (GamePanel.ui.commandNum == 1 && GamePanel.se.volumePercentage < 100) {
                            GamePanel.se.volumePercentage += 10;
                            GamePanel.se.checkVolume(GamePanel.se.volumePercentage);
                        }
                    }
                }
                if (keyCode == KeyEvent.VK_ENTER) {
                    if (GamePanel.ui.subState == 0) {
                        if (GamePanel.ui.commandNum == 2) {
                            LoadingService.restart();
                        }
                        if (GamePanel.ui.commandNum == 3) {
                            GamePanel.gameState = GameState.MENU;
                            GamePanel.ui.commandNum = 0;
                        }
                    }
                }
            } else if (GamePanel.gameState == GameState.DIALOGUE) {
                if (keyCode == KeyEvent.VK_ENTER) {
                    enterPressed = true;
                }
            } else if (GamePanel.gameState == GameState.LOSE) {
                int maxCommandNum = 2;

            /*if(keyCode == KeyEvent.VK_SPACE)
            {
                GamePanel.gameState = GameState.MENU;
            } */
                if (keyCode == KeyEvent.VK_W) {
                    GamePanel.ui.commandNum--;
                    if (GamePanel.ui.commandNum < 0) {
                        GamePanel.ui.commandNum = maxCommandNum;
                    }
                }
                if (keyCode == KeyEvent.VK_S) {
                    GamePanel.ui.commandNum++;
                    if (GamePanel.ui.commandNum > maxCommandNum) {
                        GamePanel.ui.commandNum = 0;
                    }
                }
                if (keyCode == KeyEvent.VK_ENTER) {
                    if (GamePanel.ui.commandNum == 0) {
                        LoadingService.restart();
                        GamePanel.gameState = GameState.PLAY;
                    } else if (GamePanel.ui.commandNum == 1) {
                        GamePanel.gameState = GameState.MENU;
                    } else {
                        exit(0);
                    }
                }
            } else if (GamePanel.gameState == GameState.SETTING) {
                playSE(11);
                if (keyCode == KeyEvent.VK_ESCAPE) {
                    if (GamePanel.ui.subState == 0) {
                        GamePanel.gameState = GameState.MENU;
                        GamePanel.ui.commandNum = 1;
                    }
                    if (GamePanel.ui.subState == 1) {
                        GamePanel.ui.subState = 0;

                    }

                }
                // FOR OPTIONS
                int maxCommandNum = 0;
                switch (GamePanel.ui.subState) {
                    case 0:
                        maxCommandNum = 3;
                    case 1:
                        maxCommandNum = 5;
                }

                if (keyCode == KeyEvent.VK_W) {
                    GamePanel.ui.commandNum--;
                    if (GamePanel.ui.commandNum < 0) {
                        GamePanel.ui.commandNum = 3;
                    }

                }
                if (keyCode == KeyEvent.VK_S) {
                    GamePanel.ui.commandNum++;
                    if (GamePanel.ui.commandNum > 3) {
                        GamePanel.ui.commandNum = 0;
                    }

                }
                if (keyCode == KeyEvent.VK_A) {
                    if (GamePanel.ui.subState == 0) {
                        if (GamePanel.ui.commandNum == 0 && GamePanel.music.volumePercentage > 0) {
                            GamePanel.music.volumePercentage -= 10;
                            GamePanel.music.checkVolume(GamePanel.music.volumePercentage);
                        }
                        if (GamePanel.ui.commandNum == 1 && GamePanel.se.volumePercentage > 0) {
                            GamePanel.se.volumePercentage -= 10;
                            GamePanel.se.checkVolume(GamePanel.se.volumePercentage);
                        }
                    }
                }
                if (keyCode == KeyEvent.VK_D) {
                    if (GamePanel.ui.subState == 0) {
                        if (GamePanel.ui.commandNum == 0 && GamePanel.music.volumePercentage < 100) {
                            GamePanel.music.volumePercentage += 10;
                            GamePanel.music.checkVolume(GamePanel.music.volumePercentage);
                        }
                        if (GamePanel.ui.commandNum == 1 && GamePanel.se.volumePercentage < 100) {
                            GamePanel.se.volumePercentage += 10;
                            GamePanel.se.checkVolume(GamePanel.se.volumePercentage);
                        }
                    }
                }
                if (keyCode == KeyEvent.VK_ENTER) {
                    if (GamePanel.ui.subState == 0) {
                        if (GamePanel.ui.commandNum == 2) {
                            GamePanel.ui.subState = 1;
                        }
                        if (GamePanel.ui.commandNum == 3) {
                            GamePanel.gameState = GameState.MENU;
                            GamePanel.ui.commandNum = 0;
                        }
                    }
                }
            } else if (GamePanel.gameState == GameState.QUIZ) {
                if (GamePanel.ui.selectedOption < 4) {
                    if (keyCode == KeyEvent.VK_A) {
                        GamePanel.ui.selectedOption = 0; // Đáp án A
                    } else if (keyCode == KeyEvent.VK_B) {
                        GamePanel.ui.selectedOption = 1; // Đáp án B
                    } else if (keyCode == KeyEvent.VK_C) {
                        GamePanel.ui.selectedOption = 2; // Đáp án C
                    } else if (keyCode == KeyEvent.VK_D) {
                        GamePanel.ui.selectedOption = 3; // Đáp án D
                    }
                }

                if (keyCode == KeyEvent.VK_ENTER && GamePanel.ui.selectedOption >= 4) {
                    GamePanel.gameState = GameState.PLAY;
                    Timer timer = new Timer();

                    TimerTask play = new TimerTask() {
                        @Override
                        public void run() {
                            stopMusic();
                            stopMusic();
                            playMusic(10);
                        }
                    };
                    timer.schedule(play , 200);
                }

                if (keyCode == KeyEvent.VK_ENTER && GamePanel.ui.selectedOption != -1 && GamePanel.ui.selectedOption < 4) {
                    stopMusic();
                    playSE(12);
                    if (GamePanel.ui.selectedOption == GamePanel.ui.correctAnswer) {
                        GamePanel.ui.selectedOption = 4;
                    } else {
                        GamePanel.ui.selectedOption = 5;
                    }
                }
            }
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
}