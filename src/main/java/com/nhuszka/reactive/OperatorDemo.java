package com.nhuszka.reactive;

import com.nhuszka.reactive.util.DefaultSubscriber;
import com.nhuszka.reactive.util.Util;
import org.apache.commons.lang3.tuple.Pair;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class OperatorDemo {

    public static void main(String[] args) {
        // --- handle
        Flux.range(1, 10)
                .handle((actualItemEmitted, synchronousSink) -> {
                    if (actualItemEmitted % 2 == 0) {
                        synchronousSink.next(actualItemEmitted);
                    } else {
                        synchronousSink.next(actualItemEmitted + "-odd");
                    }
                })
                .subscribe(new DefaultSubscriber());

        // ---- hooks, callbacks
        Flux.create(fluxSink -> {
                    System.out.println("inside sink");
                    for (int i = 0; i < 5; i++) {
                        fluxSink.next(i);
                    }
                    fluxSink.complete();
                    System.out.println("--completed");
                })
                .doOnComplete(() -> System.out.println("completed-callback"))
                .doFirst(() -> System.out.println("first2-callback"))
                .doOnNext(o -> System.out.println("onnext-callback " + o))
                .doFirst(() -> System.out.println("first1-callback"))
                .doOnSubscribe(subscription -> System.out.println("subscribe1-callback"))
                .doOnRequest(l -> System.out.println("doOnRequest-callback" + l))
                .doOnError(throwable -> System.out.println("doonerror-callback  " + throwable))
                .doOnTerminate(() -> System.out.println("terminate-callback"))
                .doOnSubscribe(subscription -> System.out.println("subscribe2-callback"))
                .doOnCancel(() -> System.out.println("doOncancel"))
                .doFinally(signalType -> System.out.println("doOnFinally " + signalType))
                .doOnDiscard(Object.class, o -> System.out.println("doOnDiscard " + o))
                .doAfterTerminate(() -> System.out.println("doAfterTerminate"))
                .subscribe(new DefaultSubscriber());

        // ------ limitRate
        Flux.range(1, 10)
                .log()
                .limitRate(4) // first requests 4, then if 3 consumed, requests for 4 more (75% by default)
//                .limitRate(10, 9) // 90% specified
//                .limitRate(10, 10) // behaves like 75%
//                .limitRate(10, 0) // if we want to drain everything then request more
                .subscribe(new DefaultSubscriber());

        // --- delay
        Flux.range(1, 100) // gets the request only for 32 first, then 24 (which is 75% of 32)
                .log()
                .delayElements(Duration.ofMillis(20))
                .subscribe(new DefaultSubscriber());
//        Reactor.util.concurrent.Queues.XS_BUFFER_SIZE is 32 (min size 8)
//        System.setProperty("reactor.bufferSize.x", "9");

        Util.waitFor(3);

        // ---- onError...
        Flux.range(1, 10)
                .log().map(i -> i / (5 - i))
//                .onErrorReturn(-1)
                .onErrorContinue((throwable, o) -> {
                })
//                .onErrorResume(e -> fallback10())
                .subscribe(new DefaultSubscriber());

        Util.waitFor(3);

        // ---- timeout
        getNumbersSlowly()
                .timeout(Duration.ofSeconds(1), fallbackNums())
                .subscribe(new DefaultSubscriber("slowly"));

        Util.waitFor(10);

        System.out.println("DEFAULT IF EMPTY");

        // --- default if empty
        getNumbers()
                .filter(i -> i > 10)
                .defaultIfEmpty(0)
                .subscribe(new DefaultSubscriber());

        // --- default if empty
        getNumbers()
                .filter(i -> i > 2)
                .switchIfEmpty(getNumbersSlowly())
                .subscribe(new DefaultSubscriber("switchIfEmpty"));

        Util.waitFor(4);

        // --- transform
        getPairs()
                .transform(transformerFilterMapper())
                .subscribe(new DefaultSubscriber("pairs"));

        // --- switchOnFirst
        getPairs()
                .switchOnFirst((signal, pairFlux) -> { // if first item left is not B... then sort it
                    if (signal.isOnNext()) { // emitting the data
                        if (signal.get().getLeft().startsWith("B")) {
                            System.out.println("swithonfirst - continue with original flux");
                            return pairFlux;
                        }
                    }
                    System.out.println("swithonfirst - sort flux");
                    return pairFlux.sort();
                })
                .subscribe(new DefaultSubscriber("switchonfirst"));

        // ---- flatMap
        // Java8 example
        List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9))
                .stream()
                .flatMap(collection -> collection.stream())
                .forEach(System.out::println);

        getIds()
//                .map(id -> getFlux(id)) // returns Flux<Flux<Integer>
                .flatMap(id -> getFlux(id)) // return Flux<Integer> flattened (order is not retained in some cases)
//                .concatMap(id -> getFlux(id)) // return Flux<Integer> flattened (order is retained)
                .subscribe(new DefaultSubscriber("flatmap"));
        Util.waitFor(20);

    }

    private static Flux<Integer> getIds() {
        return Flux.create((FluxSink<Integer> fluxSink) -> {
            fluxSink.next(1);
            fluxSink.next(4);
            fluxSink.next(7);
            fluxSink.complete();
        }).delayElements(Duration.ofSeconds(1));
    }

    private static Flux<Integer> getFlux(Integer id) {
        return Flux.range(id, 3);
    }


    private static Function<Flux<Pair<String, String>>, Flux<Pair<String, String>>> transformerFilterMapper() {
        return pairs -> pairs
                .filter(pair -> pair.getLeft().contains("a"))
                .map(pair -> Pair.of(pair.getLeft(), pair.getRight().toUpperCase()))
                .doOnDiscard(Pair.class, pair -> System.out.println("Discarded: " + pair));
    }

    private static Flux<Pair<String, String>> getPairs() {
        return Flux.create(sink -> {
            sink.next(Pair.of("Eva", "Adam"));
            sink.next(Pair.of("Bud", "Terence"));
            sink.next(Pair.of("Stan", "Pan"));
            sink.next(Pair.of("Jack", "Jill"));
            sink.complete();
        });
    }

    private static Flux<Integer> getNumbers() {
        return Flux.range(1, 10);
    }

    private static Flux<Integer> getNumbersSlowly() {
        return Flux.range(20, 10)
                .delayElements(Duration.ofSeconds(2));
    }

    private static Mono<Integer> fallback10() {
        return Mono.fromSupplier(
                () -> 10
        );
    }

    private static Flux<Integer> fallbackNums() {
        return Flux.range(100, 10)
                .delayElements(Duration.ofSeconds(1));
    }
}
