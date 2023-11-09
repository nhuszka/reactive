package com.nhuszka.reactive.assignment.buffer_bookstorerevenue6.br;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {

    private static List<String> GENRES = List.of("sci-fi", "thriller", "comedy");

    public static void main(String[] args) {

        Flux<Ord> orders = Flux
                .generate(ordFluxSink -> {
                    Ord order = new Ord(getRandomPrice(5, 10), getRandomGenre());
                    ordFluxSink.next(order);
                })
                .delayElements(Duration.ofMillis(200))
                .doOnNext(System.out::println)
                .cast(Ord.class);

//        AtomicInteger comedyRevenue = new AtomicInteger(0);
//        orders
//                .filter(ord -> ord.getGenre().equals("comedy"))
//                .buffer(Duration.ofSeconds(1))
//                .doOnNext(comedies -> {
//                    comedies.forEach(comedy -> comedyRevenue.addAndGet(comedy.getPrice()));
//                })
//                .subscribe(a -> {
//                    System.out.println("Revenue comedy " + comedyRevenue.get());
//                });

        // collectors.grouping by
        orders
                .buffer(Duration.ofSeconds(1))
                .map(comedies -> {
                    Map<String, Integer> revenueByGenres = comedies.stream().collect(Collectors.groupingBy(
                            Ord::getGenre, Collectors.summingInt(Ord::getPrice)
                    ));
                    return revenueByGenres;
                })
                .subscribe(revenueByGenres -> {
                    System.out.println("Revenue by genres " + revenueByGenres);
                });

        Util.waitFor(30);
    }

    private static String getRandomGenre() {
        int randomIndex = new Random().nextInt(GENRES.size());
        return GENRES.get(randomIndex);
    }

    private static int getRandomPrice(int lowerBound, int upperBound) {
        return new Random().nextInt(upperBound - lowerBound) + lowerBound;
    }
}
