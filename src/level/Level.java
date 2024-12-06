package level;

import ai.PathFinder;
import ai.PathFinder2;
import graphics.environment.EnvironmentManager;
import main.GamePanel;
import map.GameMap;
import map.MapManager;
import map.MapParser;

import static main.GamePanel.*;
import static main.GamePanel.environmentManager;

public class Level{
    public GamePanel gp;
    public GameMap map;
    EventHandler eventHandler;
    protected AssetSetter setter;
    protected EventRectangle changeMapEventRect;
    public boolean canChangeMap;

    public boolean levelFinished;
    public boolean finishedBeginingDialogue = false;
    public boolean finishedTutorialDialogue = false;

    public void init(){
        camera.setCamera(windowWidth , windowHeight , map.getMapWidth() ,map.getMapHeight());
        pFinder = new PathFinder2(map);
        setter = new AssetSetter(map);
        environmentManager = new EnvironmentManager(map);
        environmentManager.setup();
        environmentManager.lighting.setLightRadius(map.getBestLightingRadius());
        canChangeMap = false;
        levelFinished = false;
    };
    public void updateProgress(){

    }
}
