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
        boolean checkDistanceX = Math.abs(player.worldX - mp.enemy[index].worldX) <= 64;
        boolean checkDistanceY = Math.abs(player.worldY - mp.enemy[index].worldY) <= 64;
        if(checkDirection && (checkDistanceX && checkDistanceY)){
            if(!mp.enemy[index].isInvincible) {
                mp.enemy[index].currentHP -= player.damage;
                mp.enemy[index].isInvincible = true;
                System.out.println("Hit! Deal " + player.damage + " damage to the enemy!");
            }
        }
    }

    public void damageGuardian(int index){
        if(!mp.enemy[index].isInvincible) {
            if(!mp.enemy[index].getAggro){
                mp.enemy[index].getAggro = true;
                mp.enemy[index].onPath = true;
                mp.enemy[index].speed = 2;
            }
            mp.enemy[index].currentHP -= player.damage;
            mp.enemy[index].isInvincible = true;
            System.out.println("Hit! Deal " + player.damage + " damage to the enemy!");
        }
    }

    public void damageCyborgon(int index){
        if(!mp.enemy[index].isInvincible) {
            mp.enemy[index].canChangeState = true;
            mp.enemy[index].getAggro = true;
            mp.enemy[index].onPath = true;
            mp.enemy[index].currentHP -= player.damage;
            mp.enemy[index].isInvincible = true;
            System.out.println("Hit! Deal " + player.damage + " damage to the enemy!");
        }
    }

    public void damageSpectron(int index){

    }
}
