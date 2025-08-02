package org.kat.app.ui.views;

import org.kat.app.main.GameState;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.kat.app.main.GamePanel.gameState;

public class UIManager {
    public static UIScreen playScreen;
    UIScreen currentScreen;

    private static final Stack<UIScreen> screenStack = new Stack<UIScreen>();
    private static final Map<String, UIScreen> UIMap = new HashMap<>();

    public void registerUIScreen(UIScreen screen){
        UIMap.put(screen.getId(), screen);
    }

    public UIScreen findUIScreenByName(String name){
        return UIMap.get(name);
    }

    public void setPlayScreen(String name){
        if(playScreen != null)
            playScreen.hide();
        playScreen = findUIScreenByName(name);
        playScreen.show();
    }

    public void setCurrentScreen(String name){
        if(currentScreen != null)
            currentScreen.hide();
        currentScreen = findUIScreenByName(name);
        currentScreen.show();
    }

    public void addToScreenStack(String name){
        screenStack.push(findUIScreenByName(name));
        currentScreen = screenStack.peek();
    }

    public void popFromScreenStack(){
        if(!screenStack.isEmpty()){
            currentScreen.hide();
            currentScreen = screenStack.pop();
            if(!screenStack.isEmpty())
                currentScreen = screenStack.peek();
        }
    }

    public void clearFromScreenStack(){
        screenStack.clear();
        if(currentScreen != null) {
            currentScreen.hide();
        }

        currentScreen = null;
    }

    public void removeUIScreenByName(String name){
        UIMap.remove(name);
    }

    public UIScreen getCurrentScreen() {
        return currentScreen;
    }

    public void update(){
        if(playScreen != null){
            if(playScreen.isVisible() && (gameState == GameState.PLAY || gameState == GameState.PAUSE))
                playScreen.update();
        }
        if(currentScreen != null){
            if(currentScreen.isVisible()) currentScreen.update();
        }
    }

    public void render(Graphics2D g2){
        if(playScreen != null){
            if(playScreen.isVisible() && (gameState == GameState.PLAY || gameState == GameState.PAUSE))
                playScreen.render(g2);
        }
        if(currentScreen != null){
            if(currentScreen.isVisible()) currentScreen.render(g2);
        }
    }
}
