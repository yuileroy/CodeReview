package pattern.singleton;
/*
 * static block
 */
public class MyObject6 {
    private static MyObject6 instance = null;

    private MyObject6() {
    }

    static {
        instance = new MyObject6();
    }

    public static MyObject6 getInstance() {
        return instance;
    }
}