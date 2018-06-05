package javacode.code;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerConsumer {
    Deque<Integer> items = new ArrayDeque<Integer>();
    final static int ITEMS = 100;

    public static void main(String args[]) {
        ProducerConsumer pc = new ProducerConsumer();
        Thread t1 = new Thread(pc.new Producer());
        Consumer consumer = pc.new Consumer();
        Thread t2 = new Thread(consumer, "c1");
        Thread t3 = new Thread(consumer, "c2");
        Thread t4 = new Thread(consumer, "c3");
        t1.start();

        t2.start();
        t3.start();
        t4.start();
        try {
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class Producer implements Runnable {
        public void produce(int i) {
            System.out.println("Producing " + i);
            items.addLast(new Integer(i));
        }

        @Override
        public void run() {
            int i = 1;
            while (i <= ITEMS) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
                synchronized (items) {
                    if (items.size() < 5) {
                        produce(i++);
                        items.notifyAll();
                    }
                }
            }
        }
    }

    class Consumer implements Runnable {
        // consumed计数器允许线程停止
        AtomicInteger consumed = new AtomicInteger();

        public void consume() {
            if (!items.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + "ConsuminC " + items.removeFirst());
                consumed.incrementAndGet();
            }
        }

        private boolean theEnd() {
            return consumed.get() >= ITEMS;
        }

        @Override
        public void run() {
            while (!theEnd()) {
                synchronized (items) {
                    while (items.isEmpty() && (!theEnd())) {
                        try {
                            items.wait(500);
                        } catch (InterruptedException e) {
                            Thread.interrupted();
                        }
                    }
                    consume();
                }
            }
        }
    }
}