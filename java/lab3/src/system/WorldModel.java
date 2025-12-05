package system;

import entities.*;
import java.lang.Math;

public class WorldModel {
    private static Neznaika neznaika = new Neznaika(0.5f, 20, true, true);
    private static Oslik oslik = new Oslik(0.3f, 30, true, true);
    private static Julio julio = new Julio(0.5f, 20);
    private static AnotherVisitor anotherVisitor = new AnotherVisitor(0.5f, 20, 100);

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
        
        neznaika.leaveShop();
        oslik.leaveShop();
        anotherVisitor.leaveShop();
    }
}
