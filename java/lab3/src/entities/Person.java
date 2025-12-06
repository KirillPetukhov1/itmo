package entities;

public abstract class Person {
    protected String name;
    protected float lack;
    protected int strength;
    protected boolean isInShop = false;
    protected Feeling feeling;

    public class LackOutOfRangeException extends RuntimeException {

        public LackOutOfRangeException(float lack) {
            super("Некорректное значение lack. Значение должно быть от 0 до 1. Получено " + lack);
        }
    }

    public Person(String name, float lack, int strength, Feeling feeling) {
        this.name = name;
        if (lack <= 1 && lack >= 0){
            this.lack = lack;
        } else {
            throw new LackOutOfRangeException(lack);
        }
        if (strength >= 0) {
            this.strength = strength;
        } else {
            this.strength = 10;
        }
        this.feeling = feeling;
    }

    public String getName() {
        return this.name;
    }

    public float getLack() {
        return this.lack;
    }

    public void setLack(float lack) {
        if (lack <= 1 && lack >= 0){
            this.lack = lack;
        } else {
            throw new LackOutOfRangeException(lack);
        }
    }

    public int getStrength() {
        return this.strength;
    }

    public void setStrength(int strength) {
        if (strength >= 0){
            this.strength = strength;
        } else {
            this.strength = 10;
        }
    }

    public boolean getIsInShop() {
        return this.isInShop;
    }

    public Feeling getFeeling() {
        return this.feeling;
    }

    public void setFeeling(Feeling feeling) {
        this.feeling = feeling;
    }

    public String toString() {
        return this.getName();
    }

    public abstract void enterShop();

    public abstract void leaveShop();
}
