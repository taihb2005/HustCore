package main;

import entity.Entity;
import ui.manager.TitleScreen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


import static main.GamePanel.*;
import static main.KeyHandler.*;

public class UI {
    private GamePanel gp;
    public Graphics2D g2;
    public static Font joystix;
    public static Font maru;
    public static Font bitcrusher;


    StringBuilder currentDialogue = new StringBuilder();  // Dòng hội thoại hiện tại đầy đủ
    String displayedText = "";    // Dòng hội thoại đang được hiển thị dần
    int textIndex = 0;            // Chỉ số của ký tự đang được hiển thị
    double textSpeed = 0.1;       // Tốc độ hiển thị từng ký tự (càng nhỏ càng nhanh)
    int frameCounter = 0;         // Đếm số frame để điều khiển tốc độ hiển thị
    int subState = 0;
    boolean isInventoryOpen = false;
    int inventoryX = tileSize / 4;
    int inventoryY = tileSize / 2 + 80;
    int inventoryWidth = tileSize * 2 - 18;
    int inventoryHeight = tileSize * 6 + 32;
    int slotX = inventoryX + 15;
    int slotWidth = 50;
    int slotHeight = 50;
    public int selectedOption = -1;
    public final int correctAnswer = 3;
    private BufferedImage gameOverBackground;
    Color checkPassword = new Color(0 , 0 , 0);
    String maskedPassword;

    int selectedSlot = -1;

    public int commandNum = 0;


    public Entity target;

    private static BufferedImage hpFrame, manaFrame, boss_hpFrame;
    public static BufferedImage titleBackground;
    private static BufferedImage quizImage;

    public Entity npc;
    public UI(GamePanel gp) {
        this.gp = gp;
        try {
            InputStream is1 = getClass().getResourceAsStream("/font/joystix monospace.otf");
            InputStream is2 = getClass().getResourceAsStream("/font/MaruMonica.ttf");
            InputStream is3 = getClass().getResourceAsStream("/font/bitcrusher.otf");
            joystix = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(is1));
            maru= Font.createFont(Font.TRUETYPE_FONT , Objects.requireNonNull(is2));
            bitcrusher = Font.createFont(Font.TRUETYPE_FONT , Objects.requireNonNull(is3));
        } catch (FontFormatException | IOException | NullPointerException e) {
            e.printStackTrace();
        }

