package mypokemons;

import ru.ifmo.se.pokemon.*;
import statusmoves.*;
import specialmoves.*;

public final class Shiinotic extends Morelull {
    public Shiinotic(String name) {
        super(name, 25);
        super.setType(Type.GRASS, Type.FAIRY);
        super.setStats(60, 45, 80, 90, 100, 30);
        super.setMove(
                new FlashMove(),
                new ThunderWaveMove(),
                new ConfuseRayMove(),
                new ChargeBeamMove());
    }
}
