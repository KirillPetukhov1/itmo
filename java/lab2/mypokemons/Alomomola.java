package mypokemons;

import ru.ifmo.se.pokemon.*;
import specialmoves.*;
import statusmoves.*;

public final class Alomomola extends Pokemon {
    protected double maxHP;

    public Alomomola(String name) {
        super(name, 5);
        super.setType(Type.WATER);
        super.setStats(165, 75, 80, 40, 45, 65);
        maxHP = this.getHP();
        super.setMove(
                new ShadowBallMove(),
                new CalmMindMove(),
                new PlayNiceMove(),
                new AquaRingMove(maxHP));
    }
}
