package entity.object;

import entity.Entity;
import graphics.Animation;
import graphics.AssetPool;
import graphics.Sprite;
import main.KeyHandler;
import util.KeyTriple;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static main.GamePanel.camera;

public class Obj_Door extends Entity{
    private static final HashMap<KeyTriple<DoorSize, DoorState, DoorStatus>, Sprite> doorSpritePool = new HashMap<>();
    private static final HashMap<KeyTriple<DoorSize, DoorState, DoorStatus>, Animation> doorAnimations = new HashMap<>();

    public static void load() {
        for (DoorSize size : DoorSize.values()) {
            int height = 64;
            int width = switch (size) {
                case BIG -> 128;
                case SMALL -> 64;
            };
            for (DoorState state : DoorState.values()){
                boolean loop = switch (state){
                    case OPENING, CLOSING -> false;
                    case OPENED, CLOSED -> true;
                };
                for(DoorStatus status: DoorStatus.values()){
                    String imageName = getString(size, state, status);
                    if(AssetPool.getImage(imageName) == null) continue;

                    KeyTriple<DoorSize, DoorState, DoorStatus> key = new KeyTriple<>(size, state, status);

                    doorSpritePool.put(key,
                            new Sprite(AssetPool.getImage(imageName), width, height));
                    doorAnimations.put(key,
                            new Animation(doorSpritePool.get(key).getSpriteArrayRow(0), 15, loop));

                }

            }
        }

        for(DoorSize size: DoorSize.values()) {
            for (DoorState state : DoorState.values())
                for (DoorStatus status : DoorStatus.values()) {
                    int speed = switch (status) {
                        case INACTIVE, ACTIVE -> 20;
                        default -> 15;
                    };

                    KeyTriple<DoorSize, DoorState, DoorStatus> key = new KeyTriple<>(size, state, status);

                    boolean loop = state == DoorState.CLOSED;

                    if (doorSpritePool.containsKey(key)) {
                        doorAnimations.put(new KeyTriple<>(size, state, status),
                                new Animation(doorSpritePool.get(key).getSpriteArrayRow(0), speed, loop));
                    }
                }
        }
    }

    private void setState(){
        boolean change1 = false;
        boolean change2 = false;
        if(currentState != lastState){
            lastState = currentState;
            change1 = true;
        }
        if(currentStatus != lastStatus){
            lastStatus = currentStatus;
            change2 = true;
        }

        if(change1 || change2) {
            currentAnimation.reset();
            currentAnimation = doorAnimations.get(new KeyTriple<>(currentSize, currentState, currentStatus)).clone();
            currentAnimation.reset();
        }
    }

    private static String getString(DoorSize size, DoorState state, DoorStatus status) {
        String namePrefix = "door_" + size.name().toLowerCase() + "_";
        String namePostfix = ".png";

        String imageName = namePrefix + state.name().toLowerCase();

        if(status != null) {
            imageName = switch (status) {
                case INACTIVE -> imageName + "_inactive";
                case ACTIVE -> imageName + "_active";
                case NONE -> imageName;
            };
        }

        imageName += namePostfix;
        return imageName;
    }

    private DoorSize currentSize;
    private DoorState currentState;
    private DoorState lastState;
    private DoorStatus initialStatus;
    private DoorStatus currentStatus;
    private DoorStatus lastStatus;
    private Animation currentAnimation;

    private final BufferedImage effect;


    public Obj_Door(String size , String status, String idName , int x , int y){
        super(x  , y);
        this.name = "Door";
        this.idName = idName;

        if(size.equals("big")){
            currentSize = DoorSize.BIG;
            solidArea1 = new Rectangle(0 , 17 , 128 , 47);
            solidArea2 = new Rectangle(0 , 0 , 0 , 0);
            interactionDetectionArea = new Rectangle(0, 11, 128, 64);
            effect = AssetPool.getImage("door_big_closed_glowing.png");
        } else {
            currentSize = DoorSize.SMALL;
            solidArea1 = new Rectangle(0 , 17 , 64 , 47);
            solidArea2 = new Rectangle(0 , 0 , 0 , 0);
            interactionDetectionArea = new Rectangle(0, 11, 64, 57);
            effect = AssetPool.getImage("door_small_closed_glowing.png");
        }
        setDefaultSolidArea();

        initialStatus = (status.equals("inactive")) ? DoorStatus.INACTIVE : DoorStatus.ACTIVE;
        currentStatus = initialStatus;
        lastStatus = initialStatus;

        currentState = DoorState.CLOSED;
        lastState = DoorState.CLOSED;
        currentAnimation = doorAnimations.get(new KeyTriple<>(currentSize, currentState, currentStatus)).clone();

        dialogueSet = -1;
        dialogueIndex = 0;
        setDialogue();
    }

    public void open(){
        if (KeyHandler.enterPressed) {
            KeyHandler.enterPressed = false;
            if(isInteracting && currentStatus == DoorStatus.ACTIVE && currentState != DoorState.OPENED){
                currentState = DoorState.OPENING;
            } else if(currentStatus == DoorStatus.INACTIVE)
                talk();
        }
    }

    public void close(){
        if(currentState == DoorState.OPENED){
            currentState = DoorState.CLOSING;
        }
    }

    private void handleAnimation(){
        setState();

        if(currentAnimation.isFinished() && currentState == DoorState.OPENING){
            currentState = DoorState.OPENED;
            if(currentSize == DoorSize.SMALL) {
                solidArea1 = new Rectangle(0, 17, 14, 47);
                solidArea2 = new Rectangle(52, 17, 12, 47);
            } else {
                solidArea1 = new Rectangle(0, 17, 28, 47);
                solidArea2 = new Rectangle(102, 17, 26, 47);
            }
            interactionDetectionArea = new Rectangle(0, 0, 0, 0);
            setDefaultSolidArea();
        }

        if(currentAnimation.isFinished() && currentState == DoorState.CLOSING){
            currentState = DoorState.OPENED;
            if(currentSize == DoorSize.SMALL) {
                solidArea1 = new Rectangle(0 , 17 , 128 , 47);
                solidArea2 = new Rectangle(0 , 0 , 0 , 0);
                interactionDetectionArea = new Rectangle(0, 11, 128, 64);
            } else {
                solidArea1 = new Rectangle(0 , 17 , 64 , 47);
                solidArea2 = new Rectangle(0 , 0 , 0 , 0);
                interactionDetectionArea = new Rectangle(0, 11, 64, 57);
            }
            setDefaultSolidArea();
        }
        if(isInteracting) isInteracting = false;
    }

    private void setDialogue(){
        dialogues[0][0] = new StringBuilder("Hmm... Nó đã bị khóa!");
    }

    public void talk(){
        dialogueSet++;
        if(dialogues[dialogueSet][0] == null) {
            dialogueIndex = 0;
            dialogueSet--;
        }
        startDialogue(this , dialogueSet);
    }


    @Override
    public void update() throws  NullPointerException{
        if(isInteracting) open();
        handleAnimation();
        currentAnimation.update();
    }

    @Override
    public void render(Graphics2D g2)  {
        currentAnimation.render(g2, worldX - camera.getX(), worldY - camera.getY());
        if(isInteracting && (currentStatus == DoorStatus.INACTIVE || currentStatus == DoorStatus.ACTIVE))
            g2.drawImage(effect, worldX - camera.getX(), worldY - camera.getY(), null);
    }

    private enum DoorSize{
        BIG, SMALL
    }

    private enum DoorState {
        CLOSED, OPENING, CLOSING, OPENED
    }

    private enum DoorStatus {
        INACTIVE, ACTIVE, NONE
    }

}
