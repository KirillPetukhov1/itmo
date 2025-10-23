package statusmoves;

import ru.ifmo.se.pokemon.*;

public final class PlayNiceMove extends StatusMove {
    public PlayNiceMove() {
        super(Type.NORMAL, 0, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        Effect EffectObject = new Effect().stat(Stat.ATTACK, -1);
        p.addEffect(EffectObject);
    }

    @Override
    protected boolean checkAccuracy(Pokemon att, Pokemon def) {
        return true;
    }

    @Override
    protected String describe() {
        return "красиво играет";
    }
}
