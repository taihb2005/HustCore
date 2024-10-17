package tile;

import util.Camera;

import java.awt.*;

public abstract class Layer {

    public Layer(){}

    public abstract void render(Graphics2D g2, Camera camera);
    public abstract void update();

}
