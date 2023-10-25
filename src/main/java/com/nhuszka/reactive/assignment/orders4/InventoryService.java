package com.nhuszka.reactive.assignment.orders4;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

public class InventoryService {

    private final PurchaseOrderService orderService;

    private static final Map<String, Integer> inventoryReducedCounts = new HashMap<>();

    public InventoryService(PurchaseOrderService orderService) {
        this.orderService = orderService;
    }

    public Flux<InventoryReducedCount> inventoryReduced() {
        // get amount of inventory from the purchase service
        // publish events about the inventory count for any users
        return orderService.orders()
                .doOnNext(this::updateInventoryReducedCounts)
                .doOnComplete(() -> System.out.println("Done with getting inventory reductions"))
                .map(InventoryService::getInventoryReducedCount);
    }

    private void updateInventoryReducedCounts(PurchaseOrder purchaseOrder) {
        String inventoryName = purchaseOrder.getInventoryName();
        Integer amount = purchaseOrder.getAmount();

        // TODO refactor
        if (inventoryReducedCounts.containsKey(inventoryName)) {
            inventoryReducedCounts.put(inventoryName, amount + inventoryReducedCounts.get(inventoryName));
        } else {
            inventoryReducedCounts.put(inventoryName, amount);
        }
        Util.waitForHalfOf(1);
    }

    private static InventoryReducedCount getInventoryReducedCount(PurchaseOrder purchaseOrder) {
        String inventoryName = purchaseOrder.getInventoryName();
        return new InventoryReducedCount(inventoryName, inventoryReducedCounts.get(inventoryName));
    }
}
