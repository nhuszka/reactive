package com.nhuszka.reactive;

import com.nhuszka.reactive.assignment.sinks8.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class AssertTest {

    @Test
    public void testAssertNext() {
        Mono<Message> mono = Mono.fromSupplier(() ->
                new Message("messagetext", "sendername", "now"));

        StepVerifier.create(mono)
                .assertNext(msg -> Assertions.assertNotNull(msg.toString()))
                .verifyComplete();
    }


    @Test
    public void testAssertNextSlowOperationTestMethodWaits() {
        Mono<Message> mono = Mono
                .fromSupplier(() ->
                        new Message("messagetext", "sendername", "now"))
                .delayElement(Duration.ofSeconds(2));

        StepVerifier.create(mono)
                .assertNext(msg -> Assertions.assertNotNull(msg.toString()))
                .verifyComplete();
    }

    @Test
    public void testAssertNextSlowOperationTestMethodTimeout() {
        Mono<Message> mono = Mono
                .fromSupplier(() ->
                        new Message("messagetext", "sendername", "now"))
                .delayElement(Duration.ofSeconds(2));

        StepVerifier.create(mono)
                .assertNext(msg -> Assertions.assertNotNull(msg.toString()))
                .expectComplete()
                .verify(Duration.ofSeconds(1));
    }
}
