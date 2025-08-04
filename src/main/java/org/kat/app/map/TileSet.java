package org.kat.app.map;

import org.kat.app.graphics.AssetPool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Objects;

public class TileSet {

    final private int firstID;
    final private int lastID;
    final private int tileWidth;
    final private int tileHeight;
    final private int numRows;
    final private int numCols;
    final private int numTiles;

    private StringBuilder imgPath;

    private BufferedImage tileSetSprite;

    HashMap<Integer , Rectangle[]> objects = new HashMap<>();

    public TileSet(int firstID , int lastID , int tileWidth ,
                   int tileHeight , int numRows ,
                   int numCols , String imgPath)
    {
        this.firstID = firstID;
        this.lastID = lastID;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight ;
        this.numRows = numRows;
        this.numCols = numCols;
        this.numTiles = lastID - firstID + 1;
        this.imgPath = new StringBuilder(imgPath);

        loadTileSheet(imgPath);
    };

    public TileSet(int firstID , int lastID , int tileWidth ,
                   int tileHeight , int numRows ,
                   int numCols , HashMap<Integer , Rectangle[]> objects ,String imgPath)
    {
        this.firstID = firstID;
        this.lastID = lastID;
        this.tileWidth = tileWidth ;
        this.tileHeight = tileHeight;
        this.numRows = numRows;
        this.numCols = numCols;
        this.numTiles = lastID - firstID + 1;
        this.objects = objects;
        this.imgPath = new StringBuilder(imgPath);

        loadTileSheet(imgPath);
    };

    private void loadTileSheet(String imageName)
    {
        tileSetSprite = null;
        if(AssetPool.assetPool.containsKey(imageName)){
            tileSetSprite = AssetPool.getImage(imageName);
        } else {
            try {
                tileSetSprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/textures/tile/" + imageName)));
                AssetPool.assetPool.put(imageName, tileSetSprite);
            } catch (Exception e) {
                System.out.println("Cannot find imageName: " + imageName);
                e.printStackTrace();
            }
        }
    }

    public void dispose(){
        objects.clear();
        tileSetSprite = null;
        imgPath = null;
    }



    public int getFirstID(){return firstID;};
    public int getLastID(){return  lastID;};
    public int getNumRows(){return numRows;};
    public int getNumCols(){return numCols;};
    public int getNumTiles(){return numTiles;};

    public String getImgPath(){return imgPath.toString();};
    public BufferedImage getTileSetSprite(){return tileSetSprite;};

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }
}