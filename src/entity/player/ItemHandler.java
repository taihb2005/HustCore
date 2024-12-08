package entity.player;

import main.KeyHandler;

public class ItemHandler {
    public ItemHandler(){}

    public void useItem(Player player){
        if(KeyHandler.key1pressed){
            if(player.inventory[0] != null){
                KeyHandler.key1pressed = false;
                player.inventory[0].use(player);
            }
        }
        if(KeyHandler.key2pressed){
            if(player.inventory[1] != null){
                KeyHandler.key1pressed = false;
                player.inventory[1].use(player);
            }
        }
        if(KeyHandler.key3pressed){
            if(player.inventory[2] != null){
                KeyHandler.key1pressed = false;
                player.inventory[2].use(player);
            }
        }
        if(KeyHandler.key4pressed){
            if(player.inventory[3] != null){
                KeyHandler.key1pressed = false;
                player.inventory[3].use(player);
            }
        }
        if(KeyHandler.key5pressed){
            if(player.inventory[4] != null){
                KeyHandler.key1pressed = false;
                player.inventory[4].use(player);
            }
        }
    }
}
