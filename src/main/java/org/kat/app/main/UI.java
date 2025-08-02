package org.kat.app.main;

import org.kat.app.graphics.AssetPool;
import org.kat.app.level.LevelState;
import org.kat.app.ui.UIBuilder;
import org.kat.app.ui.hustcore.*;
import org.kat.app.ui.views.UIManager;
import org.kat.app.ui.views.UIScreen;
import org.kat.app.ui.views.View;
import org.kat.app.util.Tree;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static org.kat.app.main.GamePanel.*;
import static org.kat.app.main.KeyHandler.*;

public class UI {
    private GamePanel gp;
    public Graphics2D g2;
    public static Font joystix;
    public static Font maru;
    public static Font bitcrusher;

    int subState = 0;
    public int selectedOption = -1;
    public final int correctAnswer = 3;
    Color checkPassword = new Color(0 , 0 , 0);
    String maskedPassword;

    int selectedSlot = -1;

    public int commandNum = 0;

    public final static UIManager _UIManager = new UIManager();

    private static BufferedImage gameOverBackground;
    private static BufferedImage hpFrame, manaFrame, boss_hpFrame;
    public static BufferedImage titleBackground;
    private static BufferedImage quizImage;

    static{
        try {
            AssetPool.loadAll(AssetPool.UI_FOLDER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UI(GamePanel gp) {
        this.gp = gp;
        try {
            InputStream is1 = getClass().getResourceAsStream("/textures/font/joystix monospace.otf");
            InputStream is2 = getClass().getResourceAsStream("/textures/font/MaruMonica.ttf");
            InputStream is3 = getClass().getResourceAsStream("/textures/font/bitcrusher.otf");
            joystix = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(is1));
            maru= Font.createFont(Font.TRUETYPE_FONT , Objects.requireNonNull(is2));
            bitcrusher = Font.createFont(Font.TRUETYPE_FONT , Objects.requireNonNull(is3));
        } catch (FontFormatException | IOException | NullPointerException e) {
            e.printStackTrace();
        }

        hpFrame = AssetPool.getImage("hpFrame.png");
        manaFrame = AssetPool.getImage("manaFrame.png");
        titleBackground = AssetPool.getImage("Background.png");
        gameOverBackground = AssetPool.getImage("gameOverBackground.png");
        quizImage = AssetPool.getImage("quiz.png");
        boss_hpFrame = AssetPool.getImage("boss_hpFrame.png");

        Tree<View> mainTree = UIBuilder.buildFromXML("/layout/ui/main_menu.xml");
        Tree<View> settingsTree = UIBuilder.buildFromXML("/layout/ui/setting_menu.xml");
        Tree<View> loadingTree = UIBuilder.buildFromXML("/layout/ui/loading_menu.xml");
        Tree<View> instructionTree = UIBuilder.buildFromXML("/layout/ui/instruction_menu.xml");
        Tree<View> speechDisplayTree = UIBuilder.buildFromXML("/layout/ui/speech_display.xml");
        Tree<View> settingPauseTree = UIBuilder.buildFromXML("/layout/ui/setting_pause.xml");
        Tree<View> playUI = UIBuilder.buildFromXML("/layout/ui/play_ui.xml");

        UIScreen MAIN_MENU = new MainMenu("main_menu", mainTree);
        UIScreen SETTINGS_MENU = new SettingsMenu("setting_menu", settingsTree);
        UIScreen LOADING_MENU = new LoadingMenu("loading_menu", loadingTree);
        UIScreen SPEECH_DISPLAY = new SpeechDisplay("speech_display", speechDisplayTree);
        UIScreen INSTRUCTION_MENU = new InstructionMenu("instruction_menu", instructionTree);
        UIScreen SETTING_PAUSE = new SettingsPause("setting_pause", settingPauseTree);
        UIScreen PLAY_UI = new PlayUI("play_ui", playUI);

        _UIManager.registerUIScreen(MAIN_MENU);
        _UIManager.registerUIScreen(SETTINGS_MENU);
        _UIManager.registerUIScreen(LOADING_MENU);
        _UIManager.registerUIScreen(SPEECH_DISPLAY);
        _UIManager.registerUIScreen(INSTRUCTION_MENU);
        _UIManager.registerUIScreen(SETTING_PAUSE);
        _UIManager.registerUIScreen(PLAY_UI);

        _UIManager.setCurrentScreen("main_menu");
        _UIManager.setPlayScreen("play_ui");
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0,0,0, 178);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255,255,255); // WHITE
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x, y, width, height, 25, 25);
    }

    public int getXForCenteredText(String text)
    {
        int length = (int)g2.getFontMetrics().getStringBounds(text , g2).getWidth();
        return windowWidth / 2 - length / 2;
    }

    public void drawHPBarForBoss() {
        if(currentMap != null && currentMap.boss != null) {
            int fullHPWidth = 205;
            int hpBarHeight = 12;
            int x = windowWidth - 250;
            int y = windowHeight - 92;
            int currentHPWidth;
            try {
                currentHPWidth = (int) ((double) currentMap.boss.currentHP / currentMap.boss.maxHP * fullHPWidth);
            } catch (NullPointerException e) {
                currentHPWidth = 0;
            }
            // Vẽ nền (màu xám) cho thanh HP
            g2.drawImage(boss_hpFrame, x, y, 242, 36, null);
            g2.setFont(joystix.deriveFont(Font.PLAIN, 19f));
            g2.drawString("AI đầu não", x, y - 8);

            // Vẽ thanh HP hiện tại (màu đỏ)
            g2.setColor(new Color(255, 0, 255));
            g2.fillRect(x + 209 - currentHPWidth, y + 12, currentHPWidth, hpBarHeight);
        }
    }


        public void drawGameOverScreen() {
        //BACKGROUND
        g2.drawImage(gameOverBackground, 0, 0, windowWidth, windowHeight, null);
        //MENU
        g2.setFont(joystix.deriveFont(Font.BOLD, 30f));

        String text = "MÀN HÌNH CHÍNH";
        int length = (int)g2.getFontMetrics().getStringBounds(text , g2).getWidth();
        int x = getXForCenteredText(text);

        g2.setColor(Color.WHITE);
        text = "THUA";
        g2.setFont(g2.getFont().deriveFont(Font.BOLD , 50f));

        g2.setColor(Color.BLACK);
        g2.drawString(text , getXForCenteredText(text) - 2 ,windowHeight / 4 + 5);
        g2.setColor(Color.WHITE);
        g2.drawString(text , getXForCenteredText(text) - 7 ,windowHeight / 4);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD , 30f));
        text = "THỬ LẠI";
        int y = windowHeight / 2;
        g2.drawString(text, x + tileSize * 2, y-5);
        //BOUND
        g2.setColor(Color.darkGray);
        g2.drawRoundRect(x - tileSize / 2, y - tileSize, length + tileSize, tileSize + 10, 40, 40);

        if(commandNum == 0) {
            g2.setColor(Color.white);
            g2.drawString(">", x - tileSize - 10, y);
        }

        g2.setColor(Color.WHITE);

        text = "MÀN HÌNH CHÍNH";
        y += tileSize + 20;
        g2.drawString(text, x, y);
        //BOUND
            g2.setColor(Color.darkGray);
        g2.drawRoundRect(x - tileSize / 2, y - tileSize, length + tileSize, tileSize + 10, 40, 40);
        if(commandNum == 1) {
            g2.setColor(Color.WHITE);
            g2.drawString(">", x - tileSize - 10, y);
        }

        g2.setColor(Color.WHITE);
        text = "THOÁT";
        y += tileSize + 20;
        g2.drawString(text, x + tileSize * 3 - 20, y);
        //BOUND
            g2.setColor(Color.darkGray);
        g2.drawRoundRect(x - tileSize / 2, y - tileSize, length + tileSize, tileSize + 10, 40, 40);
        if(commandNum == 2) {
            g2.setColor(Color.white);
            g2.drawString(">", x - tileSize - 10, y);
        }
    }

    public void drawOptionsScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        // SUB WINDOW

        int frameX = tileSize * 4;
        int frameY = tileSize;
        int frameWidth = tileSize * 8;
        int frameHeight = tileSize * 10;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        if (subState == 0) {
            options_top(frameX, frameY);
        }
    }
    public void options_top(int frameX, int frameY) {
        int textX, textY;
        // Thử với Consolas
        g2.setFont(joystix.deriveFont(Font.PLAIN, 19));
        // TITLE
        String text = "TÙY CHỌN";
        textX = getXForCenteredText(text);
        textY = frameY + tileSize;
        g2.drawString(text, textX, textY);

        // MUSIC
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("NHẠC", textX, textY);
        drawSubWindow(textX+185, textY-25 ,tileSize*3/2, tileSize);
        g2.drawString(String.valueOf(music.volumePercentage), textX + 202, textY + 5);
        g2.drawString("-",textX +150, textY);
        g2.drawString("+",textX+280, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX-25, textY);
        }

        // SE
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("SFX", textX, textY);
        drawSubWindow(textX+185, textY-25 ,tileSize*3/2, tileSize);
        g2.drawString(String.valueOf(se.volumePercentage), textX + 202, textY + 5);
        g2.drawString("-",textX +150, textY);
        g2.drawString("+",textX+280, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX-25, textY);
        }

        // RETRY
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("THỬ LẠI", textX, textY);
        if (commandNum == 2) {
            g2.drawString(">", textX-25, textY);
        }

        // EXIT
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("THOÁT", textX, textY);
        if (commandNum == 3) {
            g2.drawString(">", textX-25, textY);
        }
    }

    public void render(Graphics2D g2) {
        this.g2 = g2;
        _UIManager.render(g2);
    }

    public void update(){
//        if(currentLevel.checkState(LevelState.PASSWORD)){
//            handlePasswordPressed();
//            maskedPassword = "*".repeat(currentLevel.getEnteredPassword().length());
//        }
        _UIManager.update();
//        if(gameState == GameState.PASSWORD){
//            handlePasswordPressed();
//            maskedPassword = "*".repeat(currentLevel.enteredPassword.length());
//        }
    }

    public void drawSettingScreen(){
        g2.drawImage(titleBackground,0, 0, windowWidth, windowHeight, null);
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        // SUB WINDOW

        int frameX = tileSize * 4;
        int frameY = tileSize;
        int frameWidth = tileSize * 8;
        int frameHeight = tileSize * 10;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch(subState) {
            case 0: setting_top(frameX, frameY); break;
            case 1: control(frameX, frameY); break;
        }
    }
    public void setting_top(int frameX, int frameY) {
        int textX, textY;
        // Thử với Consolas
        g2.setFont(joystix.deriveFont(Font.PLAIN, 19));
        // TITLE
        String text = "TÙY CHỌN";
        textX = getXForCenteredText(text);
        textY = frameY + tileSize;
        g2.drawString(text, textX, textY);
        // MUSIC
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("NHẠC", textX, textY);
        drawSubWindow(textX+185, textY-25 ,tileSize*3/2, tileSize);
        g2.drawString(String.valueOf(music.volumePercentage), textX + 202, textY + 5);
        g2.drawString("-",textX +150, textY);
        g2.drawString("+",textX+280, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX-25, textY);
        }

        // SE
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("HIỆU ỨNG", textX, textY);
        drawSubWindow(textX+185, textY-25 ,tileSize*3/2, tileSize);
        g2.drawString(String.valueOf(se.volumePercentage), textX + 202, textY + 5);
        g2.drawString("-",textX +150, textY);
        g2.drawString("+",textX+280, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX-25, textY);
        }

        //CONTROL
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("ĐIỀU KHIỂN", textX, textY);
        if (commandNum == 2) {
            g2.drawString(">", textX-25, textY);
        }

        // EXIT
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("Quay lại", textX, textY);
        if (commandNum == 3) {
            g2.drawString(">", textX-25, textY);
        }
    }
    public void control(int frameX, int frameY){
        int textX, textY;
        g2.setFont(joystix.deriveFont(Font.PLAIN, 19));
        //TITLE
        String text = "TÙY CHỌN";
        textX = getXForCenteredText(text);
        textY = frameY + tileSize;
        g2.drawString(text, textX, textY);

        //BANG HO TRO
        textX = frameX + tileSize/2;
        textY += tileSize;

        g2.drawString("DI CHUYỂN", textX, textY);
        textY+=tileSize;

        g2.drawString("BẮN", textX, textY);
        textY+=tileSize;

        g2.drawString("DÙNG VẬT PHẨM", textX, textY);
        textY+=tileSize;

        g2.drawString("DỪNG/QUAY LẠI", textX, textY);
        textY+=tileSize;

        g2.drawString("QUA HỘI THOẠI", textX, textY);

        //THONG TIN
        textX = frameX + tileSize*11/2;
        textY = frameY + tileSize*2;

        g2.drawString("W A S D", textX, textY);
        textY+=tileSize;

        g2.drawString("ENTER", textX, textY);
        textY+=tileSize;

        g2.drawString("1 2 3 4", textX, textY);
        textY+=tileSize;

        g2.drawString("ESC", textX, textY);
        textY+=tileSize;

        g2.drawString("ENTER", textX, textY);
    }
    private void drawPasswordInputBox() {
        int x = 100;
        int y = 100;
        int width = gp.getWidth() - tileSize * 4;
        int height = 200;

        drawSubWindow(x, y, width, height);

        g2.setColor(Color.WHITE);
        g2.setFont(joystix.deriveFont(Font.PLAIN, 20));
        g2.drawString("Nhập mật khẩu:", x + 20, y + 80);
        drawSubWindow(x+300,y+55,200,30);

        if(currentLevel.getEnteredPassword().isEmpty()) checkPassword = Color.WHITE;
        g2.setColor(checkPassword);
        maskedPassword = "*".repeat(currentLevel.getEnteredPassword().length());
        g2.drawString(maskedPassword, x + 310, y + 75);

        g2.setColor(Color.WHITE);
        g2.setFont(joystix.deriveFont(Font.PLAIN, 20));
        g2.drawString("Nhấn Enter để xác nhận", x + 20, y + 150);
    }
    private void handlePasswordPressed(){
        String charPressed = "";
        if(enterPressed){
            enterPressed = false;
            currentLevel.enableCheckPassword(true);
            if (currentLevel.isCorrect) {
                checkPassword = Color.GREEN;
            } else {
                checkPassword = Color.RED;
            }
        }
        if(key0pressed) {charPressed = "0"; key0pressed = false;} else
        if(key1pressed) {charPressed = "1"; key1pressed = false;} else
        if(key2pressed) {charPressed = "2"; key2pressed = false;} else
        if(key3pressed) {charPressed = "3"; key3pressed = false;} else
        if(key4pressed) {charPressed = "4"; key4pressed = false;} else
        if(key5pressed) {charPressed = "5"; key5pressed = false;} else
        if(key6pressed) {charPressed = "6"; key6pressed = false;} else
        if(key7pressed) {charPressed = "7"; key7pressed = false;} else
        if(key8pressed) {charPressed = "8"; key8pressed = false;} else
        if(key9pressed) {charPressed = "9"; key9pressed = false;}

        if(currentLevel.getEnteredPassword().length() < 12) {
            currentLevel.appendChar(charPressed);
            checkPassword = Color.WHITE;
        }

        if (keyBackspacepressed) {
            keyBackspacepressed = false;
            if (!currentLevel.getEnteredPassword().isEmpty()) {
                currentLevel.popChar();
                checkPassword = Color.WHITE;
            }
        }
        if(keyEscpressed) {
            currentLevel.clearPassword();
            currentLevel.setLevelState(LevelState.RUNNING);
            currentLevel.enableCheckPassword(false);
        }
    }

    public void dispose() {
        maskedPassword = null;
    }

    private void setupMainMenu(){

    }
}