package com.nhuszka.reactive;

import com.nhuszka.reactive.util.Util;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.w3c.dom.ls.LSOutput;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class FluxDemo {

    public static void main(String[] args) {
        // just
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);
        flux
                .filter(i -> i % 2 == 0)
                .subscribe(item -> System.out.println(item));

        flux
                .filter(i -> i % 2 != 0)
                .subscribe(item -> System.out.println(item));

        Flux<String> empty = Flux.empty();
        empty.subscribe(System.out::println);

        Flux<Object> moreTypeObject = Flux.just(0, "a", 2.0, new Object());
        moreTypeObject.subscribe(System.out::println);

        // ---- from array/iterable
        Integer[] intArray = {1, 2, 3, 4, 5, 6, 7, 8};
        Flux.fromArray(intArray);

        List<Integer> intList = List.of(9, 8);
        Flux.fromIterable(intList);

        // ---------- from stream
        Stream<Integer> intStream = intList.stream();
        Flux.fromStream(intStream)
                .subscribe(System.out::println);

        //Flux.fromStream(intStream)
        //        .subscribe(System.out::println); // will not work!

        Flux.fromStream(() -> intList.stream())
                .subscribe(System.out::println); // will not work!

        // ---------- range + .log()
        Flux.range(3, 2)
                .log()
                .map(item -> item + "range")
                .log()
                .subscribe(System.out::println);

        // ----------
        withCustomSubscriberImplementation();

        // ---------- processing data chunks ready at the same time, vs data chunks ready one-by-one
        fluxVsList();

        // ---------- emitting data continuously with interval-duration
        Flux.interval(Duration.ofSeconds(1)).subscribe(System.out::println);
        Util.waitForHalfOf(6);

        // from Mono/Publisher
        Mono<String> mono = Mono.just("abc");
        Flux<String> fromMono = Flux.from(mono);
        acceptFlux(fromMono);

        // to Mono
        Flux.range(1, 10)
                .filter(i -> i > 3)
                .next() // emit only one item
                .subscribe(System.out::println); // 4
    }

    private static void withCustomSubscriberImplementation() {
        AtomicReference<Subscription> subscriptionRef = new AtomicReference<>();
        Subscriber<Integer> customSubscriber = new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                subscriptionRef.set(subscription);
                System.out.println("onSubscribe " + subscription);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(getData("onNext ", integer));
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError " + throwable);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };
        Flux.range(1, 2)
                .log()
                .subscribeWith(customSubscriber);

        Subscription subscription = subscriptionRef.get();
        subscription.request(1);

//        subscription.cancel(); // if cancelled, then the upcoming requests has no effect

        subscription.request(1);
    }

    private static void fluxVsList() {
        getDataAsList(5).forEach(System.out::println);
        getDataAsFlux(5).subscribe(System.out::println);
    }

    private static List<String> getDataAsList(int count) {
        List<String> dataAsList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            dataAsList.add(getData("data", i));
        }
        return dataAsList;
    }

    private static String getData(String data, int i) {
        Util.waitForHalfOf(1);
        return data + i;
    }

    private static Flux<String> getDataAsFlux(int count) {
        return Flux.range(0, count)
                .map(i -> getData("data", i));
    }

    private static void acceptFlux(Flux<String> flux) {
        flux.subscribe(System.out::println);
    }
}
