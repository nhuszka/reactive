package com.nhuszka.reactive.flux_advanced;

import com.nhuszka.reactive.util.DefaultSubscriber;
import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

import java.util.Random;

public class FluxAdvancedDemo {
    private static final int LOW_STEP = -5;
    private static final int HIGH_STEP = 5;

    public static void main(String[] args) {
        DefaultSubscriber defaultSubscriberAbc = new DefaultSubscriber("abc");

        // ---- sink, programmatically emit items
        Flux.create(fluxSink -> {
            fluxSink.next(1);
            fluxSink.next(2);
            fluxSink.next(3);
//            fluxSink.error(new RuntimeException("custom exception"));
            fluxSink.complete();
        }).subscribe(defaultSubscriberAbc);


        // --- emit until condition is met
        Flux.create(fluxSink -> {
            int number;
            do {
                number = new Random().nextInt(HIGH_STEP - LOW_STEP) + LOW_STEP;
                fluxSink.next(number);
            } while (number != 0);
            fluxSink.complete();
        }).subscribe(defaultSubscriberAbc);

        // ---- custom producer to ping programmatically
        RandomIntProducer randomIntProducer = new RandomIntProducer();
        Flux.create(randomIntProducer)
                .subscribe(defaultSubscriberAbc);
        randomIntProducer.produce();


        // -- emit until condition but maximum 10 items emitted, if subscriber cancels, then exit
        Flux.generate(
                () -> 1, // initial state
                (counter, sink) -> {
                    int randomNum = new Random().nextInt(10);
                    sink.next(randomNum);
                    if (counter >= 10 || randomNum == 5) {
                        sink.complete();
                    }
                    return counter + 1;
                }
        ).subscribe(i -> System.out.println("generate-initialstate " + i));

        // -- FluxPush ~ hybrid approach between Flux.create and Flux.generate
        System.out.println("FLUX PUSH");
        Flux.push(new RandomIntProducer())
                .subscribe(System.out::println);

        // ---- custom producer with many threads
        RandomNumberThreadNameProducer randomNumberThreadNameProducer = new RandomNumberThreadNameProducer();
        Flux.create(randomNumberThreadNameProducer)
                .subscribe(defaultSubscriberAbc);
        for (int i = 0; i < 5; i++) {
            new Thread(randomNumberThreadNameProducer::produce).start();
        }

        Util.waitFor(1);

        // ---- take operator, it takes X elements and cancels the subscription
        Flux.range(1, 10)
                .log()
                .take(3)
                .log()
                .subscribe(System.out::println);
    }
}
