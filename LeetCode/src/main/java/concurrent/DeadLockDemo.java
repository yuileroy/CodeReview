package concurrent;

public class DeadLockDemo {

    public static void main(String[] args) {
        String s1 = "a", s2 = "b";
        Thread t1 = new Thread(new MyRunnable1(s1, s2), "t1");
        Thread t2 = new Thread(new MyRunnable2(s1, s2), "t2");
        t1.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();
    }

}

class MyRunnable1 implements Runnable {
    String s1, s2;

    MyRunnable1(String a, String b) {
        s1 = a;
        s2 = b;
    }

    @Override
    public void run() {
        System.out.println("Thread started:::" + Thread.currentThread().getName());
        synchronized (s1) {
            System.out.println("s1 locked by R1");
            s1 = s1 + s2;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (s2) {
                System.out.println("s1 locked by R2");
                s1 = s1 + s2;
            }
        }
        System.out.println("Thread ended:::" + Thread.currentThread().getName());
    }
}

class MyRunnable2 implements Runnable {
    String s1, s2;

    MyRunnable2(String a, String b) {
        s1 = a;
        s2 = b;
    }

    @Override
    public void run() {
        System.out.println("Thread started:::" + Thread.currentThread().getName());
        synchronized (s2) {
            System.out.println("s2 locked by R2");
            s2 = s2 + s1;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (s1) {
                System.out.println("s1 locked by R2");
                s2 = s2 + s1;
            }
        }
        System.out.println("Thread ended:::" + Thread.currentThread().getName());
    }
}
