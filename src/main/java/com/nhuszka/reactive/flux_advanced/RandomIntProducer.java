package com.nhuszka.reactive.flux_advanced;

import reactor.core.publisher.FluxSink;

import java.util.Random;
import java.util.function.Consumer;

public class RandomIntProducer implements Consumer<FluxSink<Integer>> {
    private FluxSink<Integer> integerFluxSink;

    @Override
    public void accept(FluxSink<Integer> integerFluxSink) {
        this.integerFluxSink = integerFluxSink;

    }

    public void produce() {
        int randomNumber = new Random().nextInt(5);
        integerFluxSink.next(randomNumber);
        integerFluxSink.complete();
    }
}