        try {
            hpFrame = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/ui/hpFrame.png")));
            manaFrame = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/ui/manaFrame.png")));
            titleBackground = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/ui/Background.png")));
            gameOverBackground = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/ui/robotInvasion.png")));
            quizImage  = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/ui/quiz.png")));
            boss_hpFrame = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/ui/boss_hpFrame.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void startDialogue(String dialogue) {
        currentDialogue = new StringBuilder(dialogue);
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

        x += tileSize - 10;
        y += tileSize;
        for (String line : displayedText.split("\n")) { // splits dialogue until "\n" as a line
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0,0,0, 178); //BLACK
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255,255,255); // WHITE
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x, y, width, height, 25, 25);
    }
    // Hàm vẽ thử

    public void drawTitleScreen(){
        g2.drawImage(titleBackground,0, 0, windowWidth, windowHeight, null);

        //Title name
        g2.setFont(joystix.deriveFont(Font.PLAIN, 80f));
        String text = "HUST Core";
        int x = getXForCenteredText(text);
        int y = windowHeight / 4;

        //Shadow
        g2.setColor(Color.black);
        g2.drawString(text,x+5,y+5);

        //Colour
        g2.setColor(Color.white);
        g2.drawString(text , x , y );

        //Draw Picture

        //Menu
        g2.setFont(joystix.deriveFont(Font.PLAIN, 30f));

        text = "Bắt đầu";
        x = getXForCenteredText(text);
        y = windowHeight*3/5 - tileSize;
        g2.drawString(text, x, y - 5);
        int length    = (int)g2.getFontMetrics().getStringBounds(text , g2).getWidth();
        g2.drawRoundRect(x - tileSize / 2, y - tileSize, length + 40 , tileSize + 10, 30, 30);
        if(commandNum == 0){
            g2.drawString(">",x- tileSize, y);
        }

        text = "Cài đặt";
        y = windowHeight*7/10 + 20 - tileSize;
        g2.drawString(text, x, y - 8);
        g2.drawRoundRect(x - tileSize / 2, y - tileSize, length + 40, tileSize + 10, 30, 30);
        if(commandNum == 1){
            g2.drawString(">",x- tileSize, y);
        }

        text = "Thoát";
        //x = getXForCenteredText(text);
        y = windowHeight*4/5 + 40 - tileSize;
        g2.drawString(text, x + 20, y - 8);
        g2.drawRoundRect(x - tileSize / 2, y - tileSize, length + 40, tileSize + 10, 30, 30);
        if(commandNum == 2){
            g2.drawString(">",x- tileSize, y);
        }
    }

    private int dotCount = 0;
    private int tick = 0;

    public void drawLoadingScreen(){
        g2.drawImage(titleBackground, 0, 0, windowWidth, windowHeight, null);

        tick++;
        if (tick % 30 == 0) {
            dotCount = (dotCount + 1) % 4;
        }

        String dots = ".".repeat(dotCount);
        String text = "Đang tải" + dots;

        g2.setFont(joystix.deriveFont(Font.BOLD, 32f));

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int x = (windowWidth - textWidth) / 2;
        int y = (windowHeight - textHeight) / 2 + fm.getAscent();

        // Vẽ bóng (shadow)
        g2.setColor(new Color(0, 0, 0, 150)); // màu đen mờ
        g2.drawString(text, x + 4, y + 4);

        // Vẽ chữ chính
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
    }



    private void drawPausedScreen()
    {
        g2.setColor(new Color(0 , 0 , 0 , 100));
        g2.fillRoundRect(0, 0, windowWidth, windowHeight , 0 , 0);
        g2.setFont(joystix.deriveFont(Font.PLAIN, 80f));
        g2.setColor(Color.WHITE);
        String text = "";
        int x = getXForCenteredText(text);
        int y = windowHeight / 2;
        g2.drawString(text , x , y );
    }

    public int getXForCenteredText(String text)
    {
        int length = (int)g2.getFontMetrics().getStringBounds(text , g2).getWidth();
        return windowWidth / 2 - length / 2;
    }

    public void drawHPBarForBoss() {
        if(currentMap != null && currentMap.boss != null) {
            int fullHPWidth = 205;  // Chiều dài tối đa của thanh HP
            int hpBarHeight = 12;   // Chiều cao của thanh HP
            int x = windowWidth - 250;
            int y = windowHeight - 92;
            int currentHPWidth;
            try {
                currentHPWidth = (int) ((double) currentMap.boss.currentHP / currentMap.boss.maxHP * fullHPWidth);
            } catch (NullPointerException e) {
                currentHPWidth = 0;
            }
            // Vẽ nền (màu xám) cho thanh HP
            g2.drawImage(boss_hpFrame, x, y, 242, 36, null);
            g2.setFont(joystix.deriveFont(Font.PLAIN, 19f));
            g2.drawString("AI đầu não", x, y - 8);

            // Vẽ thanh HP hiện tại (màu đỏ)
            g2.setColor(new Color(255, 0, 255));
            g2.fillRect(x + 209 - currentHPWidth, y + 12, currentHPWidth, hpBarHeight);
        }
    }

    public void drawHPBar() {
        if(currentMap != null) {
            int fullHPWidth = 178;  // Chiều dài tối đa của thanh HP
            int hpBarHeight = 12;   // Chiều cao của thanh HP
            int x = 40;
            int y = windowHeight - 80;
            int currentHPWidth;
            try {
                currentHPWidth = (int) ((double) currentMap.player.currentHP / currentMap.player.maxHP * fullHPWidth);
            } catch (NullPointerException e) {
                currentHPWidth = 0;
            }
            // Vẽ nền (màu xám) cho thanh HP
            g2.drawImage(hpFrame, x - 31, y - 10, 213, 32, null);

            // Vẽ thanh HP hiện tại (màu đỏ)
            g2.setColor(Color.RED);
            g2.fillRect(x, y, currentHPWidth, hpBarHeight);
        }
    }

    public void drawManaBar() {
        if(currentMap != null) {
            int fullManaWidth = 66;  // Chiều dài tối đa của thanh HP
            int ManaBarHeight = 12;   // Chiều cao của thanh HP
            int x = 40;
            int y = windowHeight - 40;
            int currentHPWidth;
            try {
                currentHPWidth = (int) ((double) currentMap.player.currentMana / currentMap.player.maxMana * fullManaWidth);
            } catch (NullPointerException e) {
                currentHPWidth = 0;
            }
            // Vẽ nền (màu xám) cho thanh Mana
            g2.drawImage(manaFrame, x - 31, y - 10, 101, 32, null);

            // Vẽ thanh HP hiện tại (màu xanh)
            g2.setColor(Color.BLUE);
            g2.fillRect(x, y, currentHPWidth, ManaBarHeight);
        }
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
        g2.setFont(joystix.deriveFont(Font.BOLD, 30f));

        String text = "MÀN HÌNH CHÍNH";
        int length = (int)g2.getFontMetrics().getStringBounds(text , g2).getWidth();
        int x = getXForCenteredText(text);

        g2.setColor(Color.WHITE);
        text = "THUA";
        g2.setFont(g2.getFont().deriveFont(Font.BOLD , 50f));

        g2.setColor(Color.BLACK);
        g2.drawString(text , getXForCenteredText(text) - 2 ,windowHeight / 4 + 5);
        g2.setColor(Color.WHITE);
        g2.drawString(text , getXForCenteredText(text) - 7 ,windowHeight / 4);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD , 30f));
        text = "THỬ LẠI";
        int y = windowHeight / 2;
        g2.drawString(text, x + tileSize * 2, y-5);
        //BOUND
        g2.setColor(Color.darkGray);
        g2.drawRoundRect(x - tileSize / 2, y - tileSize, length + tileSize, tileSize + 10, 40, 40);

        if(commandNum == 0) {
            g2.setColor(Color.white);
            g2.drawString(">", x - tileSize - 10, y);
        }

        g2.setColor(Color.WHITE);

        text = "MÀN HÌNH CHÍNH";
        y += tileSize + 20;
        g2.drawString(text, x, y);
        //BOUND
            g2.setColor(Color.darkGray);
        g2.drawRoundRect(x - tileSize / 2, y - tileSize, length + tileSize, tileSize + 10, 40, 40);
        if(commandNum == 1) {
            g2.setColor(Color.WHITE);
            g2.drawString(">", x - tileSize - 10, y);
        }

        g2.setColor(Color.WHITE);
        text = "THOÁT";
        y += tileSize + 20;
        g2.drawString(text, x + tileSize * 3 - 20, y);
        //BOUND
            g2.setColor(Color.darkGray);
        g2.drawRoundRect(x - tileSize / 2, y - tileSize, length + tileSize, tileSize + 10, 40, 40);
        if(commandNum == 2) {
            g2.setColor(Color.white);
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

        if (subState == 0) {
            options_top(frameX, frameY);
        }
    }
    public void options_top(int frameX, int frameY) {
        int textX, textY;
        // Thử với Consolas
        g2.setFont(joystix.deriveFont(Font.PLAIN, 19));
        // TITLE
        String text = "TÙY CHỌN";
        textX = getXForCenteredText(text);
        textY = frameY + tileSize;
        g2.drawString(text, textX, textY);

        // MUSIC
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("NHẠC", textX, textY);
        drawSubWindow(textX+185, textY-25 ,tileSize*3/2, tileSize);
        g2.drawString(String.valueOf(music.volumePercentage), textX + 202, textY + 5);
        g2.drawString("-",textX +150, textY);
        g2.drawString("+",textX+280, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX-25, textY);
        }

        // SE
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("SFX", textX, textY);
        drawSubWindow(textX+185, textY-25 ,tileSize*3/2, tileSize);
        g2.drawString(String.valueOf(se.volumePercentage), textX + 202, textY + 5);
        g2.drawString("-",textX +150, textY);
        g2.drawString("+",textX+280, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX-25, textY);
        }

        // RETRY
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("THỬ LẠI", textX, textY);
        if (commandNum == 2) {
            g2.drawString(">", textX-25, textY);
        }

        // EXIT
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("THOÁT", textX, textY);
        if (commandNum == 3) {
            g2.drawString(">", textX-25, textY);
        }
    }
    public void drawInventory() throws NullPointerException {
        if(KeyHandler.keyEpressed){
            KeyHandler.keyEpressed = false;
            isInventoryOpen = !isInventoryOpen;
        }
        if (isInventoryOpen && inventoryWidth < (tileSize * 2 - 18)) {
            inventoryWidth += 10;
            inventoryX += 10;
        }

        if (!isInventoryOpen && inventoryWidth > -64) {
            inventoryWidth -= 10;
            inventoryX -= 10;
        }

        if (isInventoryOpen && slotWidth < 50) {
            slotWidth += 10;
            slotX += 10;
        }

        if (!isInventoryOpen && slotWidth > -64) {
            slotWidth -= 10;
            slotX -= 10;
        }

        drawSubWindow(inventoryX, inventoryY, inventoryWidth, inventoryHeight);

        int slotY = inventoryY + 12;

        int slotSize = tileSize / 4;

        if (KeyHandler.key1pressed) {
            selectedSlot = 0;
            KeyHandler.key1pressed = false;
        } else if (KeyHandler.key2pressed) {
            selectedSlot = 1;
            KeyHandler.key2pressed = false;
        } else if (KeyHandler.key3pressed) {
            selectedSlot = 2;
            KeyHandler.key3pressed = false;
        } else if (KeyHandler.key4pressed) {
            selectedSlot = 3;
            KeyHandler.key4pressed = false;
        } else if (KeyHandler.key5pressed) {
            selectedSlot = 4;
            KeyHandler.key5pressed = false;
        }

        g2.setFont(joystix.deriveFont(Font.PLAIN, 15));

        for (int i = 0; i < 5; i++) {
            int currentSlotY = slotY + i * (slotSize + 50);
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRoundRect(slotX, currentSlotY, slotWidth, slotHeight, 10, 10);

            g2.setColor(new Color(255, 255, 255));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(slotX, currentSlotY, slotWidth, slotHeight, 10, 10);
        }
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
        this.g2 = g2;
            if(gameState == GameState.LOADING_STATE){
                drawLoadingScreen();
            } else if(gameState == GameState.PLAY_STATE)
            {
                drawHPBar();
                drawManaBar();
                drawInventory();
                drawHPBarForBoss();
            }else if (gameState == GameState.MENU_STATE) {
                drawTitleScreen();
            } else if (gameState == GameState.DIALOGUE_STATE) {
                drawDialogueScreen();
                drawHPBar();
                drawManaBar();
                drawInventory();
                drawHPBarForBoss();
            }
             else if(gameState == GameState.PASSWORD_STATE){
                drawPasswordInputBox();
                drawHPBar();
                drawManaBar();
                drawInventory();
            } else
            if (gameState == GameState.LEVELUP_STATE) {
                drawDialogueScreen();
            } else if (gameState == GameState.PAUSE_STATE) {
                drawHPBar();
                drawManaBar();
                drawPausedScreen();
                drawOptionsScreen();
                drawInventory();
                drawHPBarForBoss();
            }
            if (gameState == GameState.LOSE_STATE) {
                currentMap.dispose();
                drawGameOverScreen();
            }
            if (gameState == GameState.SETTING_STATE) {
                drawSettingScreen();
            }
            if (gameState == GameState.QUIZ_STATE) {
                drawQuiz();
            }

    }

    private void drawQuiz() {
        int frameX = 0;
        int frameY = 0;
        int frameWidth = 978;
        int frameHeight = 514;
        String message = "";
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        g2.drawImage(quizImage,frameX, frameY, 770, 578, null);
        if (selectedOption > -1) {
            if (selectedOption == 4) {
                message = "Chúc mừng bạn đã trả lời đúng! Nhấn Enter để tiếp tục.";
            } else if (selectedOption == 5) {
                message = "Rất tiếc, bạn đã trả lời sai. Nhấn Enter để tiếp tục.";
            } else {
                message = "Bạn chọn đáp án " + (char) ('A' + selectedOption) + ". Nhấn Enter để xem kết quả";
            }
            drawMessage(message);
        }

    }

    public void drawMessage(String message) {
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        g2.setColor(Color.BLACK);

        g2.drawString(message , 30, 550);
    }
    public void update(){
        if(gameState == GameState.PASSWORD_STATE){
            handlePasswordPressed();
            maskedPassword = "*".repeat(currentLevel.enteredPassword.length());
        }
    }

    public void drawSettingScreen(){
        g2.drawImage(titleBackground,0, 0, windowWidth, windowHeight, null);
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        // SUB WINDOW

        int frameX = tileSize * 4;
        int frameY = tileSize;
        int frameWidth = tileSize * 8;
        int frameHeight = tileSize * 10;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch(subState) {
            case 0: setting_top(frameX, frameY); break;
            case 1: control(frameX, frameY); break;
        }
    }
    public void setting_top(int frameX, int frameY) {
        int textX, textY;
        // Thử với Consolas
        g2.setFont(joystix.deriveFont(Font.PLAIN, 19));
        // TITLE
        String text = "TÙY CHỌN";
        textX = getXForCenteredText(text);
        textY = frameY + tileSize;
        g2.drawString(text, textX, textY);
        // MUSIC
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("NHẠC", textX, textY);
        drawSubWindow(textX+185, textY-25 ,tileSize*3/2, tileSize);
        g2.drawString(String.valueOf(music.volumePercentage), textX + 202, textY + 5);
        g2.drawString("-",textX +150, textY);
        g2.drawString("+",textX+280, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX-25, textY);
        }

        // SE
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("HIỆU ỨNG", textX, textY);
        drawSubWindow(textX+185, textY-25 ,tileSize*3/2, tileSize);
        g2.drawString(String.valueOf(se.volumePercentage), textX + 202, textY + 5);
        g2.drawString("-",textX +150, textY);
        g2.drawString("+",textX+280, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX-25, textY);
        }

        //CONTROL
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("ĐIỀU KHIỂN", textX, textY);
        if (commandNum == 2) {
            g2.drawString(">", textX-25, textY);
        }

        // EXIT
        textX = frameX + tileSize;
        textY += tileSize*2;
        g2.drawString("Quay lại", textX, textY);
        if (commandNum == 3) {
            g2.drawString(">", textX-25, textY);
        }
    }
    public void control(int frameX, int frameY){
        int textX, textY;
        g2.setFont(joystix.deriveFont(Font.PLAIN, 19));
        //TITLE
        String text = "TÙY CHỌN";
        textX = getXForCenteredText(text);
        textY = frameY + tileSize;
        g2.drawString(text, textX, textY);

        //BANG HO TRO
        textX = frameX + tileSize/2;
        textY += tileSize;

        g2.drawString("DI CHUYỂN", textX, textY);
        textY+=tileSize;

        g2.drawString("BẮN", textX, textY);
        textY+=tileSize;

        g2.drawString("DÙNG VẬT PHẨM", textX, textY);
        textY+=tileSize;

        g2.drawString("DỪNG/QUAY LẠI", textX, textY);
        textY+=tileSize;

        g2.drawString("QUA HỘI THOẠI", textX, textY);

        //THONG TIN
        textX = frameX + tileSize*11/2;
        textY = frameY + tileSize*2;

        g2.drawString("W A S D", textX, textY);
        textY+=tileSize;

        g2.drawString("ENTER", textX, textY);
        textY+=tileSize;

        g2.drawString("1 2 3 4", textX, textY);
        textY+=tileSize;

        g2.drawString("ESC", textX, textY);
        textY+=tileSize;

        g2.drawString("ENTER", textX, textY);
    }
    private void drawPasswordInputBox() {
        int x = 100;
        int y = 100;
        int width = gp.getWidth() - tileSize * 4;
        int height = 200;

        drawSubWindow(x, y, width, height);

        g2.setColor(Color.WHITE);
        g2.setFont(joystix.deriveFont(Font.PLAIN, 20));
        g2.drawString("Nhập mật khẩu:", x + 20, y + 80);
        drawSubWindow(x+300,y+55,200,30);

        if(currentLevel.enteredPassword.isEmpty()) checkPassword = Color.white;
        g2.setColor(checkPassword);
        maskedPassword = "*".repeat(currentLevel.enteredPassword.length());
        g2.drawString(maskedPassword, x + 310, y + 75);

        g2.setColor(Color.WHITE);
        g2.setFont(joystix.deriveFont(Font.PLAIN, 20));
        g2.drawString("Nhấn Enter để xác nhận", x + 20, y + 150);
    }
    public void handlePasswordPressed(){
        String charPressed = "";
        if(enterPressed){
            enterPressed = false;
            if (currentLevel.enteredPassword.equals(currentLevel.correctPassword)) {
                currentLevel.levelFinished = true;
                checkPassword = Color.GREEN;
            } else {
                checkPassword = Color.RED;
                currentLevel.enteredPassword = "";
            }
        }
        if(key0pressed) {charPressed = "0"; key0pressed = false;} else
        if(key1pressed) {charPressed = "1"; key1pressed = false;} else
        if(key2pressed) {charPressed = "2"; key2pressed = false;} else
        if(key3pressed) {charPressed = "3"; key3pressed = false;} else
        if(key4pressed) {charPressed = "4"; key4pressed = false;} else
        if(key5pressed) {charPressed = "5"; key5pressed = false;} else
        if(key6pressed) {charPressed = "6"; key6pressed = false;} else
        if(key7pressed) {charPressed = "7"; key7pressed = false;} else
        if(key8pressed) {charPressed = "8"; key8pressed = false;} else
        if(key9pressed) {charPressed = "9"; key9pressed = false;}

        if(currentLevel.enteredPassword.length() < 12) currentLevel.enteredPassword += charPressed;

        if (keyBackspacepressed) {
            keyBackspacepressed = false;
            if (!currentLevel.enteredPassword.isEmpty()) {
                currentLevel.enteredPassword = currentLevel.enteredPassword.substring(0, currentLevel.enteredPassword.length() - 1);
            }
        }
        if(keyEscpressed) GamePanel.gameState = GameState.PLAY_STATE;
    }
}