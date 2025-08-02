package org.kat.app.entity;

import org.kat.app.ai.PathFinder;
import org.kat.app.entity.projectile.Projectile;
import org.kat.app.graphics.Animation;
import org.kat.app.level.LevelState;
import org.kat.app.main.KeyHandler;
import org.kat.app.main.UI;
import org.kat.app.map.GameMap;
import org.kat.app.ui.hustcore.SpeechDisplay;
import org.kat.app.ui.views.Text;
import org.kat.app.ui.views.View;
import org.kat.app.ui.views.WrappedTextView;
import org.kat.app.util.KeyPair;
import org.kat.app.util.Vector2D;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.kat.app.main.GamePanel.*;

public class Entity {
    public PathFinder pFinder;
    public static final ExecutorService executor = Executors.newFixedThreadPool(4);

    protected static final Vector2D UP_DIR = new Vector2D(0, -1);
    protected static final Vector2D DOWN_DIR = new Vector2D(0, 1);
    protected static final Vector2D LEFT_DIR = new Vector2D(-1, 0);
    protected static final Vector2D RIGHT_DIR = new Vector2D(1, 0);

    private static final int MAX_DIALOGUE_SET = 5;
    private static final int MAX_DIALOGUE_COUNT = 30;

    public String name;
    public String idName = "";
    //POSITION
    public Vector2D position;
    public Vector2D newPosition;
    public Vector2D velocity;
    public String direction;
    public int speed;
    public int lastSpeed;
    //ANIMATION
    protected Animation currentAnimation;
    protected Animation lastAnimation;
    //BOOLEAN
    public boolean collisionOn = false;
    public boolean isInteracting = false;
    public boolean isOpening = false;
    public boolean isInvincible = false;
    public boolean isDying = false;
    public boolean isCollected = false;
    public boolean canChangeStatus = false;
    public boolean canbeDestroyed = false;
    public boolean onPath = false;
    //SPRITE SIZE
    public int width;
    public int height;
    //SOLID AREA
    public Rectangle solidArea1 = new Rectangle();
    public Rectangle solidArea2 = new Rectangle();
    public Rectangle hitbox = new Rectangle();
    public Rectangle interactionDetectionArea = new Rectangle();
    public int solidAreaDefaultX1 = 0;
    public int solidAreaDefaultY1 = 0;
    public int solidAreaDefaultX2 = 0;
    public int solidAreaDefaultY2 = 0;

    //CHARACTER STATUS
    public int level;
    public int maxHP;
    public int currentHP;
    public int maxMana;
    public int currentMana;
    public int exp;
    public int strength;
    public int lastStrength;
    public int strengthScalar = 1;
    public int defaultStrengthScalar = 1;
    public int defense;
    public int damage;
    public String projectileName;
    public Projectile projectile;
    public int shootAvailableCounter = 0;
    public int invincibleCounter = 0;
    public int invincibleDuration;//Thời gian bất tử

    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    public StringBuilder[][] dialogues = new StringBuilder[MAX_DIALOGUE_SET][MAX_DIALOGUE_COUNT];
    public WrappedTextView[][] shownDialogues = new WrappedTextView[MAX_DIALOGUE_SET][MAX_DIALOGUE_COUNT];

    public int dialogueIndex;
    public int dialogueSet = -1;

    public Entity() {
        this.position = new Vector2D();
        this.newPosition = new Vector2D();
        this.velocity = new Vector2D();
    }

    public Entity(int x, int y) {
        this.position = new Vector2D(x, y);
        this.newPosition = new Vector2D(x, y);
        this.velocity = new Vector2D();
    }

    public void setDefaultSolidArea() {
        if (solidArea1 != null) {
            solidAreaDefaultX1 = solidArea1.x;
            solidAreaDefaultY1 = solidArea1.y;
        }

        if (solidArea2 != null) {
            solidAreaDefaultX2 = solidArea2.x;
            solidAreaDefaultY2 = solidArea2.y;
        }
    }

    public void submitDialogue(int dialogueSet) {
        if(currentLevel.checkState(LevelState.RUNNING)) {
            KeyHandler.enterPressed = false;
            currentLevel.setLevelState(LevelState.DIALOGUE);
            ((SpeechDisplay) UI._UIManager.findUIScreenByName("speech_display")).add(new KeyPair<>(this, dialogueSet));
            UI._UIManager.setCurrentScreen("speech_display");
            //ui.dialogueQueue.add(new KeyPair<>(entity, dialogueSet));
        }
    }

    public StringBuilder getDialogueAt(int dialogueSet, int dialogueIndex) {
        return this.dialogues[dialogueSet][dialogueIndex];
    }

    public WrappedTextView getShownDialogueAt(int dialogueSet, int dialogueIndex) {
        return this.shownDialogues[dialogueSet][dialogueIndex];
    }

    public void setDialogueAt(int dialogueSet, int dialogueIndex, String dialogue){
        if(dialogueSet > MAX_DIALOGUE_SET || dialogueIndex > MAX_DIALOGUE_COUNT){
            throw new IllegalArgumentException("Dialogue set is out of bounds");
        }

        if(dialogues[dialogueSet][dialogueIndex] == null){
            dialogues[dialogueSet][dialogueIndex] = new StringBuilder();
        } else dialogues[dialogueSet][dialogueIndex].setLength(0);

        dialogues[dialogueSet][dialogueIndex].append(dialogue);
    }

