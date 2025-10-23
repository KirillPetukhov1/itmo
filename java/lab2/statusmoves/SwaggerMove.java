package statusmoves;

import ru.ifmo.se.pokemon.*;

public final class SwaggerMove extends StatusMove {
    public SwaggerMove() {
        super(Type.NORMAL, 0, 85);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        Effect EffectObject = new Effect().stat(Stat.ATTACK, 2);
        p.addEffect(EffectObject);
        Effect.confuse(p);
    }

    @Override
    protected String describe() {
        return "показывает себя крутым";
    }
}
