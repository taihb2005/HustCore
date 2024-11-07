package map;

import entity.Entity;
import entity.object.Obj_Wall;
import entity.player.Player;
import main.GamePanel;
import main.GameState;
import main.KeyHandler;
import main.Sound;
import util.CollisionHandler;

import java.awt.*;
import java.util.*;
import java.util.List;

import static main.GamePanel.camera;


public class GameMap {

    public AssetSetter setter = new AssetSetter(this);
    public CollisionHandler cChecker = new CollisionHandler(this);
    public Sound sound = new Sound();

    private final int mapWidth;
    private final int mapHeight;

    public ArrayList<TileLayer> mapLayer;

    public int inactiveObjIndex = 0;
    public Entity [] inactiveObj; //Danh sách objects không tương tác được ở trên map
    public Entity [] activeObj;   //Danh sách objects tương tác đươc ở trên map
    public Entity [] npc;         //Danh sách target ở trên map
    public Entity [] onAirEnemy;  //Danh sách kẻ địch bay
    public Entity [] onGroundEnemy;//Danh sách kẻ địch trên mặt đất
    public ArrayList<Entity> objList;     //Danh sách tất cả các object trên map bao gồn player , target,...

    private long startTime = System.nanoTime();
    public Player player = new Player(this);
    public GameMap(int mapWidth , int mapHeight)
    {
        mapLayer    = new ArrayList<>();
        inactiveObj = new Entity[100];
        activeObj   = new Entity[100];
        npc         = new Entity[100];
        onAirEnemy  = new Entity[100];
        onGroundEnemy = new Entity[100];
        objList     = new ArrayList<>();

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        setter.setObject();
        setter.setNpc();
        setter.setEnemy();
    }

    public void render(Graphics2D g2)
    {
        if(GamePanel.gameState == GameState.PLAY_STATE || GamePanel.gameState == GameState.DIALOGUE_STATE) {
            objList.add(player);
            for (Entity entity : inactiveObj) {
                if (entity != null)
                    objList.add(entity);
            }

            for (Entity entity : activeObj) {
                if (entity != null)
                    objList.add(entity);
            }

            for(Entity entity : npc)
            {
                if(entity != null)
                {
                    objList.add(entity);
                }
            }

            for(Entity entity : onAirEnemy)
            {
                if(entity != null)
                {
                    objList.add(entity);
                }
            }


            //System.out.println(target.get(0) == null);

            Collections.sort(objList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    int index;
                    if (e1.worldY == e2.worldY) {
                        index = Integer.compare(e1.worldX, e2.worldX);
                    } else
                        index = Integer.compare(e1.worldY, e2.worldY);
                    return index;
                }
            });
        }

        long lasttime = System.nanoTime();
        mapLayer.get(0).render(g2); //Base Layer
        mapLayer.get(1).render(g2);
        for (Entity mapObject : objList)
        {
            if(mapObject != null) mapObject.render(g2);
        }
        mapLayer.get(3).render(g2); //Decor layer

        long currenttime = System.nanoTime();
        long drawTime;

        //DEBUG MENU
        if (KeyHandler.showDebugMenu) //NHẤN F3 ĐỂ HỆN THỊ TỌA ĐỘ CỦA NHÂN VẬT
        {
            drawTime = currenttime - lasttime;
            g2.setColor(Color.white);
            int x = 10;
            int y = 20;
            int lineHeight = 20;
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.drawString("WorldX: " + player.worldX, x, y);
            g2.drawString("WorldY: " + player.worldY, x, y + lineHeight);
            g2.drawString("Row: " + (player.worldY + player.solidArea1.y) / 64, x, y + lineHeight * 2);
            g2.drawString("Col: " + (player.worldX + player.solidArea1.x) / 64, x, y + lineHeight * 3);
            g2.drawString("Draw time: " + drawTime, x, y + lineHeight * 4);
            g2.drawString("Time has passed: " + (System.nanoTime() - startTime) / 1000000000 , x , y + lineHeight * 5);
        }

        //DEBUG HITBOX
        if (KeyHandler.showHitbox)  // NHẤN F4 ĐỂ HIỂN THỊ HITBOX CỦA TẤT CẢ CÁC OBJECT
        {
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(1));
            for (Entity e : objList) {
                if (e != null) {
                    g2.drawRect(e.solidAreaDefaultX1 + e.worldX - camera.getX(), e.solidAreaDefaultY1 + e.worldY - camera.getY(), e.solidArea1.width, e.solidArea1.height);
                    if (e.solidArea2 != null) {
                        g2.drawRect(e.solidAreaDefaultX2 + e.worldX - camera.getX(), e.solidAreaDefaultY2 + e.worldY - camera.getY(), e.solidArea2.width, e.solidArea2.height);
                    }
                }
            }
        }
        objList.clear();
    }

    public void update()
    {
        if(GamePanel.gameState == GameState.PLAY_STATE || GamePanel.gameState == GameState.DIALOGUE_STATE) {

            //UPDATE ENTITY
            for(int i = 0 ; i < activeObj.length ; i++){
                if(activeObj[i] != null){
                    if(activeObj[i].canbeDestroyed) activeObj[i] = null;
                }
            }
            for(Entity entity : inactiveObj) if(entity != null) entity.update();
            for(Entity entity : activeObj) if(entity != null) entity.update();
            for(Entity entity : npc) if(entity != null) entity.update();
            for(Entity entity : onAirEnemy) if(entity != null) entity.update();
            for(Entity entity : onGroundEnemy) if(entity != null) entity.update();
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

                Obj_Wall wall = new Obj_Wall (layer.tileLayerData[i][j], layer.tileSetList.get(index).objects.get(tileID - 1));
                wall.worldX = layer.tileSetList.get(index).getTileWidth() * j;
                wall.worldY = layer.tileSetList.get(index).getTileHeight() * i;
                inactiveObj[inactiveObjIndex] = wall;
                inactiveObjIndex++;
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