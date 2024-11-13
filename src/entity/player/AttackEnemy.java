package entity.player;

import map.GameMap;

public class AttackEnemy {
    private GameMap mp;
    public Player player;

    public AttackEnemy(GameMap mp){
        this.mp = mp;
        this.player = mp.player;
    }
    public void damageEnemy(int index){
        if(!mp.enemy[index].isInvincible) {
            mp.enemy[index].currentHP -= player.damage;
            mp.enemy[index].isInvincible = true;
            System.out.println("Hit! Deal " + player.damage + " damage to the enemy!");
        }
    }

    public void damageShooter(int index){
        boolean checkDirection = player.projectile.checkOppositeDirection(mp.enemy[index]);
        boolean checkDistanceX = Math.abs(player.worldX - mp.enemy[index].worldX) <= 64 * 1;
        boolean checkDistanceY = Math.abs(player.worldY - mp.enemy[index].worldY) <= 64 * 1;
        if(checkDirection && (checkDistanceX && checkDistanceY)){
            if(!mp.enemy[index].isInvincible) {
                mp.enemy[index].currentHP -= player.damage;
                mp.enemy[index].isInvincible = true;
                System.out.println("Hit! Deal " + player.damage + " damage to the enemy!");
            }
        }
    }

    public void damageSpectron(int index){

    }
}
