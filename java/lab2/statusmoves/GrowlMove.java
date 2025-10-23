package statusmoves;

import ru.ifmo.se.pokemon.*;

public final class GrowlMove extends StatusMove {
    public GrowlMove() {
        super(Type.NORMAL, 0, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        Effect EffectObject = new Effect().stat(Stat.ATTACK, -1);
        p.addEffect(EffectObject);
    }

    @Override
    protected String describe() {
        return "рычит";
    }
}