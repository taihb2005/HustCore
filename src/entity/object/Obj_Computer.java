package entity.object;

import entity.Entity;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import util.KeyPair;

import java.awt.*;
import java.util.HashMap;

import static main.GamePanel.camera;

public class Obj_Computer extends Entity {
    private static final HashMap<KeyPair<ComputerState, ComputerDirection>, Sprite> computerSpritePool = new HashMap<>();
    private static final HashMap<KeyPair<ComputerState, ComputerDirection>, Animation> computerAnimations = new HashMap<>();
    public static void load(){
        for(ComputerState state: ComputerState.values()){
            for(ComputerDirection direction: ComputerDirection.values()){
                KeyPair<ComputerState, ComputerDirection> key = new KeyPair<>(state, direction);
                computerSpritePool.put(key,
                        new Sprite(AssetPool.getImage("computer_" + direction.name().toLowerCase() + "_" + state.name().toLowerCase() + ".png")));
                computerAnimations.put(key,
                        new Animation(computerSpritePool.get(key).getSpriteArrayRow(0),18, true));
            }
        }
    }

    private final Animation currentAnimation;
    private final ComputerState currentState;
    private final ComputerDirection currentDirection;

    public Obj_Computer(String state , String direction , int x , int y){
        super(x , y);
        name = "Computer";
        width = 64;
        height = 64;

        currentState = (state.equals("off")) ? ComputerState.OFF : ComputerState.ON;
        currentDirection = (direction.equals("front")) ? ComputerDirection.FRONT : ComputerDirection.BACK;

        currentAnimation = computerAnimations.get(new KeyPair<>(currentState, currentDirection)).clone();
        solidArea1 = new Rectangle(8 , 32 , 48 , 28);
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        setDefaultSolidArea();
    }

    public Obj_Computer(String state , String direction , String idName, int x , int y){
        super(x , y);
        name = "Computer";
        this.idName = idName;
        width = 64;
        height = 64;

        currentState = (state.equals("off")) ? ComputerState.OFF : ComputerState.ON;
        currentDirection = (direction.equals("front")) ? ComputerDirection.FRONT : ComputerDirection.BACK;

        currentAnimation = computerAnimations.get(new KeyPair<>(currentState, currentDirection)).clone();
        solidArea1 = new Rectangle(8 , 32 , 48 , 28);
        solidArea2 = new Rectangle(0 , 0 , 0 , 0);
        setDefaultSolidArea();
    }
    @Override
    public void update() {
        if(currentState == ComputerState.ON) {
            currentAnimation.update();
        }
    }

    @Override
    public void render(Graphics2D g2) throws ArrayIndexOutOfBoundsException , NullPointerException {
        currentAnimation.render(g2, worldX - camera.getX() , worldY - camera.getY());
    }

    private enum ComputerDirection{
        BACK, FRONT
    }
    private enum ComputerState{
        ON, OFF
    }
}
