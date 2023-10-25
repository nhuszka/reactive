package com.nhuszka.reactive;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import static com.nhuszka.reactive.util.Util.printThreadName;

public class SchedulerThreadPublishOnDemo {

    public static void main(String[] args) {
        System.out.println("------------");
        Flux<Object> flux = Flux.create(fluxSink -> {
            printThreadName("   create   ");
            for (int i = 0; i < 4; i++) {
                fluxSink.next(i);
            }
            fluxSink.complete();
        }).doOnNext(i -> printThreadName("   next-pub " + i));

        flux
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(i -> printThreadName("   next-sub " + i))
                .publishOn(Schedulers.parallel())
                .subscribe(i -> printThreadName("        subsc " + i));

        Util.waitFor(5);

        System.out.println("---     both subscribeOn and publishOn defined");

        flux
                .publishOn(Schedulers.parallel())
                .doOnNext(i -> printThreadName("   next-sub " + i)) // executed on parallel thread pool
                .subscribeOn(Schedulers.boundedElastic()) // -> flux.create, flux.doOnNext executed on bounded elastic thread pool
                .subscribe(i -> printThreadName("        subsc " + i)); // executed on parallel thread pool

        Util.waitFor(5);
    }
}
