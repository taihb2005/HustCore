package main;

import entity.Entity;
import entity.player.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;


import static main.GamePanel.*;

public class UI {
    private final GamePanel gp;
    public Player player;
    Graphics2D g2;
    public Font joystix;
    public Font maru;
    String currentDialogue = "";  // Dòng hội thoại hiện tại đầy đủ
    String displayedText = "";    // Dòng hội thoại đang được hiển thị dần
    int textIndex = 0;            // Chỉ số của ký tự đang được hiển thị
    double textSpeed = 0.1;       // Tốc độ hiển thị từng ký tự (càng nhỏ càng nhanh)
    int frameCounter = 0;         // Đếm số frame để điều khiển tốc độ hiển thị
    int subState = 0;
    int dialogueCount;
    private BufferedImage gameOverBackground;


    Color displayItemquantity = new Color(204 , 180 , 50);
    Color displayTextItemQuantity = Color.WHITE;

    private boolean slotselected = true;
    int selectedSlot = -1;

    public int commandNum = 0;

    public Entity target;
    private BufferedImage hpFrame, manaFrame;

    private BufferedImage titleBackground;


    public Entity npc;
    public UI(GamePanel gp) {
        this.gp = gp;
        this.player = currentMap.player;
        try {
            InputStream is1 = getClass().getResourceAsStream("/font/joystix monospace.otf");
            InputStream is2 = getClass().getResourceAsStream("/font/MaruMonica.ttf");
            joystix = Font.createFont(Font.TRUETYPE_FONT, is1);
            maru= Font.createFont(Font.TRUETYPE_FONT , is2);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        try {
            hpFrame = ImageIO.read(getClass().getResourceAsStream("/ui/hpFrame.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            manaFrame = ImageIO.read(getClass().getResourceAsStream("/ui/manaFrame.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            titleBackground = ImageIO.read(getClass().getResourceAsStream("/ui/titleBackground.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            gameOverBackground = ImageIO.read(getClass().getResourceAsStream("/ui/robotInvasion.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Bắt đầu đoạn hội thoại ngay khi khởi tạo UI
        // DEMO
        //startDialogue("Tôi là siêu anh hùng đến từ HUST.\nCon hồ ly tinh này đáng sợ quá");
    }
    public void startDialogue(String dialogue) {
        currentDialogue = dialogue;
        displayedText = "";
        textIndex = 0;
        frameCounter = 0;
    }

    public void drawDialogueScreen() {
        g2.setFont(joystix.deriveFont(Font.PLAIN, 19));
        int x = tileSize * 2;
        int y = tileSize / 2;
        int width = gp.getWidth() - tileSize * 4;
        int height = tileSize * 4;

        if(target.dialogues[target.dialogueSet][target.dialogueIndex] != null) {
            currentDialogue = target.dialogues[target.dialogueSet][target.dialogueIndex];
            drawSubWindow(x, y, width, height);

            frameCounter++;
            if (frameCounter > textSpeed) {
                frameCounter = 0;
                if (textIndex < currentDialogue.length()) {
                    // Cập nhật displayedText theo từng ký tự
                    gp.playSE(1);
                    displayedText += currentDialogue.charAt(textIndex);
                    textIndex++;
                }
            }
            if(KeyHandler.enterPressed)
            {
                KeyHandler.enterPressed = false;
                textIndex = 0;
                displayedText = "";
                if(gameState == GameState.DIALOGUE_STATE )
                {
                    target.dialogueIndex++;
                }
            }
        }else
        {
            target.dialogueIndex = 0;
            if(gameState == GameState.DIALOGUE_STATE)
            {
                gameState = GameState.PLAY_STATE;
            }
        }

        // Vẽ đoạn hội thoại từng dòng
        x += tileSize;
        y += tileSize;
        for (String line : displayedText.split("\n")) { // splits dialogue until "\n" as a line
            g2.drawString(line, x, y);
            y += 40;
        }

    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0,0,0, 100); //BLACK
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255,255,255); // WHITE
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x, y, width, height, 25, 25);
    }
    // Hàm vẽ thử

    public void drawTitleScreen(){
        g2.drawImage(manaFrame, 0, 0, 139, 28, null);
        g2.drawImage(titleBackground, 0, 0, windowWidth, windowHeight, null);
    }

    private void drawPausedScreen()
    {
        g2.setColor(new Color(0 , 0 , 0 , 100));
        g2.fillRoundRect(0, 0, windowWidth, windowHeight , 0 , 0);
        g2.setFont(joystix.deriveFont(Font.PLAIN, 80f));
        g2.setColor(Color.WHITE);
        String text = "";
        int x = getXforCenteredText(text);
        int y = windowHeight / 2;
        g2.drawString(text , x , y );
    }

    public int getXforCenteredText(String text)
    {
        int length = (int)g2.getFontMetrics().getStringBounds(text , g2).getWidth();
        int x = windowWidth / 2 - length / 2;
        return x;
    }

    public Font getFont(){return joystix;};

    public void drawHPBar() {
        int fullHPWidth = 175;  // Chiều dài tối đa của thanh HP
        int hpBarHeight = 12;   // Chiều cao của thanh HP
        int x = 70;
        int y = windowHeight - 80;
        int currentHPWidth = (int)((double)player.currentHP / player.maxHP * fullHPWidth);
        // Vẽ nền (màu xám) cho thanh HP
        g2.drawImage(hpFrame, x-52, y-8, 251, 28, null);

        // Vẽ thanh HP hiện tại (màu đỏ)
        g2.setColor(Color.RED);
        g2.fillRect(x, y , currentHPWidth, hpBarHeight);
    }

    public void drawManaBar() {
        int fullManaWidth = 63;  // Chiều dài tối đa của thanh HP
        int ManaBarHeight = 12;   // Chiều cao của thanh HP
        int x = 70;
        int y = windowHeight - 45;
        int currentHPWidth = (int)((double)player.currentMana / player.maxMana * fullManaWidth);
        // Vẽ nền (màu xám) cho thanh Mana
        g2.drawImage(manaFrame, x-52, y-8, 139, 28, null);

        // Vẽ thanh HP hiện tại (màu xanh)
        g2.setColor(Color.BLUE);
        g2.fillRect(x, y, currentHPWidth, ManaBarHeight);
    }

    public void drawRewardWindow(){
        g2.setFont(joystix.deriveFont(Font.PLAIN, 19));
        int x = tileSize * 2;
        int y = tileSize / 2;
        int width = gp.getWidth() - tileSize * 4;
        int height = tileSize * 4;
        drawSubWindow(x , y , width , height);
    }

    public void drawLevelUpWindow(){
        g2.setFont(joystix.deriveFont(Font.PLAIN, 19));
        int x = tileSize * 2;
        int y = tileSize / 2;
        int width = gp.getWidth() - tileSize * 4;
        int height = tileSize * 4;
        drawSubWindow(x , y , width , height);
    }

        public void drawGameOverScreen() {
        //BACKGROUND
        g2.drawImage(gameOverBackground, 0, 0, windowWidth, windowHeight, null);
        //MENU
        g2.setFont(joystix.deriveFont(Font.BOLD, 35f));
        g2.setColor(Color.white);

        String text = "RETRY";
        int x = getXforCenteredText(text);
        int y = windowHeight / 2;
        int length = (int)g2.getFontMetrics().getStringBounds(text , g2).getWidth();

        g2.drawString(text, x, y);
        //BOUND
        g2.drawRoundRect(x - tileSize / 2, y - tileSize, length + length / 3 - 10, tileSize + 10, 40, 40);
        if(commandNum == 0) {
            g2.drawString(">", x - tileSize - 10, y);
        }

        text = "MENU";
        x = getXforCenteredText(text);
        y += tileSize + 20;
        g2.drawString(text, x, y);
        //BOUND
        length = (int)g2.getFontMetrics().getStringBounds(text , g2).getWidth();
        g2.drawRoundRect(x - tileSize / 2, y - tileSize, length + length / 3, tileSize + 10, 40, 40);
        if(commandNum == 1) {
            g2.drawString(">", x - tileSize - 10, y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += tileSize + 20;
        g2.drawString(text, x, y);
        //BOUND
        length    = (int)g2.getFontMetrics().getStringBounds(text , g2).getWidth();
        g2.drawRoundRect(x - tileSize / 2, y - tileSize, length + length / 3 , tileSize + 10, 40, 40);
        if(commandNum == 2) {
            g2.drawString(">", x - tileSize - 10, y);
        }
    }

    public void drawOptionsScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        // SUB WINDOW

        int frameX = tileSize * 4;
        int frameY = tileSize;
        int frameWidth = tileSize * 8;
        int frameHeight = tileSize * 10;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch(subState) {
            case 0: options_top(frameX, frameY); break;
        }
    }
    public void options_top(int frameX, int frameY) {
        int textX, textY;
        Font codeFont = new Font("Consolas", Font.PLAIN, 20);  // Thử với Consolas
        g2.setFont(codeFont);
        // TITLE
        String text = "Options";
        textX = getXforCenteredText(text);
        textY = frameY + tileSize;
        g2.drawString(text, textX, textY);

        // MUSIC
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("Music", textX, textY);
        g2.drawString(String.valueOf(music.volumePercentage), textX + 200, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX-25, textY);
        }

        // SE
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("SE", textX, textY);
        g2.drawString(String.valueOf(se.volumePercentage), textX + 200, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX-25, textY);
        }

        // RETRY
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("Retry", textX, textY);
        if (commandNum == 2) {
            g2.drawString(">", textX-25, textY);
        }

        // EXIT
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("Exit", textX, textY);
        if (commandNum == 3) {
            g2.drawString(">", textX-25, textY);
        }
    }
    public void drawInventory() {

        int frameX = tileSize / 4;
        int frameY = tileSize / 2 + 80;
        int frameWidth = tileSize * 2 - 18;
        int frameHeight = tileSize * 6 + 32;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        int slotX = frameX + 15;
        int slotY = frameY + 12;
        int slotSize = tileSize / 4;

        if (KeyHandler.key1pressed) {
            selectedSlot = 0;
            slotselected = true;
            KeyHandler.key1pressed = false;
        } else if (KeyHandler.key2pressed) {
            selectedSlot = 1;
            slotselected = true;
            KeyHandler.key2pressed = false;
        } else if (KeyHandler.key3pressed) {
            selectedSlot = 2;
            slotselected = true;
            KeyHandler.key3pressed = false;
        } else if (KeyHandler.key4pressed) {
            selectedSlot = 3;
            slotselected = true;
            KeyHandler.key4pressed = false;
        } else if (KeyHandler.key5pressed) {
            selectedSlot = 4;
            slotselected = true;
            KeyHandler.key5pressed = false;
        }

        g2.setFont(joystix.deriveFont(Font.PLAIN, 15));

        for (int i = 0; i < 5; i++) {
            int currentslotY = slotY + i * (slotSize + 50);
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRoundRect(slotX, currentslotY, 50, 50, 10, 10);

            g2.setColor(new Color(255, 255, 255));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(slotX, currentslotY, 50, 50, 10, 10);
        }
//
//            if (slotselected && i == selectedSlot)
//            {
//                g2.setStroke(new BasicStroke(5));
//                g2.drawRoundRect(slotX, currentslotY, 50, 50, 10, 10);
//            }
//        }
        if (currentMap.player.inventory != null) for (int i = 0; i < currentMap.player.inventory.length; i++) {
            int currentSlotY = slotY + i * (slotSize + 50);
            if (currentMap.player.inventory[i] != null) {
                // Vẽ icon của item
                g2.drawImage(currentMap.player.inventory[i].getIcon(), slotX + 8, currentSlotY + 8, 33, 33, null);
                g2.setFont(maru.deriveFont(Font.PLAIN , 25));
                String quantity = Integer.toString( currentMap.player.inventory[i].getQuantity());
                g2.drawString( quantity , slotX + 37 , currentSlotY + 46);

                // Hiển thị viền slot được chọn
            }
        }
    }
    public void render(Graphics2D g2) {
        this.g2 = g2; // Gán đối tượng g2 vào để sử dụng
        if(gameState == GameState.PLAY_STATE)
        {
            drawHPBar();
            drawManaBar();
            drawInventory();
        }
        else if(gameState == GameState.MENU_STATE)
        {
            drawTitleScreen();
        }
        else if(gameState == GameState.DIALOGUE_STATE)
        {
            drawDialogueScreen();
            drawHPBar();
            drawManaBar();
            drawInventory();
        }
        if(gameState == GameState.LEVELUP_STATE){
            drawDialogueScreen();
        } else
        if(gameState == GameState.PAUSE_STATE)
        {
            drawHPBar();
            drawManaBar();
            drawPausedScreen();
            drawOptionsScreen();
            drawInventory();
        }
        if(gameState == GameState.LOSE_STATE){
            currentMap.dispose();
            drawGameOverScreen();
        }
    }
}

