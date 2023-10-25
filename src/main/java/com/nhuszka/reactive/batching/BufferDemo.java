package com.nhuszka.reactive.batching;

import com.nhuszka.reactive.util.DefaultSubscriber;
import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class BufferDemo {
    public static void main(String[] args) {
        // --- buffer with fixed size
        events(300)
                .buffer(5)
                .subscribe(new DefaultSubscriber("buff"));

        Util.waitFor(2);

        // --- buffer with timeout
        events(300)
                .buffer(Duration.ofSeconds(2))
                .subscribe(new DefaultSubscriber("time"));

        Util.waitFor(5);

        // --- buffer with buffersize+timeout
        events(30)
                .bufferTimeout(5, Duration.ofSeconds(2))
                .subscribe(new DefaultSubscriber("bs+t"));

        Util.waitFor(3);

        // --- last X items, skip oldest X items
        events(50)
                .buffer(3, 1) // last 3 items
                .subscribe(new DefaultSubscriber("last3"));

        Util.waitFor(1);


        // --- sampling: every X items, get Y items
        events(50)
                .buffer(3, 5) // last 3 items
                .subscribe(new DefaultSubscriber("every5get3items"));

        Util.waitFor(1);
    }

    private static Flux<String> events(long millisBetweenEvents) {
        return Flux.interval(Duration.ofMillis(millisBetweenEvents))
                .map(i -> "event" + i);
    }
}
