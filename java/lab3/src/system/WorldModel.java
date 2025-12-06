package system;

import entities.*;
import java.lang.Math;

public class WorldModel {
    private static Neznaika neznaika = new Neznaika(0.5f, 20, false, false, Feeling.HAPPY);
    private static Oslik oslik = new Oslik(0.3f, 30, false, true,  Feeling.SAD);
    private static Julio julio = new Julio(0.5f, 20,  Feeling.HAPPY);
    private static AnotherVisitor anotherVisitor = new AnotherVisitor(0.5f, 20, 100, Feeling.ANGRY);

    public static void go() {
        neznaika.enterShop();
        oslik.enterShop();

        neznaika.tryToGetFree();
        oslik.tryToGetFree();

        if (Math.random() < 0.5){
            julio.freeCaptives(new Person[] {neznaika, oslik});
        }
        
        if (!neznaika.getIsTied()) {
            neznaika.takeOffHat();
            neznaika.handOverEnvelope(julio);
        }

        anotherVisitor.enterShop();
        anotherVisitor.showClothes();
        anotherVisitor.buy(new Pistol("семизарядный \"Бурбон\""), (int) (150 * Math.random()));
        if (anotherVisitor.getPistol() != null) {
            anotherVisitor.preparePistol();
        }

        if (anotherVisitor.getFeeling() == Feeling.ANGRY) {
            Person randomVisitor = new Person("Случайный покупатель", 0.5f, 10, Feeling.HAPPY) {
                public void enterShop() {
                    this.isInShop = true;
                }

                public void leaveShop() {
                    if (isInShop) {
                        if (this.feeling == Feeling.SCARED) {
                            System.out.println(this.feeling + " " + this.name + " выбегает из магазина.");
                        }
                        this.isInShop = false;
                    }
                }
            };
            randomVisitor.enterShop();
            anotherVisitor.takeShot(randomVisitor);
        }
        
        neznaika.leaveShop();
        oslik.leaveShop();
        anotherVisitor.leaveShop();
    }
}
