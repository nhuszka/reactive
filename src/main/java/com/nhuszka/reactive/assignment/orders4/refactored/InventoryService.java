package com.nhuszka.reactive.assignment.orders4.refactored;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class InventoryService {


    private static final Map<String, Integer> inventoryReducedCounts = new HashMap<>();

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

    public Consumer<PurchaseOrder> purchaseOrderConsumer() {
        return this::updateInventoryReducedCounts;
    }

    public Flux<String> inventoryDecrease() {
        return Flux.
                interval(Duration.ofSeconds(1))
                .map(i -> "Inventory reduced counts: " + inventoryReducedCounts);
    }
}
