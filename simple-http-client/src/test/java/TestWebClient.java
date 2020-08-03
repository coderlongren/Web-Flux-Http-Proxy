import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class TestWebClient {

    @Test
    public void test(){
        WebClient webClient = WebClient.create();
        long start = System.currentTimeMillis();
        Mono<String> mono = webClient.get().uri("http://localhost:8081/hello/rensailong").retrieve().bodyToMono(String.class);
        mono.subscribe(res -> {System.out.println("获取到1 " + res);});
        System.out.println("耗时" + (System.currentTimeMillis() - start) / 1000);
        Mono<String> mono1 = webClient.get().uri("http://localhost:8081/hello/rensaiong").retrieve().bodyToMono(String.class);
        mono.subscribe(res -> {System.out.println("获取到2 " + res);});
        System.out.println("耗时" + (System.currentTimeMillis() - start) / 1000);

        Mono<String> mono2 = webClient.get().uri("http://localhost:8081/hello/rensa").retrieve().bodyToMono(String.class);
        mono.subscribe(res -> {System.out.println("获取到3 " + res);});
        System.out.println("耗时" + (System.currentTimeMillis() - start) / 1000);
    }
    @Test
    public void test2() throws InterruptedException {
        long start = System.currentTimeMillis();
        Mono<Object> mono = Mono.defer(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Mono.empty();
        }).subscribeOn(Schedulers.elastic());
        mono.subscribe(res -> {
            System.out.println("hehe");
            System.out.println(res);
        });
        System.out.println("耗时" + (System.currentTimeMillis() - start) / 1000);

        Thread.sleep(2222222);

    }
}
