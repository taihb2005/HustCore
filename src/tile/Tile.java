package tile;

import java.awt.image.BufferedImage;

public class Tile {
    final private BufferedImage tileImg;

    public Tile(BufferedImage img )
    {
        this.tileImg = img;
    }

    public BufferedImage getTileImg() {return tileImg;}



}