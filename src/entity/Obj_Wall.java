package entity;

import graphics.Sprite;

import java.awt.*;

import static main.GamePanel.player1;
import static main.GamePanel.tileSize;

public class Obj_Wall extends Entity{
    private String name;
    private Rectangle solidArea;

    public Obj_Wall()
    {
        super();
        name = "wall";
        try
        {
            entity_sprite = new Sprite("/tile/wall01.png");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2) {
        int screenX = (int) (worldX - player1.worldX + player1.screenX);
        int screenY = (int) (worldY - player1.worldY + player1.screenY);

        g2.drawImage(entity_sprite.getSpriteSheet() , screenX , screenY , tileSize , tileSize , null);
    }
}
