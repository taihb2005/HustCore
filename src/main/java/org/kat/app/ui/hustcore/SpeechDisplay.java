package org.kat.app.ui.hustcore;

import org.kat.app.entity.Entity;
import org.kat.app.level.LevelState;
import org.kat.app.main.KeyHandler;
import org.kat.app.ui.components.Button;
import org.kat.app.ui.components.GameButton;
import org.kat.app.ui.views.*;
import org.kat.app.ui.views.Cursor;
import org.kat.app.util.KeyPair;
import org.kat.app.util.Tree;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.kat.app.main.GamePanel.*;

public class SpeechDisplay extends UIScreen {
    public Queue<KeyPair<Entity, Integer>> dialogueQueue = new LinkedList<>();
    private KeyPair<Entity, Integer> currentPairEntity;
    private boolean setOnce = false;

    private Entity currentSpeaker;

    private TextView A;
    private TextView B;
    private WrappedTextView speechContent;

    private GameButton nextPage;

    public SpeechDisplay(String id, Tree<View> viewTree) {
        super(id, viewTree);
    }

    @Override
    protected void onCreate() {
//        A = (TextView) findViewById("entityA");
//        B = (TextView) findViewById("entityB");
        speechContent = (WrappedTextView) findViewById("speechContent");

        nextPage = (GameButton) findViewById("nextPage");
    }

    public void add(KeyPair<Entity, Integer> pair) {
        dialogueQueue.add(pair);
    }

    @Override
    public void show() {
        super.show();
        if (currentSpeaker == null && !dialogueQueue.isEmpty()) {
            currentPairEntity = dialogueQueue.poll();
            currentSpeaker = currentPairEntity.key1();
            currentSpeaker.dialogueSet = currentPairEntity.key2();
        }

        if (currentSpeaker != null) {
            StringBuilder currentDialogue = currentSpeaker.dialogues[currentSpeaker.dialogueSet][currentSpeaker.dialogueIndex];
            if (currentDialogue != null) {

                if(!speechContent.isPlaying() && !setOnce) {
                    speechContent.setText(currentDialogue.toString());
                    speechContent.build();
                    speechContent.enableDisplayCharByChar();
                    setOnce = true;
                }

                if (KeyHandler.enterPressed) {
                    KeyHandler.enterPressed = false;
                    if(speechContent.isPlaying()){
                        speechContent.skip();
                    } else {
                        setOnce = false;
                        currentSpeaker.dialogueIndex++;
                        if (currentSpeaker.dialogues[currentSpeaker.dialogueSet][currentSpeaker.dialogueIndex] == null) {
                            currentSpeaker.dialogueIndex = 0;
                            currentSpeaker = null;
                        }
                    }
                }

            } else {
                currentSpeaker = null;
            }
        } else {
            currentLevel.setLevelState(LevelState.RUNNING);
        }
    }

    public void setTextEntityA(String text) {
        A.setText(text);
    }

    public void setTextEntityB(String text) {
        B.setText(text);
    }

    public void setContent(String text){
        speechContent.setText(text);
        speechContent.build();
    }

    public void playText(){

    }

    public void playContentAt(int lineNums){

    }

    public void skip(){

    }

    @Override
    public void update(){
        super.update();
    }

    @Override
    public void render(Graphics2D g2){
        super.render(g2);
    }
}
