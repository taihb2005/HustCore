package map;

import entity.Entity;
import entity.mob.Mon_Boss;
import entity.mob.Monster;
import entity.object.Obj_Wall;
import entity.player.AttackEnemy;
import entity.player.Player;
import entity.projectile.Projectile;

import graphics.environment.EnvironmentManager;

import level.event.EventRectangle;
import main.KeyHandler;
import util.CollisionHandler;

import java.awt.*;
import java.util.*;

import static main.GamePanel.*;


public class GameMap {
    private static final int HORIZONTAL_RENDER_BUFFER = 3 * 64;
    private static final int VERTICAL_RENDER_BUFFER = 2 * 64;

    private static final int HORIZONTAL_UPDATE_BUFFER = 10 * 64;
    private static final int VERTICAL_UPDATE_BUFFER = 10 * 64;

    public Player player;
    public Mon_Boss boss = null;

    private final EnvironmentManager environmentManager;
    public CollisionHandler cChecker = new CollisionHandler(this);
    public AttackEnemy playerAttack;

    public static int childNodeSize = 32;

    private final int mapWidth;
    private final int mapHeight;
    public int maxWorldCol;
    public int maxWorldRow;

    public ArrayList<TileLayer> mapLayer;

    public Entity [] inactiveObj; //Danh sách objects không tương tác được ở trên map
    public Entity [] activeObj;   //Danh sách objects tương tác đươc ở trên map
    public Entity [] npc;//Danh sách currentSpeaker ở trên map
    public Monster [] enemy;
    public Projectile[] projectiles;
    public ArrayList<Entity> objList;     //Danh sách tất cả các object trên map bao gồm player , currentSpeaker,...

    //MAP STAT
    private int bestLightingRadius = 2000;

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

        player = new Player(this);
        playerAttack = new AttackEnemy(this);

