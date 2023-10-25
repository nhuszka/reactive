package com.nhuszka.reactive.assignment.orders4;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.Duration;

public class PurchaseOrderService {

    public Flux<PurchaseOrder> orders() {
        return Flux.create((FluxSink<PurchaseOrder> fluxSink) -> {
                    fluxSink.next(new PurchaseOrder("table", 1, 20));
                    fluxSink.next(new PurchaseOrder("table", 2, 19));
                    fluxSink.next(new PurchaseOrder("chair", 1, 5));
                    fluxSink.next(new PurchaseOrder("monitor", 3, 12));
                    fluxSink.next(new PurchaseOrder("chair", 1, 6));
                    fluxSink.next(new PurchaseOrder("monitor", 1, 15));
                    fluxSink.next(new PurchaseOrder("table", 2, 21));
                    fluxSink.next(new PurchaseOrder("monitor", 1, 14));
                    fluxSink.next(new PurchaseOrder("chair", 1, 4));
                    fluxSink.next(new PurchaseOrder("table", 2, 19));
                    fluxSink.next(new PurchaseOrder("monitor", 1, 17));
                    fluxSink.complete();
                })
                .share()
                .delayElements(Duration.ofSeconds(1));
    }
}
