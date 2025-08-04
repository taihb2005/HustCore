package org.kat.app.entity.player;

import org.kat.app.level.LevelState;
import org.kat.app.main.GameState;
import org.kat.app.main.KeyHandler;

import static org.kat.app.main.GamePanel.currentLevel;
import static org.kat.app.main.GamePanel.gameState;

public class ItemHandler {
    public ItemHandler(){}

    public void useItem(Player player) {
        if (gameState == GameState.PLAY && currentLevel.checkState(LevelState.RUNNING)) {
            if (player.inventory[0] != null) {
                KeyHandler.key1pressed = false;
                player.inventory[0].use(player);
            }
            if (KeyHandler.key2pressed) {
                if (player.inventory[1] != null) {
                    KeyHandler.key2pressed = false;
                    player.inventory[1].use(player);
                }
            }
            if (KeyHandler.key3pressed) {
                if (player.inventory[2] != null) {
                    KeyHandler.key3pressed = false;
                    player.inventory[2].use(player);
                }
            }
            if (KeyHandler.key4pressed) {
                if (player.inventory[3] != null) {
                    KeyHandler.key4pressed = false;
                    player.inventory[3].use(player);
                }
            }
            if (KeyHandler.key5pressed) {
                if (player.inventory[4] != null) {
                    KeyHandler.key5pressed = false;
                    player.inventory[4].use(player);
                }
            }
        }
    }
}
