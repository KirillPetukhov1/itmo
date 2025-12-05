package entities;

public class Julio extends Person {

    public Julio(float lack, int strength) {
        super("Господин Жулио", lack, strength);
        this.isInShop = true;
    }

    public void enterShop() {
        if (!isInShop) {
            System.out.println(this.name + " заходит в магазин.");
            this.isInShop = true;
        }
    }

    public void leaveShop() {
        if (isInShop) {
            this.isInShop = false;
            System.out.println(this.name + " удалился из магазина.");
        }
    }

    public void freeCaptives(Person[] persons) {
        for (Person person : persons) {
            if (person instanceof Captive) {
                ((Captive)person).beFree(this);
            }
        }
    }
}
