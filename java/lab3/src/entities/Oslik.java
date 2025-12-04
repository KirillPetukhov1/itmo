package entities;

public class Oslik extends Person implements Captive {
    protected boolean isTied;

    public Oslik(float lack, int strength) {
        super("Ослик", lack, strength);
        this.isInShop = true;
        this.isTied = true;
    }

    public void enterShop() {
        if (!isInShop) {
            if (isTied) {
                System.out.println("Связанный " + this.name + " заносится в магазин.");
            } else {
                System.out.println(this.name + " заходит в магазин.");
            }
            this.isInShop = true;
        }
    }

    public void leaveShop() {
        if (isInShop) {
            if (isTied) {
                System.out.println("Связанный " + this.name + " выносится из магазина.");
            } else {
                System.out.println(this.name + " выходит из магазина.");
            }
        }
    }
}
