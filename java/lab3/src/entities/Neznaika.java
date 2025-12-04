package entities;

public class Neznaika extends Person implements Captive {
    protected boolean isTied;

    public Neznaika(float lack, int strength) {
        super("Незнайка", lack, strength);
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
