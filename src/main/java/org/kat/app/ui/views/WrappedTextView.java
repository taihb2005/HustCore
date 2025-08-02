package org.kat.app.ui.views;

import org.kat.app.util.GameTimer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


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


    private void wrap() {
        BufferedImage image = new BufferedImage(1, 1 , BufferedImage.TYPE_INT_ARGB);
        Graphics g2 = image.createGraphics();
        g2.setFont(text.baseFont.deriveFont(text.fontSize));

        int parentWidth = text.getParentWidth();
        String[] contentSplits = text.getText().split("\n");

        if (textLine == null) {
            textLine = new ArrayList<>();
        } else textLine.clear();

        for(String contentSplit: contentSplits) {
            String[] contentsInText = contentSplit.split(" ");

            int currentLineWidth = 0;
            StringBuilder currentLineContent = new StringBuilder();

            for (String s : contentsInText) {
                String word = s + " ";
                int wordWidth = g2.getFontMetrics().stringWidth(word);

                if (currentLineWidth + wordWidth <= parentWidth) {
                    currentLineContent.append(word);
                    currentLineWidth += wordWidth;
                } else {
                    Text currentLine = new Text(currentLineContent.toString().trim());
                    currentLine.attach(this);
                    currentLine.setAlignment(text.getHorizontalAlignment(), text.getVerticalAlignment());
                    currentLine.setLineAt(lineNums);
                    currentLine.setFont(text.getFont());
                    currentLine.setFontSize(text.getFontSize());
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
            textLine.add(new Text(" "));
            lineNums++;
        }


        textLine.forEach(text -> {
            text.attach(this);
        });

        buildOnce = true;
        g2.dispose();
        image.flush();
    }

    @Override
    public WrappedTextView setText(String text) {
        super.setText(text);
        return this;
    }

    public void setTextAt(String text, int lineNums){
        try{
            textLine.get(lineNums).setText(text);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public WrappedTextView clear(){
        text.clear();
        return this;
    }

    @Override
    public WrappedTextView enableDisplayCharByChar(){
        this.displayCharByChar = true;
        return this;
    }

    private WrappedTextView postBuild(){
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

        return this;
    }

    public void skip(){
        reset();
    }

    public WrappedTextView build(){
        buildOnce = false;
        wrap();
        postBuild();
        buildOnce = true;
        return this;
    }

    public boolean isBuilt(){
        return buildOnce;
    }

    @Override
    public void reset(){
        currentState = TextState.IDLE;
        lineNums = 0;
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
        if(displayCharByChar){
            currentDisplayList.forEach(text -> text.render(g2));
        } else {
            if(textLine != null)
                textLine.forEach(text -> text.render(g2));
            else {
                System.out.println("null");
            }
        }

//        g2.drawRect(x, y, width, height);
    }
}
