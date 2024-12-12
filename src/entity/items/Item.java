package entity.items;

import entity.Entity;
import entity.player.Player;
import map.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GamePanel.ui;

public class Item extends Entity implements Cloneable{
    protected int id;
    protected StringBuilder name;
    protected StringBuilder description;
    protected int quantity;
    protected BufferedImage icon;
    public Item(int id, BufferedImage icon) {
        this.id = id;
        this.quantity = 1; // Khởi tạo số lượng là 1 khi tạo Item mới
        this.icon = icon;
    }

    public void add(Item[] items) {
        for (Item item : items) {
            if (item != null && item.id == this.id) {
                item.quantity++;
                return;
            }
        }
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                items[i] = this;
                return;
            }
        }
    }

    public void use(Player player){
    };
    // Các getter và setter nếu cần thiết
    public int getId() { return id; }
    public StringBuilder getName() { return name; }
    public StringBuilder getDescription(){return description;}
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity;}
    public BufferedImage getIcon() { return icon; }


}
