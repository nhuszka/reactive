package com.nhuszka.reactive;

import com.nhuszka.reactive.util.DefaultSubscriber;
import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class OverflowStrategyDemo {

    public static void main(String[] args) {
        // 75% of 16 =  2
        System.setProperty("reactor.bufferSize.small", "16");

        List<Object> list = new ArrayList<>();

        Flux.create(sink -> {
                    for (int i = 0; i < 201 && !sink.isCancelled(); i++) {
                        sink.next(i);
                        System.out.println("Pushed " + i);
                        Util.waitForMillis(1);
                    }
                    sink.complete();
                })
                .onBackpressureBuffer()
                .onBackpressureDrop(i -> list.add(i))
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(i -> Util.waitForMillis(10))
                .subscribe(new DefaultSubscriber());

        System.out.println(list);
    }
}
