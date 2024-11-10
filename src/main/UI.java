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
    String currentDialogue = "";  // Dòng hội thoại hiện tại đầy đủ
    String displayedText = "";    // Dòng hội thoại đang được hiển thị dần
    int textIndex = 0;            // Chỉ số của ký tự đang được hiển thị
    double textSpeed = 0.1;       // Tốc độ hiển thị từng ký tự (càng nhỏ càng nhanh)
    int frameCounter = 0;         // Đếm số frame để điều khiển tốc độ hiển thị
    int subState = 0;
    int dialogueCount;

    public int commandNum = 0;

    public Entity target;

    private BufferedImage hpFrame, manaFrame;

    private BufferedImage titleBackground;

    public Entity npc;
    public UI(GamePanel gp) {
        this.gp = gp;
        this.player = gp.currentMap.player;
        try {
            InputStream is = getClass().getResourceAsStream("/font/joystix monospace.otf");
            joystix = Font.createFont(Font.TRUETYPE_FONT, is);
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

        if(target.dialogues[target.dialogueIndex] != null) {
            currentDialogue = target.dialogues[target.dialogueIndex];
            drawSubWindow(x, y, width, height);

            frameCounter++;
            if (frameCounter > textSpeed) {
                frameCounter = 0;
                if (textIndex < currentDialogue.length()) {
                    // Cập nhật displayedText theo từng ký tự
                    gp.playSoundEffect(1);
                    displayedText += currentDialogue.charAt(textIndex);
                    textIndex++;
                }
            }
            if(KeyHandler.enterPressed)
            {
                textIndex = 0;
                displayedText = "";
                if(gameState == GameState.DIALOGUE_STATE )
                {
                    target.dialogueIndex++;
                    KeyHandler.enterPressed = false;
                }
            }
        }else
        {
            target.dialogueIndex--;
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

    public void render(Graphics2D g2, Player player) {
        this.g2 = g2; // Gán đối tượng g2 vào để sử dụng
        if(gameState == GameState.PLAY_STATE)
        {
            drawHPBar();
            drawManaBar();
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
        }
        else if(gameState == GameState.PAUSE_STATE)
        {
            drawHPBar();
            drawManaBar();
            drawPausedScreen();
            drawOptionsScreen();
        }
        if(gameState == GameState.LOSE_STATE)
        {
            drawGameOverScreen();
        }
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

    public void drawGameOverScreen()
    {
        g2.setFont(joystix.deriveFont(Font.PLAIN, 80f));
        g2.setColor(Color.WHITE);
        String text = "You sucked";
        int x = getXforCenteredText(text);
        int y = windowWidth / 2;
        g2.drawString(text , x , y);
    }

    public void drawOptionsScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        // SUB WINDOW

        int frameX = gp.tileSize * 4;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 10;

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
        g2.drawString(String.valueOf(sound.volumePercentage), textX + 200, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX-25, textY);
        }

        // SE
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("SE", textX, textY);
        g2.drawString(String.valueOf(sound.volumePercentage), textX + 200, textY);
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
}

