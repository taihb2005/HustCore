package level;

import entity.mob.Mon_Cyborgon;
import map.GameMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EventLv02 {
    private final GameMap gameMap;
    private final List<Mon_Cyborgon> enemies; // Danh sách các enemy (Mon_Cyborgon)
    private boolean eventTriggered = false;  // Trạng thái kiểm tra sự kiện đã được kích hoạt

    public EventLv02(GameMap gameMap) {
        this.gameMap = gameMap;
        this.enemies = new ArrayList<>();
    }

    // Sự kiện mở cửa
    public void openDoor(int doorX, int doorY) {
        if (!eventTriggered) {
            System.out.println("Cửa đã mở! Xuất hiện 2 Mon_Cyborgon.");

            // Tạo 2 Mon_Cyborgon tại vị trí gần cửa
            Mon_Cyborgon enemy1 = new Mon_Cyborgon(gameMap, doorX + 50, doorY + 50);
            Mon_Cyborgon enemy2 = new Mon_Cyborgon(gameMap, doorX - 50, doorY + 100);

            // Thêm enemy vào danh sách và bản đồ
            enemies.add(enemy1);
            enemies.add(enemy2);
            gameMap.addObject(enemy1, gameMap.enemies);
            gameMap.addObject(enemy2, gameMap.enemies);

            eventTriggered = true; // Đánh dấu sự kiện đã kích hoạt
        }
    }

    // Cập nhật trạng thái của enemy
    public void update() {
        for (Mon_Cyborgon enemy : enemies) {
            if (!enemy.canbeDestroyed) { // Nếu enemy chưa bị tiêu diệt
                enemy.update();
            }
        }
    }

    // Vẽ enemy lên map
    public void render(Graphics2D g2) {
        for (Mon_Cyborgon enemy : enemies) {
            if (!enemy.canbeDestroyed) { // Nếu enemy chưa bị tiêu diệt
                enemy.render(g2);
            }
        }
    }
}
