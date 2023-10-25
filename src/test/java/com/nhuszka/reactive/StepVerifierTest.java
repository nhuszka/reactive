package com.nhuszka.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class StepVerifierTest {

    private Flux<Integer> flux_1_2_3_Complete =
            Flux.just(1, 2, 3);
    private Flux<Integer> flux_1_2_to_50_Complete =
            Flux.range(1, 50);
    private Flux<Integer> flux_runtimeException =
            Flux.error(new RuntimeException("error"));

    @Test
    public void testExpectedValue() {
        StepVerifier.create(flux_1_2_3_Complete)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
//                .expectNext(1, 2, 3) // this means the same
                .verifyComplete();
    }

    @Test
    public void testUnexpectedValue() {
        StepVerifier.create(flux_1_2_3_Complete)
                .expectNext(1)
                .expectNext(2)
                .expectNext(4)
                .verifyComplete();
    }

    @Test
    public void testErrors() {
        Flux<Integer> withError = Flux.concat(
                flux_1_2_3_Complete, flux_runtimeException
        );

        StepVerifier.create(withError)
                .expectNext(1, 2, 3)
                .verifyError();

        StepVerifier.create(withError)
                .expectNext(1, 2, 3)
                .verifyError(RuntimeException.class);

        StepVerifier.create(withError)
                .expectNext(1, 2, 3)
                .verifyErrorMessage("error");
    }

    @Test
    public void testExpectCount() {
        StepVerifier.create(flux_1_2_to_50_Complete)
                .expectNextCount(50)
                .verifyComplete();
    }

    @Test
    public void testExpectWhileConditionTrue() {
        StepVerifier.create(flux_1_2_to_50_Complete)
                .thenConsumeWhile(i -> i < 100)
                .verifyComplete();
    }

    @Test
    public void testExpectWhileConditionFalse() {
        StepVerifier.create(flux_1_2_to_50_Complete)
                .thenConsumeWhile(i -> i < 40)
                .verifyComplete();
    }
}
