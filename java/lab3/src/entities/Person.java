package entities;

public abstract class Person {
    protected String name;
    protected float lack;
    protected int strength;
    protected boolean isInShop = false;

    public Person(String name, float lack, int strength) {
        this.name = name;
        if (lack <= 1 && lack >= 0){
            this.lack = lack;
        } else {
            // TODO value error
        }
        if (strength >= 0) {
            this.strength = strength;
        } else {
            // TODO value error
        }
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
            // TODO value error
        }
    }

    public int getStrength() {
        return this.strength;
    }

    public void setStrength(int strength) {
        if (strength >= 0){
            this.strength = strength;
        } else {
            // TODO value error
        }
    }

    public boolean getIsInShop() {
        return this.isInShop;
    }

    public abstract void enterShop();

    public abstract void leaveShop();
}
