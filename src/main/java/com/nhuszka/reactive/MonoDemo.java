package com.nhuszka.reactive;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MonoDemo {

    public static void main(String[] args) {
        // simple publisher
        Mono<Integer> mono = Mono.just(1);
        // use it if we have data already (no calculation needed ~ eager)
        // because here getName is executed even if there is no subscriber
        Mono<String> monoJustExecuted = Mono.just(getName());

        // use it if we have to calculate data, the inside is executed if there is any subscriber (lazy)
        Mono<String> monoFromSupplier = Mono.fromSupplier(() -> getName());

        // ----------

        Consumer<Integer> subscriberConsumer = i -> System.out.println("Received: " + 1);
        mono.subscribe(subscriberConsumer);

        Mono<String> mono2 = Mono.just("abc");
        // it emits the item, and the consumer is called (does nothing)
        mono2.subscribe();

        // ----------

        // we can specify onNext, onError, onComplete consumers
        Mono<String> mono3 = Mono.just("def");
        Consumer<String> onNextConsumer = item -> {
            System.out.println("Item emitted: " + item);
        };
        Consumer<Throwable> onErrorConsumer = err -> {
            System.out.println("Error: " + err);
        };
        Runnable onSuccessConsumer = () -> System.out.println("onComplete");
        mono3.subscribe(onNextConsumer, onErrorConsumer, onSuccessConsumer);

        // ----------

        // we can decorate the mono publisher pipeline: filter, map, flatmap, etc.
        Mono<Integer> monoDecorated1 = Mono.just("decor").map(s -> s.toUpperCase(Locale.ENGLISH)).map(String::length);
        monoDecorated1.subscribe(item -> System.out.println("Decorated: " + item));


        Mono<Integer> monoDecoratedError = Mono.just(1).map(item -> item / 0);

        monoDecoratedError.subscribe(System.out::println, error -> System.out.println("Error was " + error), () -> System.out.println("Done"));

        // ----------


        getNameMono(); // 1
        getNameMono(); // 2
        getNameMono()
				.subscribeOn(Schedulers.boundedElastic())
				.subscribe(item -> System.out.println(item)); // 4
        getNameMono(); // 3


        // ----------

        Mono.fromFuture(getNameCompletableFuture())
                .subscribe(item -> System.out.println(item));
        waitSomeTime();

        // ----------

        Mono.fromRunnable(getNameRunnable())
                .subscribe(
                        item -> System.out.println("on next: " + item),
                        error -> System.out.println("on error: " + error),
                        () -> System.out.println("onComplete happened")
                );
        waitSomeTime();
    }

    private static Runnable getNameRunnable() {
        return () -> {
            waitSomeTime();
            System.out.println("aName");
        };
    }


    private static CompletableFuture<String> getNameCompletableFuture() {
        return CompletableFuture.supplyAsync(() -> {
            waitSomeTime();
            return "aName";
        });
    }

    private static String getName() {
        System.out.println("Calculating name...");
        waitSomeTime();
        return "Marshall";
    }

    private static Mono<String> getNameMono() {
        System.out.println("Calculating name...");
        return Mono.fromSupplier(() -> {
            System.out.println("Generating name...");
            waitSomeTime();
            return "Marshall";
        });
    }

    private static void waitSomeTime() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
