package org.kat.app.ui.hustcore;

import org.kat.app.entity.Entity;
import org.kat.app.level.LevelState;
import org.kat.app.main.UI;
import org.kat.app.ui.UIComponentListener;
import org.kat.app.ui.components.GameButton;
import org.kat.app.ui.views.*;
import org.kat.app.ui.views.Cursor;
import org.kat.app.util.KeyPair;
import org.kat.app.util.Tree;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

import static org.kat.app.main.GamePanel.*;

public class SpeechDisplay extends UIScreen {
    public Queue<KeyPair<Entity, Integer>> dialogueQueue = new LinkedList<>();
    private KeyPair<Entity, Integer> currentPairEntity;

    private Entity currentSpeaker;

    private WrappedTextView speechContent;

    public SpeechDisplay(String id, Tree<View> viewTree) {
        super(id, viewTree);
    }

    @Override
    protected void onCreate() {
        GameButton nextPage = (GameButton) findViewById("nextPage");
        nextPage.setListener(new UIComponentListener() {
            @Override
            public void onPress() {
                if (currentSpeaker != null && speechContent != null) {
                    if (speechContent.isPlaying()) {
                        speechContent.skip();
                    } else {
                        currentSpeaker.dialogueIndex++;
                        speechContent = currentSpeaker.getShownDialogueAt(currentSpeaker.dialogueSet, currentSpeaker.dialogueIndex);
                        if (speechContent == null) {
                            currentSpeaker.dialogueIndex = 0;
                            currentSpeaker = null;
                            if(!dialogueQueue.isEmpty()){
                                currentPairEntity = dialogueQueue.poll();
                                currentSpeaker = currentPairEntity.key1();
                                currentSpeaker.dialogueSet = currentPairEntity.key2();
                            } else {
                                currentLevel.setLevelState(LevelState.RUNNING);
                                hide();
                            }
                        }
                    }
                } else {
                    if(!dialogueQueue.isEmpty()){
                        currentPairEntity = dialogueQueue.poll();
                        currentSpeaker = currentPairEntity.key1();
                        currentSpeaker.submitDialogue(currentPairEntity.key2());
                    } else {
                        speechContent = null;
                        currentLevel.setLevelState(LevelState.RUNNING);
                        hide();
                    }
                }
            }
        });

        GameButton skipButton = (GameButton) findViewById("skipButton");
        skipButton.setListener(new UIComponentListener() {

            @Override
            public void onPress() {
                skip();
            }
        });
    }

    @Override
    public void onShow(){
        super.onShow();
        if (currentSpeaker == null && !dialogueQueue.isEmpty()) {
            currentPairEntity = dialogueQueue.poll();
            currentSpeaker = currentPairEntity.key1();
            currentSpeaker.dialogueSet = currentPairEntity.key2();
        }

        if (currentSpeaker != null) {
            StringBuilder currentDialogue = currentSpeaker.getDialogueAt(currentSpeaker.dialogueSet, currentSpeaker.dialogueIndex);
            if (currentDialogue != null) {
                speechContent = currentSpeaker.getShownDialogueAt(currentSpeaker.dialogueSet, currentSpeaker.dialogueIndex);
            } else {
                currentSpeaker = null;
            }
        } else {
            currentLevel.setLevelState(LevelState.RUNNING);
        }
    }

    public void add(KeyPair<Entity, Integer> pair) {
        dialogueQueue.add(pair);
    }

    @Override
    public void show() {
        super.show();
    }

    public void setContent(String text) {
        speechContent.setText(text);
        speechContent.build();
    }

    public void skip() {
        if(speechContent != null) {
            speechContent.skip();
            speechContent = null;
        }
        if(currentSpeaker != null) {
            currentSpeaker.dialogueIndex = 0;
            currentSpeaker = null;
        }
        dialogueQueue.clear();
        hide();
//        if(UI._UIManager.getCurrentScreen().getId().equals("speech_display")){
//            UI._UIManager.clearFromScreenStack();
//        }

        currentLevel.setLevelState(LevelState.RUNNING);
    }

    @Override
    public void update(){
        super.update();
        if(speechContent != null) speechContent.update();
    }

    @Override
    public void render(Graphics2D g2){
        viewTree.inOrderTraverse(viewTree.getRoot(),
                (node) -> {
                    View currentView = node.getData();
                    if(currentView.isVisible() &&
                            !(currentView instanceof WrappedTextView)) currentView.render(g2);
                });
        if(speechContent != null) speechContent.render(g2);

        Cursor cursor = getCursor();
        if(cursor != null){
            cursor.render(g2);
        }
    }
}
