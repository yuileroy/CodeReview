package pattern.singleton;

/*
 * JVM会保证enum不能被反射并且构造器方法只执行一次,因此该单例是线程安全的
 */
public class MyObject7 {
    private enum MyEnumSingleton {
        enumFactory;
        private MyObject7 myObject7;

        private MyEnumSingleton() {
            myObject7 = new MyObject7();
        }

        public MyObject7 getInstance() {
            return myObject7;
        }
    }

    public static MyObject7 getInstance() {
        return MyEnumSingleton.enumFactory.getInstance();
    }
}