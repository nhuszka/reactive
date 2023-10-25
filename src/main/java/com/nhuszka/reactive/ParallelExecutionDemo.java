package com.nhuszka.reactive;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ParallelExecutionDemo {

    public static void main(String[] args) {
        // --- parallelism on arraylist
        List<Integer> list = new ArrayList<>();

        Flux.range(1, 10000)
                .parallel()
                .runOn(Schedulers.parallel())
//                .doOnNext(i -> printThreadName("next " + i))
                .subscribe(i -> list.add(i));

        Util.waitFor(5);

        System.out.println(list.size());
        // size should be 1000, but ArrayList is not thread safe - missing data

        System.out.println("---------------------");

        // --- parallelism on Vector
        Vector<Integer> vector = new Vector<>();

        Flux.range(1, 10000)
                .parallel(7) // control the num of threads used
                .runOn(Schedulers.parallel())
//                .doOnNext(i -> printThreadName("next " + i))
                .subscribe(i -> vector.add(i));

        Util.waitFor(5);

        System.out.println(vector.size());
        // size is 10000, because Vector is thread safe
    }
}
