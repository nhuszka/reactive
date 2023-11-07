package com.nhuszka.reactive.assignment.orders4.nov;

import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class InvServ {

    private final Map<String, BigDecimal> inventory = new HashMap<>();

    public void updateInventories(Flux<PuOr> purchaseOrders) {
        purchaseOrders
                .doOnNext(purchaseOrder -> {
                    inventory.putIfAbsent(purchaseOrder.getItemName(), BigDecimal.ZERO);
                    inventory.put(purchaseOrder.getItemName(), inventory.get(purchaseOrder.getItemName()).add(BigDecimal.ONE));
                    System.out.println("inventory : " + inventory);
                })
                .subscribe();
    }
}
