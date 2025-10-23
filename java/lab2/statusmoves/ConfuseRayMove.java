package statusmoves;

import ru.ifmo.se.pokemon.*;

public final class ConfuseRayMove extends StatusMove {
    public ConfuseRayMove() {
        super(Type.GHOST, 0, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        Effect.confuse(p);
    }

    @Override
    protected String describe() {
        return "вызывает луч конфузии";
    }
}
