package com.nhuszka.reactive;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import static com.nhuszka.reactive.util.Util.printThreadName;

public class SchedulerThreadsSubscribeOnDemo {

    public static void main(String[] args) {
        Flux<Object> flux = Flux.create(fluxSink -> {
            printThreadName("    create ");
            fluxSink.next(1);
        }).doOnNext(i -> printThreadName("    next " + i));

        flux
                .doFirst(() -> printThreadName("first3.1")) // this will run on boundedElastic thread
                .doFirst(() -> printThreadName("first2.1")) // this will run on boundedElastic thread
                .subscribeOn(Schedulers.boundedElastic())
                .doFirst(() -> printThreadName("first1.1"))
                .subscribe(o -> printThreadName("    sub" + o));

        Util.waitForHalfOf(1);
        System.out.println("---------");

        Runnable runnable = () -> flux
                .doFirst(() -> printThreadName("first3.2")) // this will run on boundedElastic thread
                .doFirst(() -> printThreadName("first2.2")) // this will run on boundedElastic thread
                .subscribeOn(Schedulers.boundedElastic())
                .doFirst(() -> printThreadName("first1.2"))
                .subscribe(o -> printThreadName("    sub" + o));

        new Thread(runnable).start();
        new Thread(runnable).start();

        Util.waitFor(5);

        System.out.println("------------ running on the same thread in the thread pools" +
                " (separate thread pools per subscriber)");
        Flux<Object> fluxNoScheduling = Flux.create(fluxSink -> {
            printThreadName("    create ");
            for (int i = 0; i < 4; i++) {
                fluxSink.next(i);
                Util.waitFor(1);
            }
            fluxSink.complete();
        }).doOnNext(i -> printThreadName("    next " + i));

        fluxNoScheduling
                .subscribeOn(Schedulers.boundedElastic()) // set up own scheduler
                .doFirst(() -> printThreadName("first1.2"))
                .subscribe(o -> printThreadName("    sub" + o));

        fluxNoScheduling
                .subscribeOn(Schedulers.parallel()) // set up own scheduler
                .doFirst(() -> printThreadName("first1.2"))
                .subscribe(o -> printThreadName("    sub" + o));

        Util.waitFor(5);

        System.out.println("------------ running on different thread in the thread pool");
        Flux<Object> fluxNoScheduling2 = Flux.create(fluxSink -> {
            printThreadName("    create ");
            for (int i = 0; i < 4; i++) {
                fluxSink.next(i);
                Util.waitFor(1);
            }
            fluxSink.complete();
        }).doOnNext(i -> printThreadName("    next " + i));

        Runnable runnable2 = () -> fluxNoScheduling2
                .subscribeOn(Schedulers.boundedElastic())
                .doFirst(() -> printThreadName("first1.2"))
                .subscribe(o -> printThreadName("    sub" + o));

        for (int i = 0; i < 5; i++) {
            new Thread(runnable2).start();
        }

        Util.waitFor(5);
    }
}
