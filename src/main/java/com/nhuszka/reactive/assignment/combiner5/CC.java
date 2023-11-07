package com.nhuszka.reactive.assignment.combiner5;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class CC {



    public static void main(String[] args) {
        final double initialPrice = 10000;
        final double stepPerSecond = 100;

        AtomicReference<Double> currentPrice = new AtomicReference<>(initialPrice);
        Flux<Double> pricePerMonth = Flux.interval(Duration.ofSeconds(1))
                .map(event -> {
                    double updatedCurrentPrice = currentPrice.get() - stepPerSecond;
                    currentPrice.set(updatedCurrentPrice);
                    return updatedCurrentPrice;
                })
                .doOnNext(ppm -> System.out.println("PricePerMonth " + ppm));

        Flux<Double> demandPerQuarter = Flux.interval(Duration.ofSeconds(3))
                .map(event -> {
                    double demand = (new Random().nextInt(120 - 80) + 80) / 100d;
                    return demand;
                })
                .doOnNext(dpq -> System.out.println("DemandPerQuarter " + dpq));

        Flux.zip(pricePerMonth, demandPerQuarter, (ppm, ppl) -> ppm * ppl)
                .subscribe(actPrice -> System.out.println("Q price computed " + actPrice));

        Util.waitFor(10);
    }
}
