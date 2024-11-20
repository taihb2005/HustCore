package entity.items;

import entity.Entity;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.ui;

public class Item extends Entity {
    protected int id;
    protected String name;
    protected String description;
    protected int quantity;
    protected BufferedImage icon;
    public Item(int id, String name, BufferedImage icon) {
        this.id = id;
        this.name = name;
        this.quantity = 1; // Khởi tạo số lượng là 1 khi tạo Item mới
        this.icon = icon;
    }

    public void add(Item[] items) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].id == this.id) {
                items[i].quantity++;
                return;
            }
        }
        // Nếu không tìm thấy, thêm Item mới vào mảng (nếu còn chỗ)
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                items[i] = this;
                return;
            }
        }
    }

    public void use(Entity entity){
    };
    // Các getter và setter nếu cần thiết
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription(){return description;}
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity;}
    public BufferedImage getIcon() { return icon; }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2) {

    }
}
