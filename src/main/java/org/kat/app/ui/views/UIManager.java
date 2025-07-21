package org.kat.app.ui.views;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIManager {
    UIScreen currentScreen;

    private final Map<String, UIScreen> UIMap = new HashMap<>();

    public void registerUIScreen(UIScreen screen){
        UIMap.put(screen.getId(), screen);
    }

    public UIScreen findUIScreenByName(String name){
        return UIMap.get(name);
    }

    public void setCurrentScreen(String name){
        currentScreen = findUIScreenByName(name);
    }

    public void removeUIScreenByName(String name){
        UIMap.remove(name);
    }

    public UIScreen getCurrentScreen() {
        return currentScreen;
    }

    public void update(){
        if(currentScreen != null){
            currentScreen.update();
        }
    }

    public void render(Graphics2D g2){
        if(currentScreen != null){
            currentScreen.render(g2);
        }
    }
}
