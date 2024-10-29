package map;

import entity.Entity;
import entity.object.Obj_Wall;
import entity.player.Player;
import main.KeyHandler;
import util.CollisionHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;

import static main.GamePanel.camera;


public class GameMap {

    int gabageCollectorCounts = 600;
    int frame = 0;
    int frameCount = 0;

    public AssetSetter setter = new AssetSetter(this);
    public CollisionHandler cChecker = new CollisionHandler(this);
    public Sound sound = new Sound();

    private final int mapWidth;
    private final int mapHeight;

    public ArrayList<TileLayer> mapLayer;

    public ArrayList<Entity> inactiveObj; //Danh sách objects không tương tác được ở trên map
    public ArrayList<Entity> activeObj;   //Danh sách objects tương tác đươc ở trên map
    public ArrayList<Entity> npc;
    public ArrayList<Entity> objList;

    private long startTime = System.nanoTime();
    public Player player = new Player(this);
    public GameMap(int mapWidth , int mapHeight)
    {
        mapLayer    = new ArrayList<>();
        inactiveObj = new ArrayList<>();
        activeObj   = new ArrayList<>();
        objList     = new ArrayList<>();

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        setter.setObject();

    }

    public void render(Graphics2D g2)
    {
        objList.add(player);
        for (int i = 0; i < inactiveObj.size(); i++) {
            if (inactiveObj.get(i) != null)
                objList.add(inactiveObj.get(i));
        }

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

        long lasttime = System.nanoTime();
        mapLayer.get(0).render(g2); //Base Layer
        mapLayer.get(1).render(g2);
        for (Entity mapObject : objList) mapObject.render(g2);
        mapLayer.get(3).render(g2); //Decor layer

        long currenttime = System.nanoTime();
        long drawTime = currenttime - lasttime;

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


    }

    public void update()
    {
//        d
        for(int i = 0 ; i < objList.size() ; i++){
            if(objList.get(i) != null)
                objList.get(i).update();
        }

        objList.clear();

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