package com.nhuszka.reactive.assignment.combiner5;

import com.nhuszka.reactive.util.DefaultSubscriber;
import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;

public class CarPriceDemo {

    public static void main(String[] args) {
        final Integer price = 10000;
        Flux.combineLatest(
                        monthStream(),
                        demandStream(),
                        (month, demand) -> (price - month * 100) * demand)
                .subscribe(new DefaultSubscriber());

        Util.waitFor(10);
    }

    private static Flux<Long> monthStream() {
        return Flux.interval(Duration.ofSeconds(1));
    }

    private static Flux<Double> demandStream() {
        return Flux.interval(Duration.ofSeconds(3))
                .map(i -> (new Random().nextInt(40) + 80) / 100D)
                .startWith(1D);
    }
}
