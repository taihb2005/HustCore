package util;

import static main.GamePanel.*;

public class Camera {
    public int offsetX, offsetY;
    // toa do cua camera
    public int x;
    public int y;

    // vi tri chinh giua man hinh cua Camera
    private final int midX;
    private final int midY;


    public Camera(int windowWidth, int windowHeight, int tileSize) {
        this.midX = windowWidth / 2 - tileSize;
        this.midY = windowHeight / 2 - tileSize;
    }

    public void update(int playerX, int playerY, int worldWidth, int worldHeight) {
        // Camera bám theo nhân vật (playerX, playerY) nhưng vẫn giới hạn trong biên của map
        this.x = Math.max(0, Math.min(playerX - windowWidth / 2 + tileSize, worldWidth - windowWidth));
        this.y = Math.max(0, Math.min(playerY - windowHeight / 2 + tileSize, worldHeight - windowHeight));
    }


    // Tính toán vị trí đối tượng trên màn hình dựa trên vị trí trong thế giới
    public int worldToScreenX(int worldX) {
        if (worldX >= midX && worldX <= currentMap.getMapWidth() - midX - 2 * tileSize)
            return midX;
        if (worldX >= currentMap.getMapWidth() - midX - 2 * tileSize)
            return (midX + (worldX - (currentMap.getMapWidth() - midX - 2 * tileSize)));
        return worldX;
    }

    public int worldToScreenY(int worldY) {
        if (worldY >= midY && worldY <= currentMap.getMapWidth() - midY - 2 * tileSize)
            return midY;
        if (worldY >= currentMap.getMapWidth() - midY-2*tileSize)
            return midY + (worldY - (currentMap.getMapWidth() - midY - 2 * tileSize));
        return worldY;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}