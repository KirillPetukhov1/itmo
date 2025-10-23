package statusmoves;

import ru.ifmo.se.pokemon.*;

public final class CalmMindMove extends StatusMove {
    public CalmMindMove() {
        super(Type.PSYCHIC, 0, 100);
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        super.applySelfEffects(p);
        Effect EffectObject = new Effect()
                .stat(Stat.SPECIAL_ATTACK, 1)
                .stat(Stat.SPECIAL_DEFENSE, 1);
        p.addEffect(EffectObject);
    }

    @Override
    protected String describe() {
        return "очищает свой разум";
    }
}
