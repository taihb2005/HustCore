package main;

import java.awt.*;

import static main.GamePanel.*;
import static main.UI.bitcrusher;
import static main.UI.joystix;

public class Credit {
    GamePanel gp;
    Graphics2D g2;
    public int scenePhase;
    int counter = 0;
    float alpha = 0f;
    int y;
    StringBuilder endCredit;

    //Scene Number
    public final int NA = 0;


    public Credit(GamePanel gp){
        this.gp = gp;
        set();
    }

    private void set(){
        endCredit = new StringBuilder("Nhóm 7 OOP:\n"
                + "Khương Anh Tài (Nhóm trưởng)\n"
                + "Hoàng Ngọc Nam\n"
                + "Nguyễn Văn Sáng\n"
                + "Nguyễn Ngọc Lê Minh\n"
                + "Nguyễn Đức Quý\n"
                + "\n\n\n\n"
                + "Coder:\n"
                + "Khương Anh Tài\n"
                + "Hoàng Ngọc Nam\n"
                + "Nguyễn Văn Sáng\n"
                + "Nguyễn Ngọc Lê Minh\n"
                + "Nguyễn Đức Quý\n"
                + "\n\n\n\n"
                + "Designer cho hình ảnh game:\n"
                + "Khương Anh Tài\n"
                + "Hoàng Ngọc Nam\n"
                + "\n\n\n\n"
                + "Designer cho ui:\n"
                + "Nguyễn Ngọc Lê Minh\n"
                + "Nguyễn Đức Quý\n"
                + "\n\n\n\n"
                + "Level designer:\n"
                + "Nguyễn Văn Sáng\n"
                + "Nguyễn Ngọc Lê Minh\n"
                + "\n\n\n\n"
                + "Âm thanh:\n"
                + "Nguyễn Ngọc Lê Minh\n"
                + "Ý tưởng:\n"
                + "Khương Anh Tài"
                + "\n\n\n\n"
                + "Giám sát:\n"
                + "Khương Anh Tài"
                + "\n\n\n\n\n\n\n\n"
                + "Special thanks to TrevorPumpkin for his amazing assets\n"
                + "Special thanks to Minh for his dedication to sound making\n"
                + "Special thanks to Nam for his effort in creating effects\n"
                + "Special thanks to Nguyen Thi Huyen for suggesting ideas\n"
                + "\n\n\n\n\n\n\n"
                + "Và big shoutout cho những ai xem được đến đây\n"
                + "\n\n\n\n\n\n\n\n"
                + "\n\n\n\n"
                + "Cảm ơn mọi người rất nhiều!");

    }

    public void render(Graphics2D g2)
    {
        this.g2 = g2;
        scene_ending();
    }

    public void scene_ending()
    {
        if(scenePhase == 0)
        {
            //Play the fanfare
            stopMusic();
            playSE(9);
            scenePhase++;
        }
        if(scenePhase == 1)
        {
            //Wait until the sound effect ends
            if(counterReached(300)) // 5 sec delay
            {
                scenePhase++;
            }
        }
        if(scenePhase == 2)
        {
            alpha = graduallyAlpha(alpha, 0.005f);

            drawBlackBackground(alpha);

            if(alpha == 1f)
            {
                alpha = 0;
                scenePhase++;
            }
        }
        if(scenePhase == 3)
        {
            drawBlackBackground(1f);

            alpha = graduallyAlpha(alpha, 0.005f);

            g2.setFont(bitcrusher.deriveFont(Font.PLAIN, 18f));
            String text = "Sau khi con AI trung tâm đã chết\n" +
                    "Sinh viên đại học dần trở lại hình dáng ban đầu\n" +
                    "Nhân vật chính trở về cuộc sống thường ngày\n" +
                    "Biết đâu một ngày nào đó cái ác sẽ xuất hiện\n" +
                    "...và cậu sẽ thành người hùng một lần nữa";

            drawString(alpha, 38f, 180, text, 70);

            if(counterReached(600) && alpha == 1f)
            {
                playMusic(8);
                alpha = 0;
                scenePhase++;
            }
        }
        if(scenePhase == 4)
        {
            drawBlackBackground(1f);

            alpha = graduallyAlpha(alpha, 0.01f);

            drawString(alpha,40f, GamePanel.windowHeight /2, "HUST CORE", 40);

            if(counterReached(200) && alpha == 1f)
            {
                scenePhase++;
                alpha = 0;
            }
        }
        if(scenePhase == 5)
        {
            //First Credits
            drawBlackBackground(1f);

            alpha = graduallyAlpha(alpha, 0.01f);

            y = windowHeight / 2 - 43;
            g2.setFont(bitcrusher.deriveFont(Font.PLAIN , 17f));
            drawString(alpha, 40f,  y, String.valueOf(endCredit), 45);

            if(counterReached(240) && alpha == 1f)
            {
                scenePhase++;
                alpha = 0;
            }
        }
        if(scenePhase == 6)
        {
            drawBlackBackground(1f);
            g2.setFont(bitcrusher.deriveFont(Font.PLAIN , 17f));

            y -= 2;
            drawString(1f, 40f,  y, String.valueOf(endCredit), 45);
            if(counterReached(2200))
            {
                stopMusic();
                gameCompleted = false;
                levelProgress = 0;
                previousLevelProgress = 0;
                sManager.reset();
                GamePanel.gameState = GameState.MENU_STATE;
            }
        }
    }

    public boolean counterReached(int target)
    {
        boolean counterReached = false;
        counter++;
        if(counter > target)
        {
            counterReached = true;
            counter = 0;
        }
        return counterReached;
    }
    public void drawBlackBackground(float alpha)
    {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.black);
        g2.fillRect(0,0, GamePanel.windowWidth, GamePanel.windowHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
    public void drawString(float alpha, float fontSize, int y, String text, int lineHeight)
    {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(fontSize));

        for(String line: text.split("\n"))
        {
            int x = GamePanel.ui.getXforCenteredText(line);
            g2.drawString(line, x, y);
            y += lineHeight;
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
    public float graduallyAlpha(float alpha, float grade)
    {
        alpha += grade;  // after 200 frames alpha becomes 1
        if(alpha > 1f)
        {
            alpha = 1f;
        }
        return alpha;
    }
}
