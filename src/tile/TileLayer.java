package tile;

import util.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.GamePanel.*;
import static main.GamePanel.tileSize;

public class TileLayer extends  Layer{

    private Tile[][] tileLayerData;
    final private ArrayList<TileSet> tileSetList;

    private boolean isVisible;

    final private int numCols;
    final private int numRows;

    final private int tileWidth;
    final private int tileHeight;

    public TileLayer(int numrows , int numcols  , int [][] data , boolean isVisible , ArrayList<TileSet> tileSetList)
    {
        super();
        this.numRows = numrows;
        this.numCols = numcols;
        this.isVisible = isVisible;
        this.tileSetList = tileSetList;
        this.tileWidth = tileSetList.get(0).getTileWidth() * scale;
        this.tileHeight = tileSetList.get(0).getTileHeight() * scale;

        parseLayerData(data);
    }


    @Override
    public void render(Graphics2D g2) {

        for(int i = 0 ; i < numRows ; i++)
        {
            for(int j = 0 ; j < numCols ; j++)
            {
                if(tileLayerData[i][j] == null) continue;

                Tile tile = tileLayerData[i][j];
                int worldX = j * tileWidth;
                int worldY = i * tileHeight;

                // toa do cuoi cung cua tile tren man hinh, truong hop dau tien la khi tile nam gan goc toa do
                int screenX = (int) worldX;
                int screenY = (int) worldY;

                // truong hop thu hai, khi tile nam o khu vuc giua thi screenX va screenY thay doi
                // them if vao thay vi screenX = worldX - player1.worldX + player1.drawX de tranh map bi giat
                if (player1.worldX >= player1.screenX && player1.worldX <= currentMap.getMapWidth() - player1.screenX-2*tileSize) screenX -= player1.worldX - player1.drawX;
                if (player1.worldY >= player1.screenY && player1.worldY <= currentMap.getMapHeight() - player1.screenY-2*tileSize) screenY -= player1.worldY - player1.drawY;

                // truong hop thu ba, khi tile nam o gan goc doi dien goc toa do
                if (player1.worldX > currentMap.getMapWidth() - player1.screenX-2*tileSize) screenX -= (currentMap.getMapWidth() - 2*tileSize - 2* player1.screenX);
                if (player1.worldY > currentMap.getMapHeight() - player1.screenY-2*tileSize) screenY -= (currentMap.getMapHeight() -2*tileSize - 2* player1.screenY);

                // giam bot so tile can ve trong 1 frame
                if((worldX + 10*tileWidth > player1.worldX -player1.screenX)&&
                        (worldX - 10*tileWidth < player1.worldX +player1.screenX)
                        &&(worldY + 8*tileHeight > player1.worldY -player1.screenY) &&(worldY - 8*tileHeight < player1.worldY +player1.screenY))
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

    public void setVisible(boolean isVisible){this.isVisible = isVisible;};
    public boolean getVisible(){return isVisible;};
}
