package com.nhuszka.reactive.assignment.flux2;

import reactor.core.publisher.Flux;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class StockPriceGenerator {

    private static final int LOW_STEP = -5;
    private static final int HIGH_STEP = 5;
    private static AtomicInteger price;

    private int getRandomStep() {
        return new Random().nextInt(HIGH_STEP - LOW_STEP) + LOW_STEP;
    }

    Flux<Integer> stockPrice() {
        Supplier<Integer> stockPriceSupplier = () -> {
            if (price == null) {
                price = new AtomicInteger(100);
                return price.get();
            }
            return price.addAndGet(getRandomStep());
        };
        return Flux.fromStream(Stream.generate(stockPriceSupplier));
    }
}
