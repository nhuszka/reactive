package com.nhuszka.reactive.assignment.buffer_bookstorerevenue6;

import com.nhuszka.reactive.util.DefaultSubscriber;
import com.nhuszka.reactive.util.Util;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RevenueAssignment {


    public static void main(String[] args) {
        List<String> interestedCategories = List.of(
                "sci-fi",
                "fantasy",
                "drama"
        );

        final AtomicInteger revenue = new AtomicInteger(0);
        new OrderService().orders()
                .filter(o -> interestedCategories.contains(o.getCategory()))
                .buffer(Duration.ofSeconds(3))
                .map(orders -> {
                    for (Order order : orders) {
                        revenue.addAndGet(order.getPrice());
                    }
                    return revenue.get();
                })
                .subscribe(new DefaultSubscriber("Revenue from books"));

        Util.waitFor(12);
    }
}
