package environment;

import map.GameMap;

import java.awt.*;

public class EnvironmentManager {

    GameMap mp;
    public Lighting lighting;

    public EnvironmentManager(GameMap mp)
    {
        this.mp = mp;
    }
    public void setup()
    {
        lighting = new Lighting(mp);
    }
    public void draw(Graphics2D g2)
    {
        lighting.draw(g2);
    }
}