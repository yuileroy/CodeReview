package javacode.code;

public class BindA {
    int i = 0;

    int get() {
        return i;
    }

    public static void main(String[] args) {
        BindA a = new BindB();
        BindB b = new BindB();
        System.out.println(a.i);
        System.out.println(a.get());
        System.out.println(b.i);
        System.out.println(b.get());
    }
}

class BindB extends BindA {
    int i = 1;
    int get() {
        return i;
    }
}
