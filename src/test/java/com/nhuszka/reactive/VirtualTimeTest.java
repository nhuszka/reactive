package com.nhuszka.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class VirtualTimeTest {

    @Test
    public void testRunsFor20SecondsTooSlowToRun() {
        StepVerifier.create(timeConsumingFluxTotal20SecOnNextEvery5Sec())
                .expectNext("1a", "2a", "3a", "4a")
                .verifyComplete();
    }

    @Test
    public void testVirtualTimePassesQuicklyForSlowFlux() {
        StepVerifier.withVirtualTime(() -> timeConsumingFluxTotal20SecOnNextEvery5Sec())
                .thenAwait(Duration.ofSeconds(30))
                .expectNext("1a", "2a", "3a", "4a")
                .verifyComplete();
    }

    @Test
    public void testPassingVirtualTimeTest_ExpectNoEventForTheFirst3SecondsExceptSubscription() {
        StepVerifier.withVirtualTime(() -> timeConsumingFluxTotal20SecOnNextEvery5Sec())
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(3))
                .thenAwait(Duration.ofSeconds(30))
                .expectNext("1a", "2a", "3a", "4a")
                .verifyComplete();
    }

    @Test
    public void testVFailingVirtualTimeTest_ExpectNoEventForTheFirst3SecondsExceptSubscription() {
        StepVerifier.withVirtualTime(() -> timeConsumingFluxTotal20SecOnNextEvery5Sec())
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(6))
                .thenAwait(Duration.ofSeconds(30))
                .expectNext("1a", "2a", "3a", "4a")
                .verifyComplete();
    }

    @Test
    public void testRunsExpectingNothingHappensFor1SecAfterSubscription() {
        StepVerifier.create(timeConsumingFluxTotal6SecOnNextEvery2Sec())
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1))
                .expectNext("5b", "6b", "7b")
                .verifyComplete();
    }

    private Flux<String> timeConsumingFluxTotal20SecOnNextEvery5Sec() {
        return Flux.range(1, 4)
                .delayElements(Duration.ofSeconds(5))
                .map(i -> i + "a");
    }

    private Flux<String> timeConsumingFluxTotal6SecOnNextEvery2Sec() {
        return Flux.range(5, 3)
                .delayElements(Duration.ofSeconds(2))
                .map(i -> i + "b");
    }
}
