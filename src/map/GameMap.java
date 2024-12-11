package map;

import entity.Entity;
import entity.mob.Monster;
import entity.object.Obj_Wall;
import entity.player.AttackEnemy;
import entity.player.Player;
import entity.projectile.Projectile;
import level.EventRectangle;
import level.Level;
import main.GamePanel;
import main.GameState;
import main.KeyHandler;
import util.CollisionHandler;

import java.awt.*;
import java.util.*;

import static main.GamePanel.*;


public class GameMap {

    public GamePanel gp;
    public Player player = new Player(this);
    public CollisionHandler cChecker = new CollisionHandler(this);
    public AttackEnemy playerAttack = new AttackEnemy(this);

    public static int childNodeSize = 32;

    private final int mapWidth;
    private final int mapHeight;
    public int maxWorldCol;
    public int maxWorldRow;

    public ArrayList<TileLayer> mapLayer;

    public Entity [] inactiveObj; //Danh sách objects không tương tác được ở trên map
    public Entity [] activeObj;   //Danh sách objects tương tác đươc ở trên map
    public Entity [] npc;//Danh sách target ở trên map
    public Monster [] enemy;
    public Projectile[] projectiles;
    public ArrayList<Entity> objList;     //Danh sách tất cả các object trên map bao gồm player , target,...

    //MAP STAT
    private int bestLightingRadius = 2000;

    private long startTime = System.nanoTime();
    public GameMap(int mapWidth , int mapHeight)
    {
        mapLayer    = new ArrayList<>();
        inactiveObj = new Entity[500];
        activeObj   = new Entity[100];
        npc         = new Entity[100];
        enemy       = new Monster[50];
        projectiles = new Projectile[100];
        objList     = new ArrayList<>();
        dispose();

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.maxWorldCol = (mapWidth / childNodeSize) + 1 ;
        this.maxWorldRow = (mapHeight/ childNodeSize) + 1;
    }

    public void render(Graphics2D g2)
    {
        objList.add(player);
        for (Entity entity : inactiveObj) if(entity != null) objList.add(entity);
        for (Entity entity : activeObj) if(entity != null) objList.add(entity);
        for (Entity entity : npc) if(entity != null) objList.add(entity);
        for (Entity entity : enemy) if(entity != null) objList.add(entity);

        Collections.sort(objList, (e1, e2) -> {
            int index;
            index = Integer.compare(e1.worldY, e2.worldY);
            return index;
        });

        mapLayer.get(0).render(g2);
        mapLayer.get(0).render(g2); //Base Layer
        mapLayer.get(1).render(g2);
        for (Entity mapObject : objList)
        {
            if(mapObject != null && !mapObject.isCollected) mapObject.render(g2);
        }
        for(Entity projectile : projectiles)
        {
            if(projectile != null) projectile.render(g2);
        }
        objList.clear();
    }

    public void update()
    {
        if(GamePanel.gameState == GameState.PLAY_STATE || GamePanel.gameState == GameState.DIALOGUE_STATE || GamePanel.gameState == GameState.PASSWORD_STATE) {

            //UPDATE ENTITY
            for(int i = 0 ; i < activeObj.length ; i++){
                if(activeObj[i] != null){
                    if(activeObj[i].canbeDestroyed){
                        activeObj[i] = null;
                    }
                }
            }
            for(int i = 0 ; i < enemy.length ; i++){
                if(enemy[i] != null){
                    if(enemy[i].canbeDestroyed) {
                        enemy[i] = null;
                    }
                }
            }
            for(int i = 0 ; i < projectiles.length ; i++){
                if(projectiles[i] != null){
                    if(!projectiles[i].active){
                        projectiles[i] = null;
                    }
                }
            }
            //UPDATE ITEM
            for(Entity entity : inactiveObj) if(entity != null) entity.update();
            for(Entity entity : activeObj) if(entity != null) entity.update();
            for(Entity entity : npc) if(entity != null) entity.update();
            for(Entity entity : enemy) if(entity != null) entity.update();
            for(Entity entity : projectiles) if(entity != null) entity.update();
            player.update();
        }
    }

    public void parseWallObject(TileLayer layer){
        for(int i = 0 ; i < layer.numRows ;i++)
        {
            for(int j = 0 ; j < layer.numCols ;j++)
            {
                if(layer.tileLayerData[i][j] == null) continue;

                int tileID = layer.tileLayerDataIndex[i][j];
                int index = layer.getIndexTileSet(layer.tileLayerDataIndex[i][j]);

                if(layer.tileSetList.get(index).objects.get(tileID - 1) != null) {


                    Obj_Wall wall = new Obj_Wall(layer.tileLayerData[i][j], layer.tileSetList.get(index).objects.get(tileID - 1));
                    wall.worldX = layer.tileSetList.get(index).getTileWidth() * j;
                    wall.worldY = layer.tileSetList.get(index).getTileHeight() * i;
                    addObject(wall, inactiveObj);
                }
//                inactiveObj[inactiveObjIndex] = wall;
//                inactiveObjIndex++;
            }
        }
    }

    public void dispose(){
        for(Entity e : activeObj) if(e != null) e.dispose();
        for(Entity e : enemy) if(e != null) e.dispose();
        for(Entity e : projectiles) if(e != null) e.dispose();
        Arrays.fill(inactiveObj, null);
        Arrays.fill(activeObj, null);
        Arrays.fill(npc, null);
        Arrays.fill(enemy, null);
        Arrays.fill(projectiles, null);
        objList.clear();

        for(TileLayer layer : mapLayer){
            layer.dispose();
        }
    }

    public void addObject(Entity entity , Entity[] list){
        for(int i = 0 ; i < list.length ; i++){
            if(list[i] == null){
                list[i] = entity;
                break;
            }
        }
    }

    public void setBestLightingRadius(int r){bestLightingRadius = r;}
    public int getBestLightingRadius(){return bestLightingRadius;}

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

}