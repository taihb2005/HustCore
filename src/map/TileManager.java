package map;

import ai.Node;
import main.GamePanel;

import java.awt.*;

import static main.GamePanel.camera;
import static main.GamePanel.pFinder2;

public class TileManager {
    GamePanel gp;

    boolean drawPath = true;

    public TileManager(GamePanel gp){
        this.gp = gp;
    }

    public void render(Graphics2D g2){
        g2.setColor(new Color(255,0,0,70));
        if(drawPath && GamePanel.pFinder2 != null){
            for(int i = 0; i < GamePanel.pFinder2.pathList.size() ; i++){
                Node node = GamePanel.pFinder2.pathList.get(i);
                int screenX = node.col * GameMap.childNodeSize - camera.getX();
                int screenY = node.row * GameMap.childNodeSize - camera.getY();
                g2.fillRect(screenX , screenY , GameMap.childNodeSize , GameMap.childNodeSize);
            }
        };
    }
}
