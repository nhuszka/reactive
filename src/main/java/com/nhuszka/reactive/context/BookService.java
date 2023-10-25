package com.nhuszka.reactive.context;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BookService {

    private static final Map<String, Integer> ALLOWED_ATTEMPTS_BY_USER_CATEGORY = new HashMap<>();

    static {
        ALLOWED_ATTEMPTS_BY_USER_CATEGORY.put("standard", 2);
        ALLOWED_ATTEMPTS_BY_USER_CATEGORY.put("prime", 3);
    }

    public static Mono<String> getBook() {
        return Mono.deferContextual(context -> {
                    if (context.get("allow")) {
                        return Mono.just("BookData");
                    }
                    return Mono.error(
                            new RuntimeException("not allowed call error")
                    );
                })
                .contextWrite(rateLimiterCtx());
    }

    public static Function<Context, Context> rateLimiterCtx() {
        return context -> {
            if (context.hasKey("category")) {
                String category = context.get("category").toString();
                Integer attempts = ALLOWED_ATTEMPTS_BY_USER_CATEGORY.get(category);
                if (attempts > 0) {
                    ALLOWED_ATTEMPTS_BY_USER_CATEGORY.put(category, attempts - 1);
                    return context.put("allow", true);
                }
            }
            return context.put("allow", false);
        };
    }
}
