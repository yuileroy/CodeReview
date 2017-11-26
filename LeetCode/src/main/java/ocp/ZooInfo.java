package ocp;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ZooInfo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService service = null;
        try {
            service = Executors.newSingleThreadExecutor();
            System.out.println("begin");
            service.execute(() -> System.out.println("Printing zoo inventory"));
            service.execute(() -> {
                for (int i = 0; i < 3; i++)
                    System.out.println("Printing record: " + i);
            });
            service.execute(() -> System.out.println("Printing zoo inventory"));
            System.out.println("end");
        } finally {
            if (service != null)
                service.shutdown();
        }

        try {
            service = Executors.newSingleThreadExecutor();
            Future<Integer> result = service.submit(() -> 30 + 11);
            System.out.println(result.get());
        } finally {
            if (service != null)
                service.shutdown();
        }

        try {
            service = Executors.newSingleThreadExecutor();
            // Add tasks to the thread executor
        } finally {
            if (service != null)
                service.shutdown();
        }
        if (service != null) {
            service.awaitTermination(3, TimeUnit.SECONDS);
            // Check whether all tasks are finished
            if (service.isTerminated())
                System.out.println("All tasks finished");
            else
                System.out.println("At least one task is still running");
        }
    }
}
