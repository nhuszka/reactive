package com.nhuszka.reactive;

import com.nhuszka.reactive.util.DefaultSubscriber;
import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SinkDemo {

    public static void main(String[] args) {
        sinkOne(); // 1 value to N subscriber
        sinkManyUnicast(); // more value to 1 subscriber
        sinkManyMulticast(); // more value to more subscriber
    }

    private static void sinkOne() {
        System.out.println("  ----sink one tryEmit - subscribing");

        Sinks.One<Object> oneSink = newOne();

        Mono<Object> mono = oneSink.asMono();
        mono.subscribe(new DefaultSubscriber("onemono-subscriber"));

        oneSink.tryEmitValue("abc");
//        oneSink.tryEmitEmpty();
//        oneSink.tryEmitError(new RuntimeException("err")); // EmitResult return value contains the error

        System.out.println("  ----sink one emit failure handler");
        Sinks.One<Object> newOneSink = newOne();
        newOneSink.emitValue("abc",
                (signalType, emitResult) -> { // error handler set up, not running yet
                    System.out.println("11signal type name" + signalType.name());
                    System.out.println("11emit result name" + emitResult.name());
                    return false;
                }
        );

        newOneSink.emitValue("another value not permitted because mono closed",
                (signalType, emitResult) -> { // this error handler runs
                    System.out.println("22signal type name" + signalType.name());
                    System.out.println("22emit result name" + emitResult.name());
                    return false; // want retry??
                }
        );

        System.out.println("  ----sink one emit multiple subscriber");

        Sinks.One<Object> sinkMoreSubscriber = newOne();
        Mono<Object> monoMoreSubscribe = sinkMoreSubscriber.asMono();
        monoMoreSubscribe.subscribe(new DefaultSubscriber("subs1"));
        monoMoreSubscribe.subscribe(new DefaultSubscriber("subs2"));

        sinkMoreSubscriber.tryEmitValue("xyz");
    }

    private static void sinkManyUnicast() {
        Util.waitFor(1);

        // emitting multiple values through sink
        Sinks.Many<Object> sinkManyUnicast = newManyUnicast().onBackpressureBuffer();

        // handle through subscribers receive items
        Flux<Object> flux = sinkManyUnicast.asFlux();
        flux.subscribe(new DefaultSubscriber("sam-uni"));
        // another subscriber will not receive any message!
        flux.subscribe(new DefaultSubscriber("mike-uni"));

        sinkManyUnicast.tryEmitNext("a");
        sinkManyUnicast.tryEmitNext("b");
        sinkManyUnicast.tryEmitNext("c");
        // we control when to call the complete method

        Util.waitFor(1);


        System.out.println("Thread safety: retry threading error until success");
        Sinks.Many<Integer> sinkManyUnicastThreading = newManyUnicast().onBackpressureBuffer();
        List<Integer> list = new ArrayList<>();
        sinkManyUnicastThreading.asFlux().subscribe(list::add);

        for (int i = 0; i < 100; i++) {
            final int j = i;
            CompletableFuture.runAsync(() -> {
                sinkManyUnicastThreading.emitNext(j, (signalType, emitResult) -> true);
            });
        }

        Util.waitFor(3);
        System.out.println(list.size());
    }

    private static void sinkManyMulticast() {
        Util.waitFor(1);
        System.out.println("--- sink many multicast with buffering");
        // emitting multiple values through sink
        Sinks.Many<Object> sinkManyMulticast = newManyMulticast().onBackpressureBuffer();

        // handle through subscribers receive items
        Flux<Object> flux = sinkManyMulticast.asFlux();
        sinkManyMulticast.tryEmitNext("a");
        flux.subscribe(new DefaultSubscriber("sam-multi+history")); // first sub. gets buffered message "a"
        sinkManyMulticast.tryEmitNext("b");

        // another subscriber will also receive messages
        flux.subscribe(new DefaultSubscriber("mike-multi")); // second sub. gets only new messages
        sinkManyMulticast.tryEmitNext("c");
        // we control when to call the complete method



        Util.waitFor(1);
        System.out.println("--- sink many multicast - history disabled");
        Sinks.Many<Object> sinkManyMulticastNoHistory = newManyMulticast().directBestEffort();
        // directAllOrNothing: slow consumer won't affect another consumer's performance
//        Sinks.Many<Object> sinkManyMulticastNoHistory = newManyMulticast().directAllOrNothing();
        // handle through subscribers receive items
        Flux<Object> flux2 = sinkManyMulticastNoHistory.asFlux();
        sinkManyMulticastNoHistory.tryEmitNext("a");
        flux2.subscribe(new DefaultSubscriber("sam-multi-no-history")); // first sub.but don't get buffered items
        sinkManyMulticastNoHistory.tryEmitNext("b");

        // another subscriber will also receive messages
        flux2.subscribe(new DefaultSubscriber("mike-multi-no-history")); // second sub. gets only new messages
        sinkManyMulticastNoHistory.tryEmitNext("c");
        // we control when to call the complete method
    }

    private static Sinks.One<Object> newOne() {
        return Sinks.one();
    }

    private static Sinks.UnicastSpec newManyUnicast() {
        return Sinks.many().unicast();
    }

    private static Sinks.MulticastSpec newManyMulticast() {
        return Sinks.many().multicast();
    }
}
