package entity.object;

import entity.Entity;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static main.GamePanel.camera;

public class Obj_PasswordAuth extends Entity {
    private static final HashMap<PasswordAuthState, Sprite> passwordAuthSpritePool = new HashMap<>();
    private static final HashMap<PasswordAuthState, Animation> passwordAuthAnimations = new HashMap<>();

    public static void load(){
        for(PasswordAuthState state: PasswordAuthState.values()){
            passwordAuthSpritePool.put(state,
                    new Sprite(AssetPool.getImage("password_authentication_" + state.name().toLowerCase() + ".png")));
            passwordAuthAnimations.put(state,
                    new Animation(passwordAuthSpritePool.get(state).getSpriteArrayRow(0), 20, true));
        }
    }
    private PasswordAuthState currentState;
    private Animation currentAnimation;

    public Obj_PasswordAuth(String state , int x , int y){
        super(x , y);
        name = "Password Authentication";
        width = 64;
        height = 64;

        currentState = (state.equals("inactive")) ? PasswordAuthState.INACTIVE : PasswordAuthState.ACTIVE;
        currentAnimation = passwordAuthAnimations.get(currentState).clone();
        solidArea1 = new Rectangle(0 , 0 , 0 , 0);
    }

    public Obj_PasswordAuth(String state, String idName, int x , int y){
        super(x , y);
        name = "Password Authentication";
        this.idName = idName;
        width = 64;
        height = 64;

        currentState = (state.equals("inactive")) ? PasswordAuthState.INACTIVE : PasswordAuthState.ACTIVE;
        currentAnimation = passwordAuthAnimations.get(currentState).clone();
        solidArea1 = new Rectangle(0 , 0 , 0 , 0);
    }
    @Override
    public void update() throws NullPointerException{
        currentAnimation.update();
    }

    @Override
    public void render(Graphics2D g2) throws NullPointerException , ArrayIndexOutOfBoundsException{
        currentAnimation.render(g2, worldX- camera.getX(), worldY - camera.getY());
    }

    private enum PasswordAuthState{
        INACTIVE, ACTIVE
    }
}
