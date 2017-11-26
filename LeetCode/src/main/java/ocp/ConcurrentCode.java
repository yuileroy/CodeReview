package ocp;

import static ocp.ConcurrentUtils.sleep;
import static ocp.ConcurrentUtils.stop;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

import org.junit.Test;

/**
 * http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/
 */

class TreeNodeSync {
    public int val;
    public TreeNodeSync left;
    public TreeNodeSync right;

    public TreeNodeSync(int x) {
        val = x;
    }

    public String toString() {
        return String.valueOf(val);
    }

    public synchronized String toString2() {
        Thread.holdsLock(this);
        return Thread.holdsLock(this) + " " + 2 * val;
    }
}

public class ConcurrentCode {

    public void codeObject() throws InterruptedException {
        TreeNodeSync ob = new TreeNodeSync(5);
        synchronized (ob) { // ! synchronized
            ob.wait(10);
            ob.notify();
            ob.notifyAll();
        }
        // IllegalMonitorException comes when one of the 2 situation occurs.... 1> wait on an object's monitor without
        // owning the specified monitor. 2> notify other threads waiting on an object's monitor without owning the
        // specified monitor.

        // ob.wait(10);
        // ob.notify();
        // ob.notifyAll();

        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
            System.out.println("Hello " + ob.toString2());
            System.out.println(Thread.holdsLock(ob)); // false

            synchronized (ob) {
                ob.toString();
                System.out.println("synchronized " + Thread.holdsLock(ob)); // false
            }
            System.out.println("Hello " + threadName);
        };
        new Thread(task).start();

