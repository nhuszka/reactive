package com.nhuszka.reactive.assignment.orders4.refactored;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.function.Consumer;

public class RevenueService {
    private Integer totalRevenue;

    private void updateTotalRevenue(PurchaseOrder purchaseOrder) {
        Integer amount = purchaseOrder.getAmount();
        Integer pricePerAmount = purchaseOrder.getPricePerAmount();

        int totalPrice = amount * pricePerAmount;
        if (totalRevenue == null) {
            totalRevenue = totalPrice;
        } else {
            totalRevenue += totalPrice;
        }
    }

    public Consumer<? super PurchaseOrder> purchaseOrderConsumer() {
        return this::updateTotalRevenue;
    }

    public Flux<String> revenue() {
        return Flux
                .interval(Duration.ofSeconds(1))
                .map(i -> "Total revenue: " + totalRevenue.toString());
    }
}
