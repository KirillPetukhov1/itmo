package specialmoves;

import ru.ifmo.se.pokemon.*;

public final class ChargeBeamMove extends SpecialMove {
    public ChargeBeamMove() {
        super(Type.ELECTRIC, 50, 90);
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        super.applySelfEffects(p);
        Effect EffectObject = new Effect().stat(Stat.SPECIAL_ATTACK, 1).chance(0.7);
        p.addEffect(EffectObject);
    }

    @Override
    protected String describe() {
        return "выпускает заряженный луч";
    }
}