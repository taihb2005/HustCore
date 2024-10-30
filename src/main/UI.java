package main;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import static main.GamePanel.*;

public class UI {
    private final GamePanel gp;
    Graphics2D g2;
    Font joystix;
    String currentDialogue = "";  // Dòng hội thoại hiện tại đầy đủ
    String displayedText = "";    // Dòng hội thoại đang được hiển thị dần
    int textIndex = 0;            // Chỉ số của ký tự đang được hiển thị
    double textSpeed = 0.1;       // Tốc độ hiển thị từng ký tự (càng nhỏ càng nhanh)
    int frameCounter = 0;         // Đếm số frame để điều khiển tốc độ hiển thị

    public UI(GamePanel gp) {
        this.gp = gp;
        try {
            InputStream is = getClass().getResourceAsStream("/font/joystix monospace.otf");
            assert is != null;
            joystix = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
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
        int x = tileSize*2;
        int y = tileSize/2;
        int width = gp.getWidth() - tileSize * 4;
        int height = tileSize*4;

        drawSubWindow(x, y, width, height);

        frameCounter++;
        if (frameCounter > textSpeed) {
            frameCounter = 0;
            if (textIndex < currentDialogue.length()) {
                // Cập nhật displayedText theo từng ký tự
                displayedText += currentDialogue.charAt(textIndex);
                textIndex++;
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
    public void draw()
    {
        if(gp.gameState == GameState.PLAY_STATE)
        {

        }
        if(gp.gameState == GameState.PAUSE_STATE)
        {
            drawPausedScreen();
        }
    }

    private void drawPausedScreen()
    {
        g2.setColor(new Color(0 , 0 , 0 , 100));
        g2.fillRoundRect(0, 0, windowWidth, windowHeight , 0 , 0);
        g2.setFont(joystix.deriveFont(Font.PLAIN, 80f));
        g2.setColor(Color.WHITE);
        String text = "PAUSED";
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

    public void render(Graphics2D g2) {
        this.g2 = g2; // Gán đối tượng g2 vào để sử dụng
        if(gp.gameState == GameState.PLAY_STATE)
        {

        }
        if(gp.gameState == GameState.PAUSE_STATE)
        {
            drawPausedScreen();
        }
    }

    public Font getFont(){return joystix;};

}
