package com.nhuszka.reactive.assignment.groupkidsautomotive7.gka;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class Main {

    private static List<String> CATEGORIES = List.of("kids", "automotive");

    public static void main(String[] args) {
        Flux<GOrder> orders = Flux
                .generate((synchronousSink) -> {
                    GOrder order = new GOrder(getRandomGenre(), getRandomPrice(5, 10));
                    synchronousSink.next(order);
                })
                .delayElements(Duration.ofSeconds(1))
                .cast(GOrder.class)
                .doOnNext(gOrder -> System.out.println("Order generated " + gOrder));

        orders
                .groupBy(GOrder::getCategory)
                .subscribe(gf -> processOrders(gf, gf.key()));

        Util.waitFor(30);
    }

    private static void processOrders(Flux<GOrder> ordersByKey, String key) {
        if (key.equals("kids")) {
            ordersByKey
                    .map(order -> new GOrder(order.getCategory(), order.getPrice() * 0.9))
                    .buffer(2)
                    .subscribe(items -> System.out.println("Creating a kids order with two items " + items));
        } else {
            ordersByKey
                    .map(order -> new GOrder(order.getCategory(), order.getPrice() * 1.1))
                    .doOnNext(gOrder -> System.out.println("Packing an automotive item"))
                    .subscribe(items -> System.out.println("Creating an automotive item" + items));
        }
    }

    private static String getRandomGenre() {
        int randomIndex = new Random().nextInt(CATEGORIES.size());
        return CATEGORIES.get(randomIndex);
    }

    private static int getRandomPrice(int lowerBound, int upperBound) {
        return new Random().nextInt(upperBound - lowerBound) + lowerBound;
    }
}
