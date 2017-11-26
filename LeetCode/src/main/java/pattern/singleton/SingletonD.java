package pattern.singleton;

public final class SingletonD {
    private SingletonD() {
    }

    private static final class LazyHolder {
        private static final SingletonD INSTANCE = new SingletonD();
    }

    public static SingletonD getInstance() {
        return LazyHolder.INSTANCE;
    }

    public String exceptions() {
        String result = "";
        String v = null;
        try {
            try {
                result += "before";
                v.length();
                result += "after";
            } catch (NullPointerException e) {
                result += "catch";
                // this Exception is ignore
                throw new Exception();
            } finally {
                result += "finally";
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
            result += "done";
        }
        System.out.println(result);
        return result;
    }

    public static void main(String[] args) {
        SingletonD d = SingletonD.getInstance();
        d.exceptions();
    }
}

class Base {
    public void m1() {
    }

    public final void m2() {
    }

    public static void m3() {
    }

    public static final void m4() {
    }
}

class Derived extends Base {
    public void m1() {
    }
    // OK, overriding Base#m1()

    // public void m2() {}
    // forbidden

    public static void m3() {
    }
    // OK, hiding Base#m3()

    // public static void m4() {}
    // forbidden
}