        environmentManager = new EnvironmentManager(this);
        environmentManager.setup();
        environmentManager.setRadius(getBestLightingRadius());
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
            index = Float.compare(e1.position.y, e2.position.y);
            return index;
        });

        mapLayer.get(0).render(g2);
        mapLayer.get(0).render(g2); //Base Layer
        mapLayer.get(1).render(g2);
        for (Entity mapObject : new ArrayList<>(objList))
        {
            if(mapObject != null && !mapObject.isCollected && isInView(mapObject, HORIZONTAL_RENDER_BUFFER, VERTICAL_RENDER_BUFFER)) mapObject.render(g2);
        }
        for(Entity projectile : projectiles)
        {
            if(projectile != null && isInView(projectile, HORIZONTAL_RENDER_BUFFER, VERTICAL_RENDER_BUFFER)) projectile.render(g2);
        }
        objList.clear();

        environmentManager.render(g2);

        //DEBUG MENU
        if (KeyHandler.showDebugMenu) //NHẤN F3 ĐỂ HIỂN THỊ TỌA ĐỘ CỦA NHÂN VẬT
        {
            g2.setColor(Color.white);
            int x = 10;
            int y = 20;
            int lineHeight = 20;
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.drawString("WorldX: " + player.position.x, x, y);
            g2.drawString("WorldY: " + player.position.y, x, y + lineHeight);
            g2.drawString("Row: " + (player.position.y + player.solidArea1.y) / 64, x, y + lineHeight * 2);
            g2.drawString("Col: " + (player.position.x + player.solidArea1.x) / 64, x, y + lineHeight * 3);
        }

        //DEBUG HITBOX
        if (KeyHandler.showHitbox)  // NHẤN F4 ĐỂ HIỂN THỊ HITBOX CỦA TẤT CẢ CÁC OBJECT
        {
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(1));
            EventRectangle x = new EventRectangle(896 , 1408 , 128, 64 , true);
            g2.drawRect(x.x - camera.getX(), x.y - camera.getY(), x.width, x.height);
            for (Entity e : objList) {
                if (e != null) {
                    g2.drawRect(e.solidAreaDefaultX1 + (int)e.position.x - camera.getX(), e.solidAreaDefaultY1 + (int)e.position.y - camera.getY(), e.solidArea1.width, e.solidArea1.height);
                    if (e.solidArea2 != null) {
                        g2.drawRect(e.solidAreaDefaultX2 + (int)e.position.x - camera.getX(), e.solidAreaDefaultY2 + (int)e.position.y - camera.getY(), e.solidArea2.width, e.solidArea2.height);
                    }
                }
            }
            for(Entity e : projectiles){
                if(e != null){
                    g2.drawRect(e.solidAreaDefaultX1 + (int)e.position.x - camera.getX(), e.solidAreaDefaultY1 + (int)e.position.y - camera.getY(), e.solidArea1.width, e.solidArea1.height);
                }
            }
            g2.setColor(Color.RED);
            for(Entity e : objList){
                if(e != null){
                    if(e.hitbox != null) g2.drawRect(e.hitbox.x + (int)e.position.x - camera.getX() , e.hitbox.y + (int)e.position.y - camera.getY() , e.hitbox.width , e.hitbox.height);
                }
            }
            for(Entity e : projectiles){
                if(e != null){
                    if(e.hitbox != null) g2.drawRect(e.hitbox.x + (int)e.position.x - camera.getX() , e.hitbox.y + (int)e.position.y - camera.getY() , e.hitbox.width , e.hitbox.height);
                }
            }
            g2.setColor(Color.GREEN);
            for(Entity e : enemy){
                if(e != null && e.interactionDetectionArea != null){
                    g2.drawRect(e.interactionDetectionArea.x + (int)e.position.x - camera.getX() , e.interactionDetectionArea.y + (int)e.position.y - camera.getY() , e.interactionDetectionArea.width , e.interactionDetectionArea.height);
                }
            }
        }
    }

    public void update()
    {
        //UPDATE ENTITY
        for(int i = 0 ; i < activeObj.length ; i++){
            if(activeObj[i] != null && isInView(activeObj[i], HORIZONTAL_UPDATE_BUFFER, VERTICAL_UPDATE_BUFFER)){
                if(activeObj[i].canbeDestroyed){
                    activeObj[i] = null;
                }
            }
        }
        for(int i = 0 ; i < enemy.length ; i++){
            if(enemy[i] != null && isInView(enemy[i], HORIZONTAL_UPDATE_BUFFER, VERTICAL_UPDATE_BUFFER)){
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
        for(Entity entity : inactiveObj) if(entity != null && isInView(entity, HORIZONTAL_UPDATE_BUFFER, VERTICAL_UPDATE_BUFFER)) entity.update();
        for(Entity entity : activeObj) if(entity != null && isInView(entity, HORIZONTAL_UPDATE_BUFFER, VERTICAL_UPDATE_BUFFER)) entity.update();
        for(Entity entity : npc) if(entity != null && isInView(entity, HORIZONTAL_UPDATE_BUFFER, VERTICAL_UPDATE_BUFFER)) entity.update();
        for(Entity entity : enemy) if(entity != null && isInView(entity, HORIZONTAL_UPDATE_BUFFER, VERTICAL_UPDATE_BUFFER)) entity.update();
        for(Entity entity : projectiles) if(entity != null) entity.update();
        player.update();

        environmentManager.update();
    }

    public static boolean isInView(Entity entity, int horizontalBuffer, int verticalBuffer) {
        int left = camera.getX() - horizontalBuffer;
        int right = camera.getX() + windowWidth + tileSize + horizontalBuffer;
        int top = camera.getY() - tileSize - verticalBuffer;
        int bottom = camera.getY() + windowHeight + tileSize + verticalBuffer;

        return (entity.position.x + entity.width > left &&
                entity.position.x < right &&
                entity.position.y + entity.height > top &&
                entity.position.y< bottom);
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
                    wall.position.x = layer.tileSetList.get(index).getTileWidth() * j;
                    wall.position.y = layer.tileSetList.get(index).getTileHeight() * i;
                    addObject(wall, inactiveObj);
                }
//                inactiveObj[inactiveObjIndex] = wall;
//                inactiveObjIndex++;
            }
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

    public EnvironmentManager getEnvironmentManager(){
        return environmentManager;
    }

    private <T extends Entity> void disposeEntityArray(T[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                array[i].dispose();
                array[i] = null;
            }
        }
    }

    public void dispose() {
        disposeEntityArray(activeObj);
        disposeEntityArray(enemy);
        disposeEntityArray(projectiles);

        Arrays.fill(inactiveObj, null);
        Arrays.fill(activeObj, null);
        Arrays.fill(npc, null);
        Arrays.fill(enemy, null);
        Arrays.fill(projectiles, null);

        objList.clear();

        for (TileLayer layer : mapLayer) {
            if (layer != null) {
                layer.dispose();
            }
        }
    }


}