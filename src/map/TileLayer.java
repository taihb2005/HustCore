package map;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.GamePanel.camera;

public class TileLayer {

    GameMap mp;

    BufferedImage[][] tileLayerData;
    int[][] tileLayerDataIndex;

    final ArrayList<TileSet> tileSetList;

    final int numCols;
    final int numRows;

    final int tileWidth;
    final int tileHeight;

    public TileLayer(int numrows, int numcols, int[][] data, ArrayList<TileSet> tileSetList , GameMap mp) {
        super();
        this.mp = mp;
        this.numRows = numrows;
        this.numCols = numcols;
        this.tileSetList = tileSetList;
        this.tileWidth = tileSetList.get(0).getTileWidth();
        this.tileHeight = tileSetList.get(0).getTileHeight();
        this.tileLayerDataIndex = data;
        parseLayerData(data);
    }


    public void render(Graphics2D g2) {
        //Gioi han tile can ve
        int startCols = Math.max((mp.player.worldX - mp.player.screenX) / tileWidth - 10, 0);
        int endCols = Math.min((mp.player.worldX + mp.player.screenX) / tileWidth + 10, numCols);

        int startRows = Math.max((mp.player.worldY - mp.player.screenY) / tileHeight - 8, 0);
        int endRows = Math.min((mp.player.worldY + mp.player.screenY) / tileHeight + 8, numRows);
        for(int i = startRows ; i < endRows ; i++)
        {
            for(int j = startCols; j < endCols ; j++)
            {
                if(tileLayerData[i][j] != null){
                    BufferedImage tile = tileLayerData[i][j];

                    int worldX = j * tileWidth;
                    int worldY = i * tileHeight;

                    g2.drawImage(tile, worldX - camera.getX() , worldY - camera.getY(), tileWidth, tileHeight, null);
                }

            }
        }

    }

    public void update() {

    }


    int getIndexTileSet(int data) {

        int index = 0;

        for (int i = 0; i < tileSetList.size(); i++) {
            TileSet tmp_tileSet = tileSetList.get(i);
            if (data >= tmp_tileSet.getFirstID() && data <= tmp_tileSet.getLastID()) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void parseLayerData(int[][] arrayOfIndexedTile) {
        this.tileLayerData = new BufferedImage[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (arrayOfIndexedTile[i][j] == 0) continue;

                int data = arrayOfIndexedTile[i][j];
                int k = getIndexTileSet(data);
                TileSet ts = tileSetList.get(k);

                data = data + ts.getNumTiles() - ts.getLastID();

                int tileRow = data / ts.getNumCols();
                int tileCol = data - tileRow * ts.getNumCols() - 1;

                if (data % ts.getNumCols() == 0) {
                    tileRow--;
                    tileCol = ts.getNumCols() - 1;
                }


                tileLayerData[i][j] = ts.getTileSetSprite().getSubimage(tileCol * ts.getTileWidth(), tileRow * ts.getTileHeight(),
                        ts.getTileWidth(), ts.getTileHeight());

            }
        }
    }

    public void dispose(){
        for(BufferedImage[] tile : tileLayerData){
            if(tile == null) continue;
            for(BufferedImage img : tile){
                if(img == null) continue;
                img.flush();
            }
        }
    }

    public int getNumCols()
    {
        return numCols;
    }

    public int getNumRows()
    {
        return numRows;
    }
}

