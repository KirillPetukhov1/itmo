package statusmoves;

import ru.ifmo.se.pokemon.*;

public final class FlashMove extends StatusMove {
    public FlashMove() {
        super(Type.NORMAL, 0, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        Effect EffectObject = new Effect().stat(Stat.ACCURACY, -1);
        p.addEffect(EffectObject);
    }

    @Override
    protected String describe() {
        return "делает вспышку";
    }
}
