package com.nhuszka.reactive.assignment.flux2;

import com.nhuszka.reactive.util.Util;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class StockPriceObserver {

    private static final int LOW_THRESHOLD = 90;
    private static final int HIGH_THRESHOLD = 110;

    public static void main(String[] args) {
        AtomicReference<Subscription> subscriptionRef = new AtomicReference<>();
        AtomicInteger actualPrice = new AtomicInteger();

        Flux<Integer> stockPrice = new StockPriceGenerator().stockPrice();
        stockPrice.subscribeWith(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("onSubscribe");
                subscriptionRef.set(subscription);
            }

            @Override
            public void onNext(Integer integer) {
                actualPrice.set(integer);
                System.out.println("Actual stock price: " + integer);
                Util.waitForHalfOf(1);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error during getting the stock price: " + throwable.getMessage());
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("No more stock price check.");
            }
        });

        boolean priceOutOfRange;
        Subscription subscription = subscriptionRef.get();
        do {
            subscription.request(1L);
            int actualPriceValue = actualPrice.get();
            priceOutOfRange = actualPriceValue >= HIGH_THRESHOLD || actualPriceValue <= LOW_THRESHOLD;
        } while (!priceOutOfRange);

        System.out.println("Exit with price value " + actualPrice.get());
    }
}
