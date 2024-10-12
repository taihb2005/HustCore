package tile;

import java.awt.*;
import java.util.ArrayList;

public class TileLayer extends  Layer{

    private int [][] tileLayerData;
    private ArrayList<TileSet> tileSetList;

    private boolean isVisible;

    private int numCols;
    private int numRows;

    public TileLayer(int numrows , int numcols  , int [][] data , boolean isVisible , ArrayList<TileSet> tileSetList)
    {
        super();
        this.numRows = numrows;
        this.numCols = numcols;
        tileLayerData = data;
        this.isVisible = isVisible;
        this.tileSetList = tileSetList;
    }


    @Override
    public void render(Graphics2D g2) {
        for(int i = 0 ; i < numRows ; i++)
        {
            for(int j = 0 ; j < numCols ; j++)
            {
                if(tileLayerData[i][j] == 0) continue;

                int data = tileLayerData[i][j];
                int k = getIndexTileSet(data);
                TileSet ts = tileSetList.get(k);

                data = data + ts.getNumTiles() - ts.getLastID();

                int tileRow = data / ts.getNumCols();
                int tileCol = data - tileRow * ts.getNumCols() - 1;

                if(data % ts.getNumCols() == 0) {
                    tileRow--;
                    tileCol = ts.getNumCols() - 1;
                }


                g2.drawImage(ts.getTileSetSprite().getSubimage(tileCol * 16, tileRow * 16 ,
                             ts.getTileWidth()  , ts.getTileHeight()) ,
                          j * ts.getTileHeight() , i * ts.getTileWidth()  , null);

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

    public void setVisible(boolean isVisible){this.isVisible = isVisible;};
    public boolean getVisible(){return isVisible;};
}
