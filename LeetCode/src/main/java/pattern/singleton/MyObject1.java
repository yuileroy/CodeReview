package pattern.singleton;

/*
 * http://www.cnblogs.com/liuyang0/p/6613152.html
 */
public class MyObject1 {
    private static MyObject1 myObject1 = new MyObject1();

    private MyObject1() {
    }

    public static MyObject1 getInstance() {
        return myObject1;
    }
}