package util;

import static main.GamePanel.*;

public class Camera {
    public int offsetX, offsetY;
    // toa do cua camera
    public int x;
    public int y;

    // vi tri chinh giua man hinh cua Camera
    private int midX;
    private int midY;


    public Camera(int windowWidth, int windowHeight, int tileSize) {
        this.midX = windowWidth/2 - tileSize;
        this.midY = windowHeight/2 - tileSize;
    }

    public void update(int playerX, int playerY, int worldWidth, int worldHeight) {
        // Camera bám theo nhân vật (playerX, playerY) nhưng vẫn giới hạn trong biên của map
        this.x = Math.max(0, Math.min(playerX - windowWidth / 2, worldWidth - windowWidth));
        this.y = Math.max(0, Math.min(playerY - windowHeight / 2, worldHeight - windowHeight));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Tính toán vị trí đối tượng trên màn hình dựa trên vị trí trong thế giới
    public int worldToScreenX(float worldX) {
        if (worldX >= midX && worldX <= currentMap.getMapWidth() - midX-2*tileSize)
            return midX;
        if (worldX >= currentMap.getMapWidth() - midX-2*tileSize)
            return (int)(midX + (worldX - (currentMap.getMapWidth() - midX-2*tileSize)));
        return (int)worldX;
    }

    public int worldToScreenY(float worldY) {
        if (worldY >= midY && worldY <= currentMap.getMapWidth() - midY-2*tileSize)
            return midY;
        if (worldY >= currentMap.getMapWidth() - midY-2*tileSize)
            return (int)(midY + (worldY - (currentMap.getMapWidth() - midY-2*tileSize)));
        return (int)worldY;
    }

    // tinh khoang cach giua doi tuong can ve va toa do cua nhan vat chinh trong man hinh
    public int distanceX(float worldX) {
        if (worldX >= midX && worldX <= currentMap.getMapWidth() - midX-2*tileSize) return (int)(worldX-midX);
        if (worldX > currentMap.getMapWidth() - midX-2*tileSize) return (currentMap.getMapWidth() - 2*tileSize - 2* midX);
        return 0;
    }
    public int distanceY(float worldY) {
        if (worldY >= midY && worldY <= currentMap.getMapWidth() - midY-2*tileSize) return (int)(worldY-midY);
        if (worldY > currentMap.getMapWidth() - midX-2*tileSize) return (currentMap.getMapWidth() - 2*tileSize - 2* midY);
        return 0;
    }
}
