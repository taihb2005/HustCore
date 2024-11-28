package map;

import ai.Node;
import main.GamePanel;

import java.awt.*;

import static main.GamePanel.camera;

public class TileManager {
    GamePanel gp;

    boolean drawPath = false;

    public TileManager(GamePanel gp){
        this.gp = gp;
    }

    public void render(Graphics2D g2){
        g2.setColor(new Color(255,0,0,70));
        if(drawPath){
            for(int i = 0 ; i < gp.pFinder.pathList.size() ; i++){
                Node node = gp.pFinder.pathList.get(i);
                int screenX = node.col * GameMap.childNodeSize - camera.getX();
                int screenY = node.row * GameMap.childNodeSize - camera.getY();
                g2.fillRect(screenX , screenY , GameMap.childNodeSize , GameMap.childNodeSize);
            }
        };
    }
}