        Thread.sleep(1000);
        Thread.yield();
        Thread.activeCount();
        System.out.println(Thread.currentThread());
        System.out.println(Thread.holdsLock(ob)); // false
        ob.toString();
        ob.equals(ob);
        ob.hashCode();
        System.out.println(ob.getClass());
    }

    public void code11() {
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        };

        Thread thread = new Thread(task);
        thread.start();

        System.out.println("Done!");
    }

    public void Code12() {
        Runnable runnable = () -> {
            try {
                String name = Thread.currentThread().getName();
                System.out.println("Foo " + name);
                // Performs a Thread.sleep using this time unit. This is a convenience method that converts time
                // arguments into the form required by the Thread.sleep method.
                TimeUnit.SECONDS.sleep(1); // Thread.sleep(1000);
                Thread.sleep(1000);
                System.out.println("Bar " + name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void code13() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        });

        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }
    }

    public void code14() throws InterruptedException, ExecutionException {

        // In addition to Runnable executors support another kind of task named Callable. Callables are functional
        // interfaces just like runnables but instead of being void they return a value.
        Callable<Integer> task = () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 123;
            } catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Integer> future = executor.submit(task);

        System.out.println("future done? " + future.isDone());

        Integer result = future.get();

        System.out.println("future done? " + future.isDone());
        System.out.print("result: " + result);
    }

    public void code15() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Integer> future = executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                return 123;
            } catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });

        // Waits if necessary for at most the given time for the computation to complete, and then retrieves its result,
        // if available.
        future.get(1, TimeUnit.SECONDS);
    }

    int count = 0;

    // ReentrantLock lock = new ReentrantLock();
    synchronized void incrementSync() {
        count = count + 1;
    }

    void incrementSync2() {
        synchronized (this) {
            count = count + 1;
        }
    }

    public void code21() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        IntStream.range(0, 10000).forEach(i -> executor.submit(this::incrementSync));
        ConcurrentUtils.stop(executor);
        System.out.println(count); // 10000
    }

    public void code22() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        ReentrantLock lock = new ReentrantLock();
        executor.submit(() -> {
            lock.lock();
            try {
                sleep(3);
            } finally {
                lock.unlock();
            }
        });

        executor.submit(() -> {
            sleep(6);
            System.out.println("Locked: " + lock.isLocked());
            System.out.println("Held by me: " + lock.isHeldByCurrentThread());
            boolean locked = lock.tryLock();
            System.out.println("Lock acquired: " + locked);
        });

        executor.submit(() -> {
            sleep(1);
            System.out.println("Locked: " + lock.isLocked());
            System.out.println("Held by me: " + lock.isHeldByCurrentThread());
            boolean locked = lock.tryLock();
            System.out.println("Lock acquired: " + locked);
        });

        stop(executor);
    }

    public void code23() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Map<String, String> map = new HashMap<>();
        ReadWriteLock lock = new ReentrantReadWriteLock();

        executor.submit(() -> {
            lock.writeLock().lock();
            try {
                sleep(1);
                map.put("foo", "bar");
            } finally {
                lock.writeLock().unlock();
            }
        });

        Runnable readTask = () -> {
            lock.readLock().lock();
            try {
                System.out.println(map.get("foo"));
                sleep(1);
            } finally {
                lock.readLock().unlock();
            }
        };

        executor.submit(readTask);
        executor.submit(readTask);
        stop(executor);
    }

    public void code24() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        StampedLock lock = new StampedLock();

        executor.submit(() -> {
            long stamp = lock.tryOptimisticRead();
            try {
                System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
                sleep(1);
                System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
                sleep(2);
                System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
            } finally {
                lock.unlock(stamp);
            }
        });

        executor.submit(() -> {
            long stamp = lock.writeLock();
            try {
                System.out.println("Write Lock acquired");
                sleep(2);
            } finally {
                lock.unlock(stamp);
                System.out.println("Write done");
            }
        });

        stop(executor);
    }

    public void code25() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        StampedLock lock = new StampedLock();

        executor.submit(() -> {
            long stamp = lock.readLock();
            try {
                if (count == 0) {
                    stamp = lock.tryConvertToWriteLock(stamp);
                    if (stamp == 0L) {
                        System.out.println("Could not convert to write lock");
                        stamp = lock.writeLock();
                    }
                    count = 23;
                }
                System.out.println(count);
            } finally {
                lock.unlock(stamp);
            }
        });

        stop(executor);
    }

    public void code26() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Semaphore semaphore = new Semaphore(5);
        Runnable longRunningTask = () -> {
            boolean permit = false;
            try {
                permit = semaphore.tryAcquire(1, TimeUnit.SECONDS);
                if (permit) {
                    System.out.println("Semaphore acquired");
                    sleep(2);
                } else {
                    System.out.println("Could not acquire semaphore");
                }
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            } finally {
                if (permit) {
                    semaphore.release();
                }
            }
        };

        IntStream.range(0, 10).forEach(i -> executor.submit(longRunningTask));

        stop(executor);
    }

    public void code31() {
        System.out.println(ForkJoinPool.getCommonPoolParallelism());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("foo", "bar");
        map.put("han", "solo");
        map.put("r2", "d2");
        map.put("c3", "p0");
        map.put("f1", "bar");
        map.put("f2", "solo");
        map.put("f3", "d2");
        map.put("f4", "p0");

        map.forEach(1, (key, value) -> System.out.printf("key: %s; value: %s; thread: %s\n", key, value,
                Thread.currentThread().getName()));
        String result = map.search(1, (key, value) -> {
            System.out.println(Thread.currentThread().getName());
            if ("foo".equals(key)) {
                return value;
            }
            return null;
        });
        System.out.println("Result1: " + result + "\n");

        String result2 = map.searchValues(1, value -> {
            System.out.println(Thread.currentThread().getName());
            if (value.length() > 3) {
                return value;
            }
            return null;
        });
        System.out.println("Result2: " + result2 + "\n");

        String result3 = map.reduce(3, (key, value) -> {
            System.out.println("Transform: " + Thread.currentThread().getName());
            return key + "=" + value;
        }, (s1, s2) -> {
            System.out.println("Reduce: " + Thread.currentThread().getName());
            return s1 + ", " + s2;
        });
        System.out.println("Result3: " + result3 + "\n");
    }

    @Test
    public void test02() throws InterruptedException {
        code31();
    }

    // @Test
    public void test01() throws InterruptedException {
        codeObject();
    }
}
