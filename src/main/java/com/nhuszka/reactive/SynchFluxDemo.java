package com.nhuszka.reactive;

import com.nhuszka.reactive.util.DefaultSubscriber;
import reactor.core.publisher.Flux;

import java.util.Random;

public class SynchFluxDemo {
    public static void main(String[] args) {
        DefaultSubscriber defaultSubscriberAbc = new DefaultSubscriber("abc");
        // -- emit endless loop, but take only 2
        Flux.generate(
                synchronousSink -> synchronousSink.next(new Random().nextInt())
        )
                .take(2)
                .subscribe(defaultSubscriberAbc);


        // --- emit in endless loop internally but takes only one, then complete
        Flux.generate(synchronousSink -> {
            synchronousSink.next(new Random().nextInt());
            synchronousSink.complete();
        }).take(2).subscribe(defaultSubscriberAbc);

        // -- emit until condition
        Flux.generate(synchronousSink -> {
            int randomNum = new Random().nextInt(10);
            synchronousSink.next(randomNum);
            if (randomNum == 5) {
                synchronousSink.complete(); // it is like a loop, exit on condition
            }
        }).subscribe(defaultSubscriberAbc);


        // --- emit in endless loop internally
        Flux.generate(synchronousSink -> {
            synchronousSink.next(new Random().nextInt());
        })
//                .subscribe(defaultSubscriberAbc)
        ;
    }
}
