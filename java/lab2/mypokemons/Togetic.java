package mypokemons;

import ru.ifmo.se.pokemon.*;
import statusmoves.*;
import specialmoves.*;

public class Togetic extends Togepi {
    public Togetic(String name) {
        super(name, 1);
        super.setType(Type.FAIRY);
        super.setStats(55, 40, 85, 80, 105, 40);
        super.setMove(new DreamEaterMove(this), new SwaggerMove(), new GrowlMove());
    }

    protected Togetic(String name, int level) {
        super(name, level);
    }
}
