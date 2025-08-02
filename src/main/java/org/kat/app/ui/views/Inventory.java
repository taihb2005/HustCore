package org.kat.app.ui.views;

import org.kat.app.entity.items.Item;
import org.kat.app.ui.Updatable;

import java.awt.*;

import static org.kat.app.main.GamePanel.tileSize;
import static org.kat.app.main.UI.maru;

public class Inventory extends View implements Updatable {
    private boolean isOpen = false;
    private final SubWindow inventoryContainer;

    private Item[] inventory;
    private int slotX;
    private int slotWidth;
    private int slotHeight;

    private int selectedSlot = 0;

    public Inventory(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        inventoryContainer = new SubWindow(x, y, width, height);
        slotX = x + 15;
        slotWidth = 50;
        slotHeight = 50;
    }

    public void setInventory(Item[] inventory) {
        this.inventory = inventory;
    }

    public void toggle() {
        isOpen = !isOpen;
    }

    @Override
    public void update() {
        if (isOpen && width < 78) {
            width += 10;
            x += 10;
        } else if (!isOpen && width > -64) {
            width -= 10;
            x -= 10;
        }

        if (isOpen && slotWidth < 50) {
            slotWidth += 10;
            slotX += 10;
        } else if (!isOpen && slotWidth > -64) {
            slotWidth -= 10;
            slotX -= 10;
        }

        inventoryContainer.setDimensions(x, y, width, height);
    }

    @Override
    public void render(Graphics2D g2) {
        inventoryContainer.render(g2);

        int slotY = y + 12;
        int slotSize = tileSize / 4;

        for (int i = 0; i < 5; i++) {
            int currentSlotY = slotY + i * (slotSize + 50);
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRoundRect(slotX, currentSlotY, slotWidth, slotHeight, 10, 10);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(slotX, currentSlotY, slotWidth, slotHeight, 10, 10);
        }

        if (inventory != null) {
            g2.setFont(maru.deriveFont(Font.PLAIN, 25));
            for (int i = 0; i < inventory.length; i++) {
                int currentSlotY = slotY + i * (slotSize + 50);
                Item item = inventory[i];
                if (item != null) {
                    g2.drawImage(item.getIcon(), slotX + 8, currentSlotY + 8, 33, 33, null);
                    String quantity = Integer.toString(item.getQuantity());
                    g2.drawString(quantity, slotX + 37, currentSlotY + 46);
                }
            }
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setSelectedSlot(int selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }
}
