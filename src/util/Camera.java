package util;

import static main.GamePanel.*;

public class Camera {

    // toa do cua camera
    private int x;
    private int y;

    // vi tri chinh giua man hinh cua Camera
    private int midX; // Chiều rộng màn hình
    private int midY; // Chiều cao màn hình


    public Camera(int windowWidth, int windowHeight, int tileSize) {
        this.midX = windowWidth/2 - tileSize;
        this.midY = windowHeight/2 - tileSize;
    }

    public void update(int playerX, int playerY, int worldWidth, int worldHeight) {
        // Camera bám theo nhân vật (playerX, playerY)
        this.x = playerX - midX / 2;
        this.y = playerY - midY / 2;

        // Giới hạn camera không vượt ra ngoài biên của bản đồ
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > worldWidth - midX) x = worldWidth - midX;
        if (y > worldHeight - midY) y = worldHeight - midY;
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