    public void buildDialogue() {
        WrappedTextView parent = (WrappedTextView) UI._UIManager
                .findUIScreenByName("speech_display")
                .findViewById("speechContent");

        for(int i = 0; i < dialogues.length; i++) {
            for(int j = 0; j < dialogues[i].length; j++) {
                if(dialogues[i][j] == null) continue;
                if(shownDialogues[i][j] == null){
                    Text t = new Text(dialogues[i][j].toString());
                    t.setProperties(parent.getText());
                    shownDialogues[i][j] = new WrappedTextView(t, parent.getX(), parent.getY(), parent.getWidth(), parent.getHeight())
                            .enableDisplayCharByChar()
                            .build();
                } else {
                    shownDialogues[i][j].clear()
                            .setText(dialogues[i][j].toString())
                            .enableDisplayCharByChar()
                            .build();
                }
            }
        }
    }

    public void talk() {
    }

    public void set() {
    }

    public void projectileCauseEffect(){

    }

    public String getOppositeDirection(String direction){
        return  switch (direction) {
            case "left" -> "right";
            case "right" -> "left";
            case "up" -> "down";
            case "down" -> "up";
            default -> "";
        };
    }

    public void die() {
        isDying = true;
        hitbox = new Rectangle(0, 0, 0, 0);
    }

    public void updateInvincibility() {
        if (isInvincible) {
            invincibleCounter++;
            if (invincibleCounter >= invincibleDuration) {
                invincibleCounter = 0;
                isInvincible = false;
            }
        }
    }

    public void setHitbox() {
    }

    public void checkCollision(){
        collisionOn = false;
        currentMap.cChecker.checkCollisionWithEntity(this , currentMap.inactiveObj);
        currentMap.cChecker.checkCollisionWithEntity(this , currentMap.npc);
        currentMap.cChecker.checkCollisionWithEntity(this , currentMap.activeObj);
        currentMap.cChecker.checkCollisionWithEntity(this , currentMap.enemy);
    }

    public void searchPath(int goalCol, int goalRow) {
        int worldX = (int)position.x;
        int worldY = (int)position.y;
        int startCol = (worldX + solidArea1.x) / GameMap.childNodeSize;
        int startRow = (worldY + solidArea1.y) / GameMap.childNodeSize;
        pFinder.setNodes(startCol,startRow,goalCol,goalRow);
        if(pFinder.search())
        {
            //Next WorldX and WorldY
            if(pFinder.pathList.isEmpty()){
                onPath = false;
                up = down = left = right = false;
            } else {
                int nextX = pFinder.pathList.get(0).col * GameMap.childNodeSize;
                int nextY = pFinder.pathList.get(0).row * GameMap.childNodeSize;


                //Entity's solidArea position
                int enLeftX = worldX + solidArea1.x;
                int enRightX = worldX + solidArea1.x + solidArea1.width;
                int enTopY = worldY + solidArea1.y;
                int enBottomY = worldY + solidArea1.y + solidArea1.height;

                // TOP PATH
                if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + GameMap.childNodeSize) {
                    direction = "up";
                }
                // BOTTOM PATH
                else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + GameMap.childNodeSize) {
                    direction = "down";
                }
                // RIGHT - LEFT PATH
                else if (enTopY >= nextY && enBottomY < nextY + GameMap.childNodeSize) {
                    //either left or right
                    // LEFT PATH
                    if (enLeftX > nextX) {
                        direction = "left";
                    }
                    // RIGHT PATH
                    if (enLeftX < nextX) {
                        direction = "right";
                    }
                }
                //OTHER EXCEPTIONS
                else if (enTopY > nextY && enLeftX > nextX) {
                    // up or left
                    direction = "up";
                    checkCollision();
                    //System.out.println(collisionOn);
                    if (collisionOn) {
                        direction = "left";
                    }
                } else if (enTopY > nextY && enLeftX < nextX) {
                    // up or right
                    direction = "up";
                    checkCollision();
                    if (collisionOn) {
                        direction = "right";
                    }
                } else if (enTopY < nextY && enLeftX > nextX) {
                    // down or left
                    direction = "down";
                    checkCollision();
                    if (collisionOn) {
                        direction = "left";
                    }
                } else if (enTopY < nextY && enLeftX < nextX) {
                    // down or right
                    direction = "down";
                    checkCollision();
                    if (collisionOn) {
                        direction = "right";
                    }
                }
            }
        }
    }

    public void decideToMove() {
        up = down = left = right = false;
        switch (direction) {
            case "right":
                right = true;
                break;
            case "left":
                left = true;
                break;
            case "down":
                down = true;
                break;
            case "up":
                up = true;
                break;
        }
    }

    public void update(){};

    public void render(Graphics2D g2){
        currentAnimation.render(g2, (int)position.x - camera.getX(), (int)position.y - camera.getY());
    }

    public void dispose() {
        if (dialogues != null) {
            for (StringBuilder[] s : dialogues) {
                if (s != null) {
                    Arrays.fill(s, null);
                }
            }
            dialogues = null;
        }

        pFinder = null;
        solidArea1 = null;
        solidArea2 = null;
        hitbox = null;
        projectile = null;
        name = null;
        idName = null;
    }
}