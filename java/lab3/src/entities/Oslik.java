package entities;

public class Oslik extends Person implements Captive {
    protected boolean isTied;

    public Oslik(float lack, int strength, boolean isInShop, boolean isTied, Feeling feeling) {
        super("Ослик", lack, strength, feeling);
        this.isInShop = isInShop;
        this.isTied = isTied;
    }

    public void enterShop() {
        if (!isInShop || (!this.feeling.getIsActive() && !isTied)) {
            if (isTied) {
                System.out.println("Связанный " + this.feeling + " " + this.name + " заносится в магазин.");
            } else {
                System.out.println(this.feeling + " " + this.name + " заходит в магазин.");
            }
            this.isInShop = true;
        }
    }

    public void leaveShop() {
        if (isInShop) {
            this.isInShop = false;
            if (isTied) {
                System.out.println("Связанный " + this.feeling + " " + this.name + " выносится из магазина.");
            } else {
                System.out.println(this.feeling + " " + this.name + " выходит из магазина.");
            }
        }
    }

    public boolean getIsTied() {
        return this.isTied;
    }

    public void tryToGetFree() {
        if (!isTied) return;
        
        if (this.strength / 10 * Math.random() < this.lack) {
            isTied = false;
            System.out.println(this.name + " самостоятельно освобождается от веревки.");
        } else {
            System.out.println(this.name + " не смог освободиться, ему не хватило силы.");
        }
    }

    public void beFree(Person person) {
        if (!isTied) return;
        isTied = false;
        System.out.println(this.name + " освобождается при помощи " + person.getName() + ".");
    }
}
