package com.nhuszka.reactive.batching;

import com.nhuszka.reactive.util.DefaultSubscriber;
import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class WindowDemo {

    public static void main(String[] args) {
        events(500)
                .window(5)
                .flatMap(stringFlux -> process(stringFlux))
                .subscribe(new DefaultSubscriber("wind"));

        Util.waitFor(5);
    }

    private static Mono<Void> process(Flux<String> stringFlux) {
        return stringFlux
                .doOnNext(s -> System.out.println("done " + s))
                .doOnComplete(() -> System.out.println("Done batch"))
                .then(); // converting to Mono<Void>
    }

    private static Flux<String> events(long millisBetweenEvents) {
        return Flux.interval(Duration.ofMillis(millisBetweenEvents))
                .map(i -> "event" + i);
    }

}
