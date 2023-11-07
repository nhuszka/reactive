package com.nhuszka.reactive.assignment.orders4.nov;

import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Random;

public class OrderService {

    private final List<String> itemNames = List.of("apple", "pear", "broccoli", "potato", "tomato");
    private final int randomPriceLowerBound = 10;
    private final int randomPriceUpperBound = 20;

    public Flux<PuOr> orders() {
        return Flux.generate((synchronousSink) -> {
                    PuOr purchaseOrder = new PuOr(getRandomItemName(), getRandomPrice(randomPriceLowerBound, randomPriceUpperBound));
                    synchronousSink.next(purchaseOrder);
                })
                .delayElements(Duration.ofSeconds(1))
                .publish()
                .refCount(2)
                .cast(PuOr.class);
    }

    private String getRandomItemName() {
        int randomIndex = new Random().nextInt(itemNames.size());
        return itemNames.get(randomIndex);
    }

    private BigDecimal getRandomPrice(int lowerBound, int upperBound) {
        int randomPrice = new Random().nextInt(upperBound - lowerBound) + lowerBound;
        return new BigDecimal(randomPrice);
    }
}
