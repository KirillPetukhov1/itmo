package statusmoves;

import ru.ifmo.se.pokemon.*;

public final class AquaRingMove extends StatusMove {
    protected double maxHP;

    public AquaRingMove(double maxHP) {
        super(Type.WATER, 0, 100);
        this.maxHP = maxHP;
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        super.applySelfEffects(p);
        Effect EffectObject = new Effect()
                .stat(Stat.HP, (int) Math.floor(maxHP / 16))
                .turns(2147483647);
        p.addEffect(EffectObject);
    }

    @Override
    protected String describe() {
        return "окружает себя завесой воды";
    }
}
