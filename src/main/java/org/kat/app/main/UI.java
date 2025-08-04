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
    public Graphics2D g2;
    public static Font joystix;
    public static Font maru;
    public static Font bitcrusher;

    public final static UIManager _UIManager = new UIManager();

    static{
        try {
            AssetPool.loadAll(AssetPool.UI_FOLDER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UI() {
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


        Tree<View> mainTree = UIBuilder.buildFromXML("/layout/ui/main_menu.xml");
        Tree<View> settingsTree = UIBuilder.buildFromXML("/layout/ui/setting_menu.xml");
        Tree<View> loadingTree = UIBuilder.buildFromXML("/layout/ui/loading_menu.xml");
        Tree<View> instructionTree = UIBuilder.buildFromXML("/layout/ui/instruction_menu.xml");
        Tree<View> speechDisplayTree = UIBuilder.buildFromXML("/layout/ui/speech_display.xml");
        Tree<View> settingPauseTree = UIBuilder.buildFromXML("/layout/ui/setting_pause.xml");
        Tree<View> playUI = UIBuilder.buildFromXML("/layout/ui/play_ui.xml");
        Tree<View> passwordInputTree = UIBuilder.buildFromXML("/layout/ui/password_input.xml");
        Tree<View> loseTree = UIBuilder.buildFromXML("/layout/ui/lose_menu.xml");
        Tree<View> creditTree = UIBuilder.buildFromXML("/layout/ui/end_credit.xml");

        UIScreen MAIN_MENU = new MainMenu("main_menu", mainTree);
        UIScreen SETTINGS_MENU = new SettingsMenu("setting_menu", settingsTree);
        UIScreen LOADING_MENU = new LoadingMenu("loading_menu", loadingTree);
        UIScreen SPEECH_DISPLAY = new SpeechDisplay("speech_display", speechDisplayTree);
        UIScreen INSTRUCTION_MENU = new InstructionMenu("instruction_menu", instructionTree);
        UIScreen SETTING_PAUSE = new SettingsPause("setting_pause", settingPauseTree);
        UIScreen PLAY_UI = new PlayUI("play_ui", playUI);
        UIScreen PASSWORD_INPUT = new PasswordInput("password_input", passwordInputTree);
        UIScreen LOSE_MENU = new LoseScreen("lose_menu", loseTree);
        UIScreen END_CREDIT = new CreditScreen("end_credit", creditTree);

        _UIManager.registerUIScreen(MAIN_MENU);
        _UIManager.registerUIScreen(SETTINGS_MENU);
        _UIManager.registerUIScreen(LOADING_MENU);
        _UIManager.registerUIScreen(SPEECH_DISPLAY);
        _UIManager.registerUIScreen(INSTRUCTION_MENU);
        _UIManager.registerUIScreen(SETTING_PAUSE);
        _UIManager.registerUIScreen(PLAY_UI);
        _UIManager.registerUIScreen(PASSWORD_INPUT);
        _UIManager.registerUIScreen(LOSE_MENU);
        _UIManager.registerUIScreen(END_CREDIT);

        _UIManager.setCurrentScreen("main_menu");
        _UIManager.setPlayScreen("play_ui");
    }

    public void render(Graphics2D g2) {
        this.g2 = g2;
        _UIManager.render(g2);
    }

    public void update(){
        _UIManager.update();
    }

    public void dispose() {

    }
}