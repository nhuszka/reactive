package com.nhuszka.reactive.assignment.orders4.nov;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

public class Main {

    public static void main(String[] args) {
        Flux<PuOr> orders = new OrderService().orders();

        RevServ revServ = new RevServ();

        revServ.updateRevenue(orders);

        Util.waitFor(3);

        new InvServ().updateInventories(orders);

        orders
                .subscribe(purchaseOrder -> {
                    System.out.println("NEW PO: " + purchaseOrder.getItemName() + ": " + purchaseOrder.getPrice());
                });
        revServ.revenue().subscribe(totalRev -> System.out.println("Total revenue: " + totalRev));

        Util.waitFor(60);
    }
}
