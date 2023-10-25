package com.nhuszka.reactive.assignment.buffer_bookstorerevenue6;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class OrderService {

    private static final List<String> CATEGORIES = List.of(
            "sci-fi",
            "documentary",
            "kid",
            "fantasy",
            "educational",
            "drama",
            "historical"
    );
    private static final int MAX_PRICE = 100;

    public Flux<Order> orders() {
        return Flux.interval(Duration.ofMillis(500))
                .map(i -> createRandomOrder());
    }

    private static Order createRandomOrder() {
        int randomIndex = new Random().nextInt(CATEGORIES.size());
        String randomCategory = CATEGORIES.get(randomIndex);
        int randomPrice = new Random().nextInt(MAX_PRICE);
        System.out.println("Order book" + randomCategory + ": "+ randomPrice);
        return new Order(randomCategory, randomPrice);
    }
}
