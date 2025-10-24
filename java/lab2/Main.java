import ru.ifmo.se.pokemon.*;
import mypokemons.*;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        Pokemon p1 = new Alomomola("Аламола");
        Pokemon p2 = new Morelull("Морелул");
        Pokemon p3 = new Shiinotic("Шиинотик");
        Pokemon p4 = new Togekiss("Тогекисс");
        Pokemon p5 = new Togepi("Тогепи");
        Pokemon p6 = new Togetic("Тогетик");
        b.addAlly(p1);
        b.addAlly(p2);
        b.addAlly(p3);
        b.addFoe(p4);
        b.addFoe(p5);
        b.addFoe(p6);
        b.go();
    }
}