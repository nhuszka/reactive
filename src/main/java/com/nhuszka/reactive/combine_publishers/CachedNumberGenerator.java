package com.nhuszka.reactive.combine_publishers;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CachedNumberGenerator {



    private List<Integer> cache = new ArrayList<>();

    public Flux<Integer> generateIntegers() {
        return Flux.generate(sink -> {
                    System.out.println("generated fresh");
                    Util.waitFor(1);
                    int i = new Random().nextInt(Integer.MAX_VALUE);
                    sink.next(i);

                    cache.add(i);
                })
                .cast(Integer.class)
                .startWith(getFromCache());
    }

    private Flux<Integer> getFromCache() {
        return Flux.fromIterable(cache);
    }
}
