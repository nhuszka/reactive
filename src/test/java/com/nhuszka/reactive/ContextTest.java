package com.nhuszka.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;
import reactor.util.context.Context;

public class ContextTest {

    @Test
    public void testContextPass_ExceptionUnauthenticated() {
        StepVerifier.create(getWelcomeMsgUserContextCheckAuthenticated())
                .verifyError();
    }

    @Test
    public void testContextFail_ExceptionUnauthenticated() {
        StepVerifier.create(getWelcomeMsgUserContextCheckAuthenticated())
                .expectNext("Welcome sam")
                .verifyComplete();
    }

    @Test
    public void testContextPass_ContextProvided() {
        StepVerifierOptions options = StepVerifierOptions.create()
                .withInitialContext(Context.of("user", "sam"));
        StepVerifier.create(getWelcomeMsgUserContextCheckAuthenticated(), options)
                .expectNext("Welcome sam (authenticated)")
                .verifyComplete();
    }

    private static Mono<Object> getWelcomeMsgUserContextCheckAuthenticated() {
        return Mono.deferContextual(ctx -> {
            boolean hasUserKey = ctx.hasKey("user");
            if (hasUserKey) {
                return getStringMono("Welcome " + ctx.get("user") + " (authenticated)");
            }
            return Mono.error(new RuntimeException("unauthenticated"));
        });
    }

    private static Mono<String> getStringMono(String msg) {
        return Mono.fromSupplier(() -> msg);
    }

}
