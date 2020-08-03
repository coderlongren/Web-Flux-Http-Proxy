import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import reactor.core.publisher.Flux;

public class TestCompletableFutureAsync {
    @Test
    public void test() {
        Flux.fromArray(new Integer[]{1, 2 ,3 ,5}).buffer(2).subscribe(buffer -> {
            System.out.println(buffer);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testClf() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "res";
        }, executorService);

        future1.thenRun(() -> {
            try {
                Thread.sleep(40000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("执行线程0 " + Thread.currentThread().getName());
        });
        future1.thenRun(() -> {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("执行线程1 " + Thread.currentThread().getName());
        });
        future1.thenRun(() -> {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("执行线程2  " + Thread.currentThread().getName());
        });
        future1.thenRun(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("执行线程3 " + Thread.currentThread().getName());
        });


//        future1.thenAccept(res -> {
//            try {
//                Thread.sleep(30000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("执行线程  " + Thread.currentThread().getName());
//            System.out.println("second" + res);
//        });
//        future1.thenAccept(res -> {
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("执行线程  " + Thread.currentThread().getName());
//            System.out.println("second" + res);
//        });
        future1.join();
        Thread.sleep(222222);
    }

    public static void ma2in(String[] args) {

    }
}
