package entity.object;

import entity.Entity;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import util.KeyTriple;

import java.awt.*;
import java.util.HashMap;

import static main.GamePanel.camera;

public class Obj_Television extends Entity {
    private static final HashMap<KeyTriple<TelevisionSize, TelevisionState, Integer>, Sprite> televisionSpritePool = new HashMap<>();
    private static final HashMap<KeyTriple<TelevisionSize, TelevisionState, Integer>, Animation> televisionAnimation = new HashMap<>();

    public static void load(){
        televisionSpritePool.put(new KeyTriple<>(TelevisionSize.SMALL, TelevisionState.OFF, 1),
                new Sprite(AssetPool.getImage("television_small_off_id1.png"),64 ,64));
        televisionSpritePool.put(new KeyTriple<>(TelevisionSize.SMALL, TelevisionState.OFF, 2),
                new Sprite(AssetPool.getImage("television_small_off_id2.png"),64 ,64));
        televisionSpritePool.put(new KeyTriple<>(TelevisionSize.SMALL, TelevisionState.OFF, 3),
                new Sprite(AssetPool.getImage("television_small_off_id3.png"),64 ,64));
        televisionSpritePool.put(new KeyTriple<>(TelevisionSize.SMALL, TelevisionState.ON, 1),
                new Sprite(AssetPool.getImage("television_small_on_id1.png"),64 ,64));
        televisionSpritePool.put(new KeyTriple<>(TelevisionSize.BIG, TelevisionState.OFF, 1),
                new Sprite(AssetPool.getImage("television_big_off_id1.png"),128 ,64));
        televisionSpritePool.put(new KeyTriple<>(TelevisionSize.BIG, TelevisionState.ON, 1),
                new Sprite(AssetPool.getImage("television_big_on_id1.png"),128 ,64));

        // SMALL - OFF
        televisionAnimation.put(new KeyTriple<>(TelevisionSize.SMALL, TelevisionState.OFF, 1),
                new Animation(televisionSpritePool.get(new KeyTriple<>(TelevisionSize.SMALL, TelevisionState.OFF, 1))
                        .getSpriteArrayRow(0), 20, true));
        televisionAnimation.put(new KeyTriple<>(TelevisionSize.SMALL, TelevisionState.OFF, 2),
                new Animation(televisionSpritePool.get(new KeyTriple<>(TelevisionSize.SMALL, TelevisionState.OFF, 2))
                        .getSpriteArrayRow(0), 20, true));
        televisionAnimation.put(new KeyTriple<>(TelevisionSize.SMALL, TelevisionState.OFF, 3),
                new Animation(televisionSpritePool.get(new KeyTriple<>(TelevisionSize.SMALL, TelevisionState.OFF, 3))
                        .getSpriteArrayRow(0), 20, true));

// SMALL - ON
        televisionAnimation.put(new KeyTriple<>(TelevisionSize.SMALL, TelevisionState.ON, 1),
                new Animation(televisionSpritePool.get(new KeyTriple<>(TelevisionSize.SMALL, TelevisionState.ON, 1))
                        .getSpriteArrayRow(0), 20, true));

// BIG - OFF
        televisionAnimation.put(new KeyTriple<>(TelevisionSize.BIG, TelevisionState.OFF, 1),
                new Animation(televisionSpritePool.get(new KeyTriple<>(TelevisionSize.BIG, TelevisionState.OFF, 1))
                        .getSpriteArrayRow(0), 20, true));

// BIG - ON
        televisionAnimation.put(new KeyTriple<>(TelevisionSize.BIG, TelevisionState.ON, 1),
                new Animation(televisionSpritePool.get(new KeyTriple<>(TelevisionSize.BIG, TelevisionState.ON, 1))
                        .getSpriteArrayRow(0), 20, true));

    }

    private TelevisionState currentState;
    private TelevisionSize size;
    private Animation currentAnimation;

    public Obj_Television(String state , String size , int initialFrame , int id, int x , int y) throws Exception{
        super(x , y);
        name = "Television";

        this.size = (size.equals("big"))? TelevisionSize.BIG: TelevisionSize.SMALL;
        this.currentState = (state.equals("on"))? TelevisionState.ON: TelevisionState.OFF;
        this.currentAnimation = televisionAnimation.get(new KeyTriple<>(this.size, currentState, id)).clone(initialFrame);

        setDefault();
    }

    public Obj_Television(String state , String size , int initialFrame , int id, String idName, int x , int y) throws Exception{
        super(x , y);
        name = "Television";
        this.idName = idName;

        this.size = (size.equals("big"))? TelevisionSize.BIG: TelevisionSize.SMALL;
        this.currentState = (state.equals("on"))? TelevisionState.ON: TelevisionState.OFF;
        this.currentAnimation = televisionAnimation.get(new KeyTriple<>(this.size, currentState, id)).clone(initialFrame);

        setDefault();
    }

    private void setDefault()
    {
        solidArea1 = new Rectangle(0  , 0 , 0 , 0);
    }

    @Override
    public void update() throws NullPointerException{
        currentAnimation.update();
    }

    @Override
    public void render(Graphics2D g2) throws NullPointerException , ArrayIndexOutOfBoundsException{
        currentAnimation.render(g2, worldX - camera.getX(), worldY - camera.getY());
    }

    public void dispose(){
        currentAnimation = null;
    }

    private enum TelevisionState{
        ON, OFF
    }

    private enum TelevisionSize{
        SMALL, BIG
    }
}
