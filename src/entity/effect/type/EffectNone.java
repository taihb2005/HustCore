package entity.effect.type;

import entity.effect.Effect;
import entity.player.Player;

import java.awt.*;

public class EffectNone extends Effect {
    public EffectNone(Player player) {
        super(player);
        id = 0;
    }

    @Override
    public void render(Graphics2D g2) {

    }

    @Override
    public void update() {

    }
}
