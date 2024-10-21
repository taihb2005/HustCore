package map;

import entity.Entity;
import entity.Obj_Wall;
import entity.Player;
import util.Camera;
import util.CollisionHandler;

import java.awt.*;
import java.util.ArrayList;

import static main.GamePanel.scale;
import static util.UtilityTool.scaleHitbox;

public class GameMap {


    public CollisionHandler cChecker = new CollisionHandler(this);

    private final int mapWidth;
    private final int mapHeight;

    ArrayList<TileSet> tileSetList;
    ArrayList<TileLayer> map;
    public ArrayList<Entity> obj; //Danh sách objects ở trên map

    public Player player = new Player(this);

    public GameMap(int mapWidth , int mapHeight)
    {
        map = new ArrayList<>();
        obj = new ArrayList<>();
        //mapObjects.add(player);

        tileSetList = new ArrayList<>();

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

    }

    public void render(Graphics2D g2, Camera camera)
    {
        map.get(0).render(g2 , camera); //Base Layer
        for (Entity mapObject : obj) mapObject.render(g2, camera);
        //map.get(1).render(g2 , camera); //Wall layer
        map.get(2).render(g2 , camera); //Decor layer

        player.render(g2, camera);
    }

    public void update()
    {
        player.update();
    }

    void parseObject(TileLayer layer){
        for(int i = 0 ; i < layer.numRows ;i++)
        {
            for(int j = 0 ; j < layer.numCols ;j++)
            {
                if(layer.tileLayerData[i][j] == null) continue;

                int tileID = layer.tileLayerDataIndex[i][j];
                int index = layer.getIndexTileSet(layer.tileLayerDataIndex[i][j]);

                Obj_Wall wall = new Obj_Wall (layer.tileLayerData[i][j].getTileImg() , layer.tileSetList.get(index).objects.get(tileID - 1));
                wall.worldX = layer.tileSetList.get(index).getTileWidth() * j;
                wall.worldY = layer.tileSetList.get(index).getTileHeight() * i;
                obj.add(wall);

            }
        }
    }


    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

}