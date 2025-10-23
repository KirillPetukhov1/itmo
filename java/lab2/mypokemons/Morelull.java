package mypokemons;

import ru.ifmo.se.pokemon.*;
import statusmoves.*;

public class Morelull extends Pokemon {
    public Morelull(String name) {
        super(name, 25);
        super.setType(Type.GRASS, Type.FAIRY);
        super.setStats(40, 35, 55, 65, 75, 15);
        super.setMove(new FlashMove(), new ThunderWaveMove(), new ConfuseRayMove());
    }

    protected Morelull(String name, int level) {
        super(name, level);
    }
}
