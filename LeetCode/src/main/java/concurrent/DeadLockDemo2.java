package concurrent;

public class DeadLockDemo2 {

    public static void main(String[] args) throws InterruptedException {
        String[] s1 = { "a" }, s2 = { "b" };
        Thread t1 = new Thread(new MyRun1(s1, s2), "t1");
        Thread t2 = new Thread(new MyRun2(s1, s2), "t2");
        t1.start();
        Thread.sleep(200);
        t2.start();
        t1.join();
        t2.join();
        System.out.println(s1[0]);
        System.out.println(s2[0]);
    }
}

class MyRun1 implements Runnable {
    String[] s1, s2;

    MyRun1(String[] a, String[] b) {
        s1 = a;
        s2 = b;
    }

    @Override
    public void run() {
        System.out.println("Thread started:::" + Thread.currentThread().getName());
        synchronized (s1) {
            System.out.println("s1 locked by R1");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("s2 changed by R1");
            s2[0] = s1[0] + s2[0];
            synchronized (s2) {
                System.out.println("s2 locked by R1");
                s2[0] = s1[0] + s2[0];
            }
        }
        System.out.println("Thread ended:::" + Thread.currentThread().getName());
    }
}

class MyRun2 implements Runnable {
    String[] s1, s2;

    MyRun2(String[] a, String[] b) {
        s1 = a;
        s2 = b;
    }

    @Override
    public void run() {
        System.out.println("Thread started:::" + Thread.currentThread().getName());
        synchronized (s2) {
            System.out.println("s2 locked by R2");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("s1 changed by R2");
            s1[0] = s1[0] + s2[0];
            // synchronized (s1) {
            // System.out.println("s1 locked by R2");
            // s2[0] = s2[0] + s1[0];
            // }
            System.out.println("s2 released by R2");
        }
        System.out.println("Thread ended:::" + Thread.currentThread().getName());
    }
}
