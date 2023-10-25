package com.nhuszka.reactive.assignment.orders4;

import reactor.core.publisher.Flux;

public class RevenueService {
    private final PurchaseOrderService orderService;
    private Integer totalRevenue;

    public RevenueService(PurchaseOrderService orderService) {
        this.orderService = orderService;
    }

    public Flux<Integer> revenue() {
        // get amount of price paid in the order service, store in map and
        // publish total revenue to anyone who wants to see it
        return orderService.orders()
                .doOnNext(this::updateTotalRevenue)
                .doOnComplete(() -> System.out.println("Done with getting revenue"))
                .map(purchaseOrder -> totalRevenue);
    }

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
}
