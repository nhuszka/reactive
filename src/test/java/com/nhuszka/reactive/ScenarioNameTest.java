package com.nhuszka.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

public class ScenarioNameTest {

    private Flux<String> flux_a_b_c_Complete = Flux.just("a", "b", "c");

    @Test
    public void testScenarioName() {
        StepVerifierOptions options = StepVerifierOptions.create()
                .scenarioName("abc-test");

        StepVerifier.create(flux_a_b_c_Complete, options)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void testStepName() {
        StepVerifier.create(flux_a_b_c_Complete)
                .expectNext("a")
                .as("step-checking-A")
                .expectNext("b")
                .as("step-checking-B")
                .expectNext("zzz")
                .as("step-checking-ZZZ")
                .verifyComplete();
    }
}
