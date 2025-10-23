package specialmoves;

import ru.ifmo.se.pokemon.*;

public final class ShadowBallMove extends SpecialMove {
    public ShadowBallMove() {
        super(Type.GHOST, 80, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        Effect EffectObject = new Effect().stat(Stat.SPECIAL_DEFENSE, -1).chance(0.2);
        p.addEffect(EffectObject);
    }

    @Override
    protected String describe() {
        return "выпускает теневой шар";
    }
}
