package specialmoves;

import ru.ifmo.se.pokemon.*;

public final class DreamEaterMove extends SpecialMove {
    protected Pokemon selfP;

    public DreamEaterMove(Pokemon selfP) {
        super(Type.PSYCHIC, 80, 100);
        this.selfP = selfP;
    }

    @Override
    protected void applyOppDamage(Pokemon p, double damage) {
        if (p.getCondition() == Status.SLEEP) {
            p.setMod(Stat.HP, (int) Math.round(damage));
            Effect EffectObject = new Effect().stat(Stat.HP, (int) Math.round(damage / 2));
            selfP.addEffect(EffectObject);
        }
    }

    @Override
    protected String describe() {
        return "ворует сны";
    }
}
