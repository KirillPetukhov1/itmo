package statusmoves;

import ru.ifmo.se.pokemon.*;

public final class ThunderWaveMove extends StatusMove {
    public ThunderWaveMove() {
        super(Type.ELECTRIC, 0, 90);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        Effect.paralyze(p);
    }

    @Override
    protected String describe() {
        return "вызывает громовую волну";
    }
}
