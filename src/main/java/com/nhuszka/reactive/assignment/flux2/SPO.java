package com.nhuszka.reactive.assignment.flux2;

import com.nhuszka.reactive.util.Util;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SPO {

    public static void main(String[] args) {
        new SPO().start();
    }

    private void start() {
        AtomicReference<Subscription> subscriptionAtomicReference = new AtomicReference<>();
        Subscriber<Integer> subscriber = new Subscriber<>() {

            @Override
            public void onSubscribe(Subscription subscription) {
                subscriptionAtomicReference.set(subscription);
            }

            @Override
            public void onNext(Integer i) {
                System.out.println(i);
                if (i > 120 || i < 80) {
                    subscriptionAtomicReference.get().cancel();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError");

            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        getStockPriceFlux()
                .subscribeWith(subscriber);

        subscriptionAtomicReference.get().request(Long.MAX_VALUE);

        AtomicInteger total = new AtomicInteger(0);
        AtomicInteger count = new AtomicInteger(0);
        getRandomValue(-100, 100)
                .subscribe(r -> {
                    int sum = total.addAndGet(r);
                    int countAct = count.incrementAndGet();
                    System.out.println("Act random value: " + r + " (avg: " + sum / countAct + ")");
                });

        Util.waitFor(60);
    }

    private static Flux<Integer> getRandomValue(int from, int to) {
        return Flux.interval(Duration.ofMillis(10))
                .map(a -> new Random().nextInt(to - from) + from);
    }

    private static Flux<Integer> getStockPriceFlux() {
        AtomicInteger stockPrice = new AtomicInteger(100);
        return Flux.interval(Duration.ofMillis(500))
                .map(a -> {
                    int step = new Random().nextInt(20) - 10;
                    stockPrice.set(stockPrice.get() + step);
                    return stockPrice.get();
                });
    }
}
