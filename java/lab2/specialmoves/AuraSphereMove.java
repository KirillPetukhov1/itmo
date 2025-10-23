package specialmoves;

import ru.ifmo.se.pokemon.*;

public final class AuraSphereMove extends SpecialMove {
    public AuraSphereMove() {
        super(Type.FIGHTING, 80, 100);
    }

    @Override
    protected boolean checkAccuracy(Pokemon att, Pokemon def) {
        return true;
    }

    @Override
    protected String describe() {
        return "выпускает сферу ауры";
    }
}
