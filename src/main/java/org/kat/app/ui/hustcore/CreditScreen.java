package org.kat.app.ui.hustcore;

import org.kat.app.main.GamePanel;
import org.kat.app.main.GameState;
import org.kat.app.main.UI;
import org.kat.app.ui.views.*;
import org.kat.app.util.Tree;

import java.awt.*;

import static org.kat.app.main.GamePanel.*;
import static org.kat.app.main.GamePanel.gameCompleted;
import static org.kat.app.main.GamePanel.levelProgress;
import static org.kat.app.main.GamePanel.previousLevelProgress;
import static org.kat.app.main.GamePanel.sManager;
import static org.kat.app.main.GamePanel.stopMusic;
import static org.kat.app.main.UI.bitcrusher;

public class CreditScreen extends UIScreen {
    private ImageView blackScreen;
    private WrappedTextView endGameText;
    private TextView gameTitleText;
    private WrappedTextView creditText;

    public int scenePhase;
    int counter = 0;
    float alpha = 0f;
    public CreditScreen(String id, Tree<View> viewTree) {
        super(id, viewTree);
    }

    @Override
    protected void onCreate() {
        blackScreen = (ImageView) findViewById("root");
        blackScreen.setAlpha(0f);

        endGameText = (WrappedTextView) findViewById("endGameText");
        gameTitleText = (TextView) findViewById("gameTitleText");
        creditText = (WrappedTextView) findViewById("creditText");

        endGameText.setText("Sau khi con AI trung tâm đã chết\n" +
                "Sinh viên đại học dần trở lại hình dáng ban đầu\n" +
                "Nhân vật chính trở về cuộc sống thường ngày\n" +
                "Biết đâu một ngày nào đó cái ác sẽ xuất hiện\n" +
                "...và cậu sẽ thành người hùng một lần nữa");
        endGameText.getText().setFont(bitcrusher);

        gameTitleText.setText("HUST CORE");

        creditText.setText(
                "Thành viên:\n"
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
                        + "Cảm ơn TrevorPumpkin vì assets game\n"
                        + "Cảm ơn Minh Lê trong tìm âm thanh\n"
                        + "Cảm ơn Nam trong việc làm hiệu ứng\n"
                        + "Cảm ơn Huyền khi đề xuất ý tưởng\n"
                        + "\n\n\n\n\n\n\n"
                        + "Và big shoutout cho những ai xem được đến đây\n"
                        + "\n\n\n\n\n\n\n\n"
                        + "\n\n\n\n"
                        + "Cảm ơn mọi người rất nhiều!"
        );

        endGameText.build();
        creditText.build();

        endGameText.setAlpha(0f);
        gameTitleText.setAlpha(0f);
    }

    public void scene_ending()
    {
        if(scenePhase == 0)
        {
            stopMusic();
            playSE(9);
            scenePhase++;
        }
        if(scenePhase == 1)
        {
            if(counterReached(300))
            {
                scenePhase++;
            }
        }
        if(scenePhase == 2)
        {
            alpha = graduallyAlpha(alpha, 0.005f);

            blackScreen.setAlpha(alpha);

            if(alpha == 1f)
            {
                alpha = 0;
                scenePhase++;
            }
        }
        if(scenePhase == 3)
        {
            alpha = graduallyAlpha(alpha, 0.005f);

            endGameText.setAlpha(alpha);

            if(counterReached(600) && alpha == 1f)
            {
                playMusic(8);
                alpha = 0;
                scenePhase++;
            }
        }
        if(scenePhase == 4)
        {
            alpha = graduallyAlpha(alpha, 0.01f);

            endGameText.setAlpha(0f);
            gameTitleText.setAlpha(alpha);

            if(counterReached(200) && alpha == 1f)
            {
                scenePhase++;
                alpha = 0;
            }
        }
        if(scenePhase == 5)
        {
            alpha = graduallyAlpha(alpha, 0.01f);

            creditText.getText().setAlpha(alpha);

            if(counterReached(240) && alpha == 1f)
            {
                scenePhase++;
                alpha = 0;
            }
        }
        if(scenePhase == 6)
        {
            gameTitleText.setDimensions(gameTitleText.getX(),gameTitleText.getY() - 2,
                    gameTitleText.getWidth(), gameTitleText.getHeight());
            creditText.setDimensions(creditText.getX(),creditText.getY() - 2,
                    creditText.getWidth(), creditText.getHeight());
            System.out.println(counter);
            if(counterReached(2200))
            {
                stopMusic();
                gameCompleted = false;
                levelProgress = 0;
                previousLevelProgress = 0;
                sManager.reset();
                currentLevel.dispose();
                GamePanel.gameState = GameState.MENU;

                UI._UIManager.clearFromScreenStack();
                UI._UIManager.setCurrentScreen("main_menu");
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
    public float graduallyAlpha(float alpha, float grade)
    {
        alpha += grade;
        if(alpha > 1f)
        {
            alpha = 1f;
        }
        return alpha;
    }

    @Override
    public void update(){
        scene_ending();
    }

    @Override
    public void render(Graphics2D g2){
        blackScreen.render(g2);
        endGameText.render(g2);
        gameTitleText.render(g2);
        creditText.render(g2);
    }
}
