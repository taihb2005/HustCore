package level;

import ai.PathFinder;
import ai.PathFinder2;
import environment.EnvironmentManager;
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
    protected EventRectangle eventRect;
    public boolean canChangeMap;

    boolean levelFinished;

    public void init(){
        camera.setCamera(windowWidth , windowHeight , map.getMapWidth() ,map.getMapHeight());
        pFinder = new PathFinder2(map);
        setter = new AssetSetter(map);
        setter.loadAll();
        environmentManager = new EnvironmentManager(map);
        environmentManager.setup();
        environmentManager.lighting.setLightSource(2000);
        eventHandler = new EventHandler(this);
        canChangeMap = false;
        levelFinished = true;
    };
    public void updateProgress(){
        if(levelFinished) eventHandler.detectEvent();
    }
}
