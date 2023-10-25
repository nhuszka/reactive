package com.nhuszka.reactive;

import com.nhuszka.reactive.util.DefaultSubscriber;
import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.Stream;

public class ColdHotPublisherDemo {

    public static void main(String[] args) {
        // --- cold publishers
        Flux<String> netflixStream = Flux.fromStream(() -> getMovie())
                .delayElements(Duration.ofSeconds(1));

        netflixStream.subscribe(new DefaultSubscriber("sam-netflix"));
        Util.waitFor(1);

        netflixStream.subscribe(new DefaultSubscriber("tom-netflix"));
        Util.waitFor(3);

        // --- hot publisher (created with .share() )
        Flux<String> tvStream = Flux.fromStream(() -> getMovie())
                .delayElements(Duration.ofSeconds(1))
                .share();

        tvStream.subscribe(new DefaultSubscriber("sam-tv"));
        Util.waitFor(2);

        tvStream.subscribe(new DefaultSubscriber("tom-tv")); // Tom arrived late, has missed the scene1
        Util.waitFor(3);

        // --- hot publisher (created with .publish().refCount() )
        Flux<String> tvStream2 = Flux.fromStream(() -> getMovie())
                .delayElements(Duration.ofSeconds(1))
                .publish() // this is a ConnectableFlux
                .refCount(1);

        tvStream2.subscribe(new DefaultSubscriber("sam-tv"));
        Util.waitFor(2);

        tvStream2.subscribe(new DefaultSubscriber("tom-tv")); // Tom arrived late, has missed the scene1
        Util.waitFor(3);

        // --- hot-hot publisher ( do not restart on new joiner )
        Util.waitFor(5);

        Flux<String> reallyHotTvStream = Flux.fromStream(() -> getMovie())
                .delayElements(Duration.ofSeconds(1))
                .publish() // this is a ConnectableFlux
                .autoConnect(0) // automatically starts
                .doOnEach(s -> System.out.println("Playing " + s))
                .doOnSubscribe(subscription -> System.out.println("Someone new is watching"));

        Util.waitForHalfOf(3);
        reallyHotTvStream.subscribe(new DefaultSubscriber("sam-hothot"));
        Util.waitFor(1);

        System.out.println("Tom about to join");
        reallyHotTvStream.subscribe(new DefaultSubscriber("tom-hothot"));


        // --- caching publisher
        Util.waitFor(5);
        Flux<String> cachedTvStream = Flux.fromStream(() -> getMovie())
                .delayElements(Duration.ofSeconds(1))
                .cache(); // this is a ConnectableFlux

        cachedTvStream.subscribe(new DefaultSubscriber("sam-cached"));
        Util.waitFor(4);

        cachedTvStream.subscribe(new DefaultSubscriber("tom-cached")); // gets almost immediately (cached items)
        Util.waitFor(1);
    }

    private static Stream<String> getMovie() {
        System.out.println("Movie streaming request");
        return Stream.of("scene1", "scene2", "scene3");
    }
}
