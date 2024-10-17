package tile;

import java.awt.image.BufferedImage;

public class Tile {
    final private BufferedImage tileImg;
    private boolean isAnimated;

    public Tile(BufferedImage img , boolean isAnimated)
    {
        this.tileImg = img;
        this.isAnimated = isAnimated;
    }

    public BufferedImage getTileImg() {return tileImg;}

    public boolean isAnimated() {return isAnimated;}

    public void setAnimatedState(boolean isAnimated){this.isAnimated = isAnimated;};

}
