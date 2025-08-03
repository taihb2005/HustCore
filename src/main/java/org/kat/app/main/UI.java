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
        Tree<View> passwordInputTree = UIBuilder.buildFromXML("/layout/ui/password_input.xml");
        Tree<View> loseTree = UIBuilder.buildFromXML("/layout/ui/lose_menu.xml");

        UIScreen MAIN_MENU = new MainMenu("main_menu", mainTree);
        UIScreen SETTINGS_MENU = new SettingsMenu("setting_menu", settingsTree);
        UIScreen LOADING_MENU = new LoadingMenu("loading_menu", loadingTree);
        UIScreen SPEECH_DISPLAY = new SpeechDisplay("speech_display", speechDisplayTree);
        UIScreen INSTRUCTION_MENU = new InstructionMenu("instruction_menu", instructionTree);
        UIScreen SETTING_PAUSE = new SettingsPause("setting_pause", settingPauseTree);
        UIScreen PLAY_UI = new PlayUI("play_ui", playUI);
        UIScreen PASSWORD_INPUT = new PasswordInput("password_input", passwordInputTree);
        UIScreen LOSE_MENU = new LoseScreen("lose_menu", loseTree);

        _UIManager.registerUIScreen(MAIN_MENU);
        _UIManager.registerUIScreen(SETTINGS_MENU);
        _UIManager.registerUIScreen(LOADING_MENU);
        _UIManager.registerUIScreen(SPEECH_DISPLAY);
        _UIManager.registerUIScreen(INSTRUCTION_MENU);
        _UIManager.registerUIScreen(SETTING_PAUSE);
        _UIManager.registerUIScreen(PLAY_UI);
        _UIManager.registerUIScreen(PASSWORD_INPUT);
        _UIManager.registerUIScreen(LOSE_MENU);

        _UIManager.setCurrentScreen("main_menu");
        _UIManager.setPlayScreen("play_ui");
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


    public void render(Graphics2D g2) {
        this.g2 = g2;
        _UIManager.render(g2);
    }

    public void update(){
        _UIManager.update();
    }



    public void dispose() {
        maskedPassword = null;
    }
}