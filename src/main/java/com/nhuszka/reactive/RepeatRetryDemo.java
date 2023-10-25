package com.nhuszka.reactive;

import com.nhuszka.reactive.util.DefaultSubscriber;
import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetrySpec;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class RepeatRetryDemo {

    private static final AtomicInteger INT = new AtomicInteger(1);

    public static void main(String[] args) {
        System.out.println("    --- REPEAT ----");
        repeat();

        Util.waitForHalfOf(1);
        System.out.println("    --- RETRY ----");
        retry();

        System.out.println("    --- RETRY customRetrySpecForDifferentErrors ----");
        customRetrySpecForDifferentErrors();
        Util.waitFor(10);
    }

    private static void repeat() {
        getIntegers()
                .repeat(2)
//                .repeat() // endless repetition
                .subscribe(new DefaultSubscriber());

        Util.waitForHalfOf(1);
        System.out.println("---repeat with error signal -> don't work");
        getIntegersErrorSignalAlways()
                .repeat(2)
                .subscribe(new DefaultSubscriber());

        Util.waitForHalfOf(1);
        System.out.println("---repeat with condition");
        getIntegers()
                .repeat(() -> INT.get() < 15) // evaluated on complete signal
                .subscribe(new DefaultSubscriber());
    }

    private static void retry() {
        getIntegersErrorSignalAlways()
//                .retry() // indefinitely
                .doOnError(err -> System.out.println("- - -error"))
                .retry(2)
                .subscribe(new DefaultSubscriber());

        Util.waitForHalfOf(1);
        System.out.println("    --- RETRY random error 5 times overcome ----");
        getIntegersErrorSignalRandom()
                .doOnError(err -> System.out.println("- - -error"))
                .retry(5)
                .subscribe(new DefaultSubscriber());

        Util.waitForHalfOf(1);
        System.out.println("    --- RETRY random error with fixed delay ----");
        getIntegersErrorSignalRandom()
                .doOnError(err -> System.out.println("- - -error"))
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)))
                .subscribe(new DefaultSubscriber());

        Util.waitFor(10);
    }

    private static Flux<Integer> getIntegers() {
        return Flux.range(1, 3)
                .doOnSubscribe(subscription -> System.out.println("Subscribed"))
                .doOnComplete(() -> System.out.println("completed-dooncomplete"))
                .map(i -> INT.getAndIncrement());
    }

    private static Flux<Integer> getIntegersErrorSignalAlways() {
        return Flux.range(1, 3)
                .doOnSubscribe(subscription -> System.out.println("Subscribed"))
                .doOnComplete(() -> System.out.println("completed-dooncomplete"))
                .map(i -> i / 0); // doing error
    }

    private static Flux<Integer> getIntegersErrorSignalRandom() {
        return Flux.range(1, 3)
                .doOnSubscribe(subscription -> System.out.println("Subscribed"))
                .doOnComplete(() -> System.out.println("completed-dooncomplete"))
                .map(i -> {
                    int i1 = new Random().nextInt(2);
                    System.out.println("Divisor" + i1);
                    return i / i1;
                }); // doing error
    }

    private static void customRetrySpecForDifferentErrors() {
        service()
                .retryWhen(RetrySpec.from(retrySignalFlux ->
                        retrySignalFlux
                                .doOnNext(retrySignal -> {
                                    System.out.println("totalRetries : "
                                            + retrySignal.totalRetries());
                                    System.out.println("totalRetriesInARow"
                                            + retrySignal.totalRetriesInARow());
                                })
                                .handle((retrySignal, sink) -> {
                                    String failureMsg = retrySignal.failure().getMessage();
                                    if (failureMsg.equals("500")) {
                                        System.out.println("status was 500, retrying ");
                                        sink.next(1);
                                    } else if (failureMsg.equals("404")) {
                                        System.out.println("status was 400, error is propagated ");
                                        sink.error(new RuntimeException("cannot do anything to "
                                                + retrySignal.failure()));
                                    }
                                })
                                .delayElements(Duration.ofSeconds(1))
                ))
                .subscribe(new DefaultSubscriber());
    }

    private static Mono<String> service() {
        return Mono.fromSupplier(() -> {
            downstreamStatusNoErrorOr500Or404(123);
            return "success";
        });
    }

    private static void downstreamStatusNoErrorOr500Or404(int ccCumber) {
        int random = new Random().nextInt(20);
        if (random < 8) {
            throw new RuntimeException("500");
        }
        if (random < 16) {
            throw new RuntimeException("404");
        }
    }
}
