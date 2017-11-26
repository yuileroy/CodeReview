package pattern.singleton;

/*
 * lock class
 */
public class MyObject4 {
    private static MyObject4 myObject4;

    private MyObject4() {
    }

    public static MyObject4 getInstance() {
        if (myObject4 == null) {
            synchronized (MyObject4.class) {
                if (myObject4 == null) {
                    myObject4 = new MyObject4();
                }
            }
        }
        return myObject4;
    }
}