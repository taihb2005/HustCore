package org.kat.app.ui.views;

import org.kat.app.util.GameTimer;

import java.awt.*;
import java.util.ArrayList;

import static org.kat.app.main.GamePanel.playSE;


public class WrappedTextView extends TextView{
    private ArrayList<Text> textLine;
    private ArrayList<Text> currentDisplayList;
    private int currentLine;
    protected int lineNums;
    private GameTimer lineTimer;

    private boolean buildOnce;

    public WrappedTextView(Text text) {
        super(text);

        lineNums = 0;
    }

    public WrappedTextView(Text text, int x, int y, int width, int height) {
        super(text, x, y, width, height);

        lineNums = 0;
        buildOnce = false;
    }

    public Text getTextAt(int lineNums){
        return textLine.get(lineNums);
    }


    private void wrap(Graphics2D g2) {
        int parentWidth = text.getParentWidth();
        String[] contentsInText = text.getText().split(" ");

        int currentLineWidth = 0;
        StringBuilder currentLineContent = new StringBuilder();

        for (String s : contentsInText) {
            String word = s + " ";
            int wordWidth = g2.getFontMetrics().stringWidth(word);

            if (currentLineWidth + wordWidth <= parentWidth) {
                currentLineContent.append(word);
                currentLineWidth += wordWidth;
            } else {
                if (textLine == null) {
                    textLine = new ArrayList<>();
                }
                Text currentLine = new Text(currentLineContent.toString().trim());
                currentLine.attach(this);
                currentLine.setAlignment(text.getHorizontalAlignment(), text.getVerticalAlignment());
                currentLine.setLineAt(lineNums);
                textLine.add(currentLine);

                currentLineContent.setLength(0);
                currentLineContent.append(word);
                currentLineWidth = wordWidth;

                lineNums++;
            }
        }

        if (!currentLineContent.isEmpty()) {
            Text currentLine = new Text(currentLineContent.toString().trim());
            currentLine.attach(this);
            currentLine.setAlignment(text.getHorizontalAlignment(), text.getVerticalAlignment());
            currentLine.setLineAt(lineNums);
            textLine.add(currentLine);
        }

        textLine.forEach(text -> {
            text.attach(this);
        });
    }

    public void setTextAt(String text, int lineNums){
        try{
            textLine.get(lineNums).setText(text);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void enableDisplayCharByChar(){
        this.displayCharByChar = true;
    }

    private void buildDisplay(){
        this.textIndex = 0;
        this.lastState = TextState.PLAYING;
        this.currentState = TextState.PLAYING;

        if(currentDisplayList == null){
            currentDisplayList = new ArrayList<>();
        } else {
            currentDisplayList.clear();
        }

        textLine.forEach(text -> {
            Text t = new Text("");
            t.setProperties(text);
            t.attach(this);

            currentDisplayList.add(t);
        });

        currentLine = 0;

        if(lineTimer == null){
            lineTimer = new GameTimer(
                    () -> {
                        Text currentText = currentDisplayList.get(currentLine);
                        currentText.append(textLine.get(currentLine).getTextAt(textIndex));
                        textIndex++;

                        if(currentText.equals(textLine.get(currentLine))){
                            currentLine++;
                            textIndex = 0;
                        }

                        if(currentLine == lineNums + 1){
                            reset();
                        }
                    }, 1 , true
            );
        }
    }

    public void skip(){
        reset();
    }

    public void build(){
        buildOnce = false;
    }

    @Override
    public void reset(){
        currentState = TextState.IDLE;
        currentLine = 0;
        textIndex = 0;
        currentDisplayList.forEach(Text::clear);
        displayCharByChar = false;
    }

    @Override
    public void update(){
        if(displayCharByChar){
            if(lineTimer != null) lineTimer.update();
        }
    }

    @Override
    public void render(Graphics2D g2){
        if (textLine == null || !buildOnce) {
            wrap(g2);
            buildDisplay();
            buildOnce = true;
        }

        if(displayCharByChar){
            currentDisplayList.forEach(text -> text.render(g2));
        } else {
            textLine.forEach(text -> text.render(g2));
        }
    }
}
