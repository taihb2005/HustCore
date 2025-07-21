package org.kat.app.entity.items;

import org.kat.app.entity.Entity;
import org.kat.app.entity.player.Player;

import java.awt.image.BufferedImage;

public class Item extends Entity{
    protected int id;
    protected StringBuilder name;
    protected StringBuilder description;
    protected int quantity;
    protected BufferedImage icon;
    public Item(int id, BufferedImage icon) {
        this.id = id;
        this.quantity = 1;
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
    public int getId() { return id; }
    public StringBuilder getName() { return name; }
    public StringBuilder getDescription(){return description;}
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity;}
    public BufferedImage getIcon() { return icon; }
}
