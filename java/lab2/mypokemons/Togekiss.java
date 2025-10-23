package mypokemons;

import ru.ifmo.se.pokemon.*;
import statusmoves.*;
import specialmoves.*;

public final class Togekiss extends Togetic {
    public Togekiss(String name) {
        super(name, 1);
        super.setType(Type.FAIRY);
        super.setStats(85, 50, 95, 120, 115, 80);
        super.setMove(
                new DreamEaterMove(this),
                new SwaggerMove(),
                new GrowlMove(),
                new AuraSphereMove());
    }
}
