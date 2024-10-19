package tile;


import util.Camera;

import java.awt.*;
import java.util.ArrayList;

import static main.GamePanel.*;
import static main.GamePanel.tileSize;

public class TileLayer extends  Layer{

    private Tile[][] tileLayerData;
    final private ArrayList<TileSet> tileSetList;


    final private int numCols;
    final private int numRows;

    final private int tileWidth;
    final private int tileHeight;
    private Camera camera;

    public TileLayer(int numrows , int numcols  , int [][] data , boolean isVisible , ArrayList<TileSet> tileSetList)
    {
        super();
        this.numRows = numrows;
        this.numCols = numcols;
        this.tileSetList = tileSetList;
        this.tileWidth = tileSetList.get(0).getTileWidth() * scale;
        this.tileHeight = tileSetList.get(0).getTileHeight() * scale;

        parseLayerData(data);
    }


    @Override
    public void render(Graphics2D g2, Camera camera) {
    //Gioi han tile can ve
        int startCols = Math.max((int)(player1.worldX - player1.screenX) / tileWidth - 10, 0);
        int endCols = Math.min((int)(player1.worldX + player1.screenX) / tileWidth + 10, numCols);

        int startRows = Math.max((int)(player1.worldY - player1.screenY) / tileHeight - 8, 0);
        int endRows = Math.min((int)(player1.worldY + player1.screenY) / tileHeight + 8, numRows);
        for(int i = startRows ; i < endRows ; i++)
        {
            for(int j = startCols ; j < endCols ; j++)
            {
                if(tileLayerData[i][j] == null) continue;

                Tile tile = tileLayerData[i][j];

                int worldX = j * tileWidth;
                int worldY = i * tileHeight;

                int screenX = (int) worldX- camera.distanceX(player1.worldX);
                int screenY = (int) worldY - camera.distanceY(player1.worldY);


                g2.drawImage(tile.getTileImg() , screenX  , screenY  , tileWidth , tileHeight  , null );
            }
        }

    }

    @Override
    public void update() {

    }


    private int getIndexTileSet(int data)
    {

        int index = 0;

        for(int i = 0 ; i < tileSetList.size() ; i++)
        {
            TileSet tmp_tileSet = tileSetList.get(i);
            if(data >= tmp_tileSet.getFirstID() && data <= tmp_tileSet.getLastID())
            {
                index = i;
                break;
            }
        }
        return index;
    }

    private void parseLayerData(int [][] arrayOfIndexedTile)
    {
        this.tileLayerData = new Tile[numRows][numCols];

        for(int i = 0 ; i < numRows ; i++)
        {
            for(int j = 0 ; j < numCols ; j++)
            {
                if(arrayOfIndexedTile[i][j] == 0) continue;

                int data = arrayOfIndexedTile[i][j];
                int k = getIndexTileSet(data);
                TileSet ts = tileSetList.get(k);

                data = data + ts.getNumTiles() - ts.getLastID();

                int tileRow = data / ts.getNumCols();
                int tileCol = data - tileRow * ts.getNumCols() - 1;

                if(data % ts.getNumCols() == 0) {
                    tileRow--;
                    tileCol = ts.getNumCols() - 1;
                }


                tileLayerData[i][j] =  new Tile(ts.getTileSetSprite().getSubimage(tileCol * ts.getTileWidth(), tileRow * ts.getTileHeight() ,
                        ts.getTileWidth()  , ts.getTileHeight()) , false);

            }
        }
    }


