import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.coderlong.webflux.pojo.User;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class TestWebClient {

    @Test
    public void test(){
        WebClient webClient = WebClient.create();
        long start = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "coderlong");
        Mono<User> mono = webClient.get().uri("http://localhost:8081/hello/{name}", params).retrieve().bodyToMono(User.class);
        User user = mono.block();
        System.out.println(user);
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
