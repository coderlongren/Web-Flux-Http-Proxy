import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TestCompareReactorCMF {
    public static void main(String[] args) {
        CompletableFuture<List<String>> ids = ifhIds();

        CompletableFuture<List<String>> result = ids.thenComposeAsync(l -> {
            Stream<CompletableFuture<String>> zip =
                    l.stream().map(i -> {
                        CompletableFuture<String> nameTask = ifhName(i);
                        CompletableFuture<Integer> statTask = ifhStat(i);
                        return nameTask
                                .thenCombineAsync(statTask, (name, stat) -> "Name " + name + " has stats " + stat);
                    });
            List<CompletableFuture<String>> combinationList = zip.collect(Collectors.toList());
            CompletableFuture<String>[] combinationArray =
                    combinationList.toArray(new CompletableFuture[combinationList.size()]);

            CompletableFuture<Void> allDone = CompletableFuture.allOf(combinationArray);
            return allDone.thenApply(v -> combinationList.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList()));
        });
        CompletableFuture.runAsync(() ->{}).cancel(true);
        FutureTask task = new FutureTask(() -> "");
        task.cancel(true);

    }

    public static void mai2n(String[] args) {
        Flux<String> ids = ifhrIds();
        Flux<String> combinations =
                ids.flatMap(id -> {
                    Mono<String> nameTask = ifhrName(id);
                    Mono<Integer> statTask = ifhrStat(id);

                    return nameTask.zipWith(statTask,
                            (name, stat) -> "Name " + name + " has stats " + stat);
                });

        Mono<List<String>> result = combinations.collectList();

        List<String> results = result.block();

    }


    private static Mono<Integer> ifhrStat(String id) {
        return null;
    }

    private static Mono<String> ifhrName(String id) {
        return null;
    }

    private static Flux<String> ifhrIds() {
        return null;
    }

    private static CompletableFuture<Integer> ifhStat(String i) {
        return null;
    }

    private static CompletableFuture<String> ifhName(String i) {
        return null;
    }

    private static CompletableFuture<List<String>> ifhIds() {
        return null;
    }
}
