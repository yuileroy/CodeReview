package pattern.singleton;

/*
 * lock method
 */
public class MyObject3 {
    private static MyObject3 myObject3;

    private MyObject3() {
    }

    synchronized public static MyObject3 getInstance() {
        if (myObject3 == null) {
            myObject3 = new MyObject3();
        }
        return myObject3;
    }
}
