package com.nhuszka.reactive.assignment.groupkidsautomotive7;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GroupOrderService {

    private static AtomicInteger ID = new AtomicInteger(1);

    private static final List<String> CATEGORIES = List.of(
            "automotive",
            "kids"
    );
    private static final int MAX_PRICE = 100;

    public Flux<ItemOrder> orders() {
        return Flux.interval(Duration.ofMillis(500))
                .map(i -> createRandomOrder());
    }

    private static ItemOrder createRandomOrder() {
        int randomIndex = new Random().nextInt(CATEGORIES.size());

        String randomCategory = CATEGORIES.get(randomIndex);
        int randomPrice = new Random().nextInt(MAX_PRICE);

        ItemOrder itemOrder = new ItemOrder(ID.getAndAdd(1), randomCategory, randomPrice);
        System.out.println("producing item order " + itemOrder);
        return itemOrder;
    }
}
