package graphics.environment;

import map.GameMap;

import java.awt.*;

public class EnvironmentManager {

    GameMap mp;
    public Lighting lighting;

    public EnvironmentManager(GameMap mp)
    {
        this.mp = mp;
    }
    public void setRadius(int radius){
        lighting.setLightRadius(radius);
    }
    public void setup()
    {
        lighting = new Lighting(mp);
    }
    public void render(Graphics2D g2) {
        lighting.draw(g2);
    }
    public void update(){
        lighting.update();
    }
}