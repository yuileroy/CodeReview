package pattern.singleton;

/*
 * private static class holder
 */
public class MyObject5 {
    private static class MyObject5Handle {
        private static MyObject5 myObject5 = new MyObject5();
    }

    private MyObject5() {
    }

    public static MyObject5 getInstance() {
        return MyObject5Handle.myObject5;
    }
}