package entities;

import java.util.ArrayList;
import system.exceptions.*;

public class AnotherVisitor extends Person implements Bayer {
    protected int moneyAmount;
    protected Pistol pistol;
    protected ArrayList<Object> purchases = new ArrayList<Object>();
    protected Clothes trousers;
    protected Clothes outerwear;
    protected Hat hat;
    
    public AnotherVisitor(float lack, int strength, int moneyAmount, Feeling feeling) {
        super("Еще один посетитель", lack, strength, feeling);
        this.moneyAmount = moneyAmount;
        this.isInShop = false;
        this.trousers = Clothes.TIGHT_TROUSERS;
        this.outerwear = Clothes.GREY_SWEATSHIRT;
        this.hat = new Hat("клетчатая кепка", true);
    }

    public void buy(Object purchase, int cost) {
        if (cost > moneyAmount) {
            System.out.println(this.name + " хотел купить " + purchase + ", но ему не хватило денег.");
            return;
        }

        if (purchase instanceof Pistol) {
            pistol = (Pistol) purchase;
        } else {
            purchases.add(purchase);
        }
        System.out.println(this.name + " покупает " + purchase + " за " + cost + ".");
    }

    public void preparePistol() {
        if (pistol != null) {
            pistol.charge();
            pistol.equip();
            System.out.println(this.name + " рассовывает патроны и прицепляет пистолет к поясу.");
        }
    }

    public void enterShop() {
        if (!isInShop) {
            System.out.println(this.feeling + " " + this.name + " заходит в магазин.");
            this.isInShop = true;
        }
    }

    public void leaveShop() {
        if (isInShop) {
            this.isInShop = false;
            if (trousers.getIsTight()) {
                System.out.println(this.feeling + " " + this.name + " удалился из магазина, широко расставляя свои согнутые в коленях ноги.");
            } else {
                System.out.println(this.feeling + " " + this.name + " удалился из магазина.");
            }
        }
    }

    public void showClothes() {
        System.out.println("На нем была " + this.hat + ", " + this.outerwear + " и " + this.trousers);
    }

    public Pistol getPistol() {
        return this.pistol;
    }

    public void takeShot(Person person) {
        class Shot {
            private Person target;
            private Pistol pistol;
            
            public Shot(Person target, Pistol pistol) {
                this.target = target;
                this.pistol = pistol;
            }

            public boolean canTakeShot() throws PersonIsUnavailableException {
                if (!this.target.isInShop) throw new PersonIsUnavailableException(person);
                return pistol != null && pistol.getIsEquipped() && pistol.getIsCharged();
            }
        }

        try {
            if ((new Shot(person, this.pistol)).canTakeShot()) {
                System.out.println(this.feeling + " " + this + " стреляет в сторону " + person + ".");
                this.pistol.discharge();
                person.setFeeling(Feeling.SCARED);
                person.leaveShop();
            } else {
                System.out.println(this.feeling + " " + this + " хотел бы выстрелить в сторону " + person + ", но у него нет оружия.");
            }
        } catch (PersonIsUnavailableException e) {
            System.out.println(this.feeling + " " + this + " хочет в кого-нибудь выстрелить.");
        }
    }
}
