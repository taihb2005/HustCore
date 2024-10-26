package map;

import entity.Entity;
import entity.Obj_Wall;
import entity.Player;
import main.GamePanel;
import util.CollisionHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class GameMap {

    public CollisionHandler cChecker = new CollisionHandler(this);
    public Sound sound = new Sound();

    private final int mapWidth;
    private final int mapHeight;

    public ArrayList<TileLayer> mapLayer;

    public ArrayList<Entity> inactiveObj; //Danh sách objects không tương tác được ở trên map
    public ArrayList<Entity> activeObj;   //Danh sách objects tương tác đươc ở trên map
    public ArrayList<Entity> npc;
    public ArrayList<Entity> objList;

    public Player player = new Player(this);

    public GameMap(int mapWidth , int mapHeight)
    {
        mapLayer    = new ArrayList<>();
        inactiveObj = new ArrayList<>();
        activeObj   = new ArrayList<>();
        objList     = new ArrayList<>();

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

    }

    public void render(Graphics2D g2)
    {
        objList.add(player);
        for(int i = 0 ; i < inactiveObj.size() ; i++)
        {
            if(inactiveObj.get(i) != null)
                objList.add(inactiveObj.get(i));
        }

        Collections.sort(objList, new Comparator<Entity>() {
            @Override
            public int compare(Entity e1, Entity e2) {
                int index;
                if(e1.worldY == e2.worldY)
                {
                    index = Integer.compare(e1.worldX , e2.worldX);
                } else
                    index = Integer.compare(e1.worldY , e2.worldY);
                return index;
            }
        });

        mapLayer.get(0).render(g2); //Base Layer
        mapLayer.get(1).render(g2);
        for (Entity mapObject : objList) mapObject.render(g2);
        mapLayer.get(3).render(g2); //Decor layer

        for(int i = 0 ; i < objList.size() ; i++)
        {
            objList.remove(i);
        }
    }

    public void update()
    {
        player.update();
    }

    public void parseWallObject(TileLayer layer){
        for(int i = 0 ; i < layer.numRows ;i++)
        {
            for(int j = 0 ; j < layer.numCols ;j++)
            {
                if(layer.tileLayerData[i][j] == null) continue;

                int tileID = layer.tileLayerDataIndex[i][j];
                int index = layer.getIndexTileSet(layer.tileLayerDataIndex[i][j]);

                Obj_Wall wall = new Obj_Wall (layer.tileLayerData[i][j], layer.tileSetList.get(index).objects.get(tileID - 1));
                wall.worldX = layer.tileSetList.get(index).getTileWidth() * j;
                wall.worldY = layer.tileSetList.get(index).getTileHeight() * i;
                inactiveObj.add(wall);

            }
        }
    }


    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void playMusic(int index)
    {
        sound.setFile(index);
        sound.play();
        sound.loop();
    }
    public void stopMusic(int index)
    {
        sound.stop();
    }
    public void playSoundEffect(int index)
    {
        sound.setFile(index);
        sound.play();
    }

}