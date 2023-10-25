package com.nhuszka.reactive.batching;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class GroupDemo {

    public static void main(String[] args) {
        Flux.range(1, 30)
                .delayElements(Duration.ofSeconds(1))
                .groupBy(i -> i % 2) // key: 0 odd number, 1 even number
                .subscribe(groupedFlex -> process(groupedFlex, groupedFlex.key()));

        Util.waitFor(5);
    }

    private static void process(Flux<Integer> flux, int key) {
        System.out.println("called process");
        flux.subscribe(integer -> System.out.println("key: " + key + ":" + integer));
    }
}
