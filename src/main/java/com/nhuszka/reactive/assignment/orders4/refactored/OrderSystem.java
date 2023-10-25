package com.nhuszka.reactive.assignment.orders4.refactored;

import com.nhuszka.reactive.util.Util;

public class OrderSystem {

    public static void main(String[] args) {
        PurchaseOrderService orderService = new PurchaseOrderService();
        InventoryService inventoryService = new InventoryService();
        RevenueService revenueService = new RevenueService();

        orderService.orders().subscribe(inventoryService.purchaseOrderConsumer());
        orderService.orders().subscribe(revenueService.purchaseOrderConsumer());

        inventoryService.inventoryDecrease().subscribe(System.out::println);
        revenueService.revenue().subscribe(System.out::println);

        Util.waitFor(18);
    }
}
