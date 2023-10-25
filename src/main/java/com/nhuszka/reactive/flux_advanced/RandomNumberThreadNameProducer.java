package com.nhuszka.reactive.flux_advanced;

import reactor.core.publisher.FluxSink;

import java.util.Random;
import java.util.function.Consumer;

public class RandomNumberThreadNameProducer implements Consumer<FluxSink<String>> {
    private FluxSink<String> strFluxSink;

    @Override
    public void accept(FluxSink<String> integerFluxSink) {
        this.strFluxSink = integerFluxSink;

    }

    public void produce() {
        int randomNumber = new Random().nextInt(5);
        String threadName = Thread.currentThread().getName();
        strFluxSink.next(threadName + " - " + randomNumber);
    }
}
