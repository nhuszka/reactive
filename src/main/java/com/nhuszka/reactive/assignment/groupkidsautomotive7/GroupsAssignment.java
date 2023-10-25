package com.nhuszka.reactive.assignment.groupkidsautomotive7;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

public class GroupsAssignment {

    public static void main(String[] args) {
        // two different processing based on product category
        // if automotive, add 10% tax, then do packaging (printing statement of packing)
        // if kids: put 5% discount, and include one more free product

        // lastly we print the order type what it was

        Flux<ItemOrder> orders = new GroupOrderService().orders();

        Flux<GroupedFlux<String, ItemOrder>> groupedFluxOfFlux = orders
                .take(3)
                .groupBy(itemOrder -> itemOrder.getCategory());

        groupedFluxOfFlux
                .flatMap(gf -> {
                    if (isCategory(gf, "kids")) {
                        return kidsOrders(gf);
                    } else {
                        return automotiveOrders(gf);
                    }
                })
                .doOnComplete(() -> System.out.println("DONE WITH ALL ORDERS"))
                .subscribe((o) -> System.out.println("Done grouping for " + o));

        Util.waitFor(18);
    }

    private static Mono<String> automotiveOrders(GroupedFlux<String, ItemOrder> gf) {
        return gf
                .doOnNext(itemOrder -> {
                    itemOrder.setUpdatedPrice(itemOrder.getPrice() * 1.1D);
                    System.out.println("Packing automotive order " + itemOrder);
                }).then(Mono.just(("automotive")));
    }

    private static Mono<String> kidsOrders(GroupedFlux<String, ItemOrder> gf) {
        return gf
                .buffer(2)
                .doOnNext(itemOrders -> {
                    ItemOrder item = itemOrders.get(0);
                    item.setUpdatedPrice(item.getPrice() * 0.95D);
                    System.out.println("Kids order " + item);
                    if (itemOrders.size() > 1) {
                        ItemOrder freeItem = itemOrders.get(1);
                        System.out.println("Kids free item " + freeItem);
                    } else {
                        System.out.println("No free item!?");
                    }
                }).then(Mono.just(("kids")));
    }

    private static boolean isCategory(GroupedFlux<String, ItemOrder> categoryFlux, String category) {
        return categoryFlux.key().equals(category);
    }
}
