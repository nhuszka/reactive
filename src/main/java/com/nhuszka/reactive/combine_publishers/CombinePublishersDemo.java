package com.nhuszka.reactive.combine_publishers;

import com.nhuszka.reactive.util.DefaultSubscriber;
import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;

public class CombinePublishersDemo {

    public static void main(String[] args) {
//        startWithCombiner();
//        concatCombiner();
//        mergeCombiner();
//        zipCombiner();
        combineLatestCombiner();
    }

    private static void startWithCombiner() {
        // --- startWith printed 20,21, ..., 29, 1
        Flux.range(1, 10)
                .startWith(Flux.range(20, 10))
                .take(11)
                .subscribe(System.out::println);

        Util.waitFor(1);

        // -- startWith: caching values
        CachedNumberGenerator generator = new CachedNumberGenerator();

        generator.generateIntegers()
                .take(2)
                .subscribe(new DefaultSubscriber("twonums1"));

        generator.generateIntegers()
                .take(2)
                .subscribe(new DefaultSubscriber("twonums2"));

        Util.waitFor(4);

        generator.generateIntegers()
                .take(2)
                .subscribe(new DefaultSubscriber("twonums3"));

        generator.generateIntegers()
                .filter(i -> i % 11 == 0)
                .take(1)
                .subscribe(new DefaultSubscriber("num%11==0"));

        Util.waitFor(10);
    }

    private static void concatCombiner() {
        Flux<Integer> flux12 = Flux.just(1, 2);
        Flux<Integer> flux345 = Flux.just(3, 4, 5);
        Flux<Object> fluxError = Flux.error(new RuntimeException("error"));

        // printed 1,2,3,4,5,error
        Flux<Object> concatFlux = Flux.concat(flux12, flux345, fluxError);
        concatFlux.subscribe(new DefaultSubscriber());

        // because of error, closing after 1,2
        // printed 1,2,erro
        Flux<Object> concatErrorFlux = Flux.concat(flux12, fluxError, flux345);
        concatErrorFlux.subscribe(new DefaultSubscriber());

        // error, but delayed, so not closing after 1,2
        // printed 1,2,3,4,5,error
        Flux<Object> concatDelayedErrorFlux = Flux.concatDelayError(flux12, fluxError, flux345);
        concatDelayedErrorFlux.subscribe(new DefaultSubscriber());
    }

    private static void mergeCombiner() {
        Flux<String> qatar = Flux.range(10, 10)
                .map(i -> Math.abs(i * getRandom(3) / 2))
                .delayElements(Duration.ofMillis(231))
                .map(i -> "Qatar" + String.valueOf(i).substring(0, 3));
        Flux<String> klm = Flux.range(10, 7)
                .map(i -> Math.abs(i * getRandom(5)))
                .delayElements(Duration.ofMillis(325))
                .map(i -> "KLM" + String.valueOf(i).substring(0, 3));

        // publisher emitting is merged
        Flux.merge(qatar, klm).subscribe(new DefaultSubscriber());

        Util.waitFor(10);
    }

    private static long getRandom(int num) {
        return new Random().nextLong() / num;
    }

    private static void zipCombiner() {
        Flux<String> body = Flux.range(1, 2)
                .map(i -> "body");
        Flux<String> engine = Flux.range(1, 3)
                .map(i -> "engine");
        Flux<String> tires = Flux.range(1, 6)
                .map(i -> "tires");

        // 2 cars are the result
        Flux.zip(body, engine, tires)
                .map(objects -> "car="
                        + objects.getT1() + "+"
                        + objects.getT2() + "+"
                        + objects.getT3())
                .subscribe(new DefaultSubscriber());
    }

    private static void combineLatestCombiner() {
        Flux<String> strings = Flux.just("A", "B", "C", "D")
                .delayElements(Duration.ofSeconds(1));
        Flux<Integer> numbers = Flux.just(1, 2, 3)
                .delayElements(Duration.ofSeconds(3));

        // A is gone...
        // B1, C1, D1, D2, D3
        Flux.combineLatest(strings, numbers, (s, i) -> s + i)
                .subscribe(new DefaultSubscriber());

        Util.waitFor(10);
    }
}
