package mypokemons;

import ru.ifmo.se.pokemon.*;
import statusmoves.*;
import specialmoves.*;

public class Togepi extends Pokemon {
    public Togepi(String name) {
        super(name, 1);
        super.setType(Type.FAIRY);
        super.setStats(35, 20, 65, 40, 65, 20);
        super.setMove(new DreamEaterMove(this), new SwaggerMove());
    }

    protected Togepi(String name, int level) {
        super(name, level);
    }
}
