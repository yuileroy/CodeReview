package concurrent;

public class StaticMethod {

    public static void main(String[] args) throws Exception {
        Thread threadA = new Thread(new CounterThread(), "t1");
        Thread threadB = new Thread(new CounterThread2(), "t2");

        threadA.start();
        Thread.sleep(1000);
        threadB.start();
    }

}

// Only One Thread can access static method, the lock is on Class
class Counter {
    static long count = 0, count2 = 0;

    public static synchronized void add(long value) {
        count += value;
    }

    public static synchronized void add2(long value) {
        count2 += value;
    }
}

class CounterThread implements Runnable {
    public void run() {
        System.out.println("Thread started:::" + Thread.currentThread().getName());
        Counter.add(1);
        System.out.println("Thread ended:::" + Thread.currentThread().getName());
    }
}

class CounterThread2 implements Runnable {
    public void run() {
        System.out.println("Thread started:::" + Thread.currentThread().getName());
        Counter.add2(1);
        System.out.println("Thread ended:::" + Thread.currentThread().getName());
    }
}
