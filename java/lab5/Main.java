// package java.lab5;

public class Main {
    public static void main(String[] args) {
        A a = new B("gdrg");
        System.out.println(a.getClass());
        IA b = a;
        System.out.println(b.getClass());
    }
}

interface IA {
    void a();
}

interface IB {
    void a();
}

class A implements IA {

    String t;

    A(String t) {
        this.t = t;
    }

    public void a() {

    };
}

class B extends A implements IB {

    String t;

    B(String t) {
        super(t);
        this.t = t;
    }

    public void a() {

    }
}

