package entity.object;

import entity.Entity;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import util.KeyPair;

import java.awt.*;
import java.util.HashMap;

import static main.GamePanel.camera;

public class Obj_Tank extends Entity {
    private static final HashMap<KeyPair<TankState, Integer>, Sprite> tankSpritePool = new HashMap<>();
    private static final HashMap<KeyPair<TankState, Integer>, Animation> tankAnimations = new HashMap<>();

    public static void load(){
        for(TankState state: TankState.values()) {
            for (int id = 1; id <= 2; id++) {
                KeyPair<TankState, Integer> key = new KeyPair<>(state, id);
                tankSpritePool.put(key,
                        new Sprite(AssetPool.getImage("tank_" + state.name().toLowerCase() + "_id" + id + ".png"), 64, 128));
                tankAnimations.put(key,
                        new Animation(tankSpritePool.get(key).getSpriteArrayRow(0), 9, true));
            }
        }
    }
    private TankState currentState;
    private Animation currentAnimation;
    public Obj_Tank(String state , int id, int x , int y) throws Exception
    {
        super(x , y);
        name = "Tank";
        super.width = 64;
        super.height = 128;

        if(state.equals("empty") && id != 1){
            throw new Exception("Cái Tank đã empty rồi thì để id = 1 nhé anh bạn!");
        }

        currentState = (state.equals("empty")) ? TankState.EMPTY : TankState.FILLED;
        currentAnimation = tankAnimations.get(new KeyPair<>(currentState, id)).clone();

        setDefault();
    }

    private void setDefault()
    {
        solidArea1 = new Rectangle(12 , 58 , 42 , 36);
        solidArea2 = new Rectangle(17 , 95 , 32 , 12 );
        super.setDefaultSolidArea();
    }

    @Override
    public void update(){
        currentAnimation.update();
    }

    @Override
    public void render(Graphics2D g2) throws ArrayIndexOutOfBoundsException , NullPointerException {
        currentAnimation.render(g2, worldX - camera.getX(), worldY - camera.getY());
    }

    private enum TankState{
        EMPTY, FILLED
    }

}
