package com.nhuszka.reactive.context;

import com.nhuszka.reactive.util.DefaultSubscriber;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class ContextDemo {
    public static void main(String[] args) {
        getWelcomeMsg()
                .subscribe(new DefaultSubscriber());

        getWelcomeMsgUserContextCheckAuthenticated()
                .contextWrite(Context.of("user", "sam"))
                .subscribe(new DefaultSubscriber());

        // context goes upwards -> upstream sees jake
        getWelcomeMsgUserContextCheckAuthenticated()
                .contextWrite(Context.of("user", "jake")) // will replace the context (sam)
                .contextWrite(Context.of("user", "sam"))
                .subscribe(new DefaultSubscriber());

        // to provide additional info, key should be different -> upstream sees sam (user)
        getWelcomeMsgUserContextCheckAuthenticated()
                .contextWrite(Context.of("users", "jake"))
                .contextWrite(Context.of("user", "sam"))
                .subscribe(new DefaultSubscriber());

        // when upstream wants user info in UPPERCASE format, but downstream provides it lowercase
        // update context, should know the value and key
        getWelcomeMsgUserContextCheckAuthenticated()
                .contextWrite(context ->
                        context.put("user", context.get("user").toString().toUpperCase()) )
                .contextWrite(Context.of("user", "sam"))
                .subscribe(new DefaultSubscriber());
    }

    // ~ the API endpoint
    private static Mono<String> getWelcomeMsg() {
        return getStringMono("Welcome");
    }

    // getting the context object provided by downstream

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
