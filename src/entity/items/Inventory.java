package entity.items;
import entity.Item;

import java.util.ArrayList;
// Khong dung class nay
public class Inventory {
    public ArrayList<Item> items = new ArrayList<>();
    public int size() {
        return items.size();
    }
    public Inventory() {
    }
}
