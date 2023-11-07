package com.nhuszka.reactive.assignment.orders4.nov;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class RevServ {

    private final Map<String, BigDecimal> revenue = new HashMap<>();

    public void updateRevenue(Flux<PuOr> purchaseOrders) {
        purchaseOrders
                .doOnNext(purchaseOrder -> {
                    revenue.putIfAbsent(purchaseOrder.getItemName(), BigDecimal.ZERO);
                    revenue.put(purchaseOrder.getItemName(), revenue.get(purchaseOrder.getItemName()).add(purchaseOrder.getPrice()));
                })
                .subscribe();
    }

    public Flux<BigDecimal> revenue() {
        return Flux.generate(fluxSink -> {
            Util.waitFor(2);
            fluxSink.next(computeTotalRevenue());
        });
    }

    private BigDecimal computeTotalRevenue() {
        return revenue.keySet().stream()
                .map(revenue::get)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
