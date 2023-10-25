package com.nhuszka.reactive.assignment.orders4;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

public class OrderSystem {

    public static void main(String[] args) {
        PurchaseOrderService orderService = new PurchaseOrderService();
        InventoryService inventoryService = new InventoryService(orderService);
        RevenueService revenueService = new RevenueService(orderService);

        Flux<InventoryReducedCount> inventoryReduction = inventoryService.inventoryReduced();
        inventoryReduction.subscribe(System.out::println);

        Flux<Integer> revenue = revenueService.revenue();
        revenue.subscribe(revenueItem -> System.out.println("Actual revenue " + revenueItem));

        Util.waitFor(18);
    }
}
