package level;

import ai.PathFinder;
import entity.mob.Monster;
import graphics.environment.EnvironmentManager;
import main.GamePanel;
import main.GameState;
import map.GameMap;

import java.awt.*;

import static main.GamePanel.*;
import static main.GamePanel.environmentManager;

public class Level{
    public GamePanel gp;
    public GameMap map;
    protected AssetSetter setter;
    public EventRectangle changeMapEventRect1;
    public EventRectangle changeMapEventRect2;
    public boolean canChangeMap;

    public boolean levelFinished;
    public boolean finishedBeginingDialogue = false;

    public String enteredPassword = "";
    public String correctPassword ;

    public Monster[] monster = new Monster[100];

    public void init(){
        camera.setCamera(windowWidth , windowHeight , map.getMapWidth() ,map.getMapHeight());
        setter = new AssetSetter(map);
        environmentManager = new EnvironmentManager(map);
        environmentManager.setup();
        environmentManager.lighting.setLightRadius(map.getBestLightingRadius());
        canChangeMap = false;
        levelFinished = false;

    };
    public void updateProgress(){}
    public void render(Graphics2D g2){};

    public void dispose(){
        map.dispose();
    }
}

