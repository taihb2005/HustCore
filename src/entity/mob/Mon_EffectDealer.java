package entity.mob;

import entity.effect.Effect;
import entity.effect.type.EffectNone;
import entity.effect.type.Slow;
import entity.projectile.Proj_BasicGreenProjectile;
import map.GameMap;

import java.awt.*;

public class Mon_EffectDealer extends Monster{
    public Mon_EffectDealer(GameMap mp, int x, int y) {
        super(mp, x, y);
        width = 64; height = 64;

        setDefault();
    }
    public Mon_EffectDealer(GameMap mp, Effect eff ,int x, int y) {
        super(mp, x, y);
        name = "Effect Dealer";
        width = 64; height = 64;

        setDefault();
        this.effectDealOnTouch = eff;
    }

    public Mon_EffectDealer(GameMap mp, Effect eff, String idName, int x, int y) {
        super(mp, x, y);
        name = "Effect Dealer";
        this.idName = idName;
        width = 64; height = 64;

        setDefault();
        this.effectDealOnTouch = eff;
    }
    private void setDefault()
    {
        projectile = new Proj_BasicGreenProjectile(mp);
        invincibleDuration = 0;
        maxHP = 1;
        currentHP = maxHP;
        strength = 0;
        speed = 0;
        lastSpeed = speed;
        effectDealOnTouch = new Slow(mp.player , 10);
        effectDealByProjectile = new EffectNone(mp.player);

        solidArea1 = new Rectangle(0 , 0 , 0 , 0);
        hitbox = new Rectangle(0 , 0 , 64 , 64);
        //solidArea2 = new Rectangle(0 , 0 , 0 ,0);
        super.setDefaultSolidArea();
    }

    public void update() throws NullPointerException{
        damagePlayer();
    }

    public void render(Graphics2D g2){

    }

    public void dispose(){
        solidArea1 = null;
        solidArea2 = null;
        hitbox = null;
    }

    public void setEffectDealOnTouch(Effect eff){this.effectDealOnTouch = eff;}
}
