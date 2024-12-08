package level.progress.level02;

import entity.Entity;
import level.Level;
import level.EventHandler;
import main.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;


public class EventHandler02 extends EventHandler {
    private int enemiesDefeated = 0;
    private final String correctPassword = "0156";
    private boolean showPasswordInput = false;
    private String enteredPassword = "";
    private final int[] reward = {0, 1, 5, 6};
    private int rewardIndex = 0;
    public Graphics2D g2;

    public EventHandler02(Level lvl) {
        super(lvl);
    }

    public void onEnemyDefeated() {
        for (Entity e : lvl.map.enemy) {
            if (e != null && e.canbeDestroyed) {
                enemiesDefeated++;
                System.out.println("Enemy defeated: " + enemiesDefeated);

                if (enemiesDefeated % 5 == 0 && rewardIndex < reward.length) {
                    int digit = reward[rewardIndex];
                    eventMaster.dialogues[0][0] = "Chữ số bạn nhận được: " + digit;
                    eventMaster.startDialogue(eventMaster, 0);
                    rewardIndex++;
                }
            }
        }
    }

    public void update() {
        onEnemyDefeated();
        if (enemiesDefeated == 6 && !showPasswordInput) {
            showPasswordInput = true;
        }
        draw(g2);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        if (showPasswordInput) {
            drawPasswordInputBox();
        }
    }

    private void drawPasswordInputBox() {
        int x = 100;
        int y = 100;
        int width = 100;
        int height = 100;

        drawSubWindow(x, y, width, height);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        g2.drawString("Nhập mật khẩu:", x + 20, y + 50);

        String maskedPassword = "*".repeat(enteredPassword.length());
        g2.drawString(maskedPassword, x + 20, y + 100);

        g2.setFont(new Font("Arial", Font.ITALIC, 18));
        g2.drawString("Nhấn Enter để xác nhận", x + 20, y + 150);
    }

    public void handleKeyPress(KeyEvent e) {
        if (showPasswordInput) {
            int key = e.getKeyCode();

            if ((key >= KeyEvent.VK_0 && key <= KeyEvent.VK_9)) {
                enteredPassword += e.getKeyChar();
            } else if (key == KeyEvent.VK_BACK_SPACE && enteredPassword.length() > 0) {
                enteredPassword = enteredPassword.substring(0, enteredPassword.length() - 1);
            } else if (key == KeyEvent.VK_ENTER) {
                if (enteredPassword.equals(correctPassword)) {
                    System.out.println("Mật khẩu đúng! Qua màn!");
                    showPasswordInput = false;
                } else {
                    System.out.println("Mật khẩu sai. Hãy thử lại!");
                    enteredPassword = "";
                }
            }
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 178);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x, y, width, height, 25, 25);
    }

}
