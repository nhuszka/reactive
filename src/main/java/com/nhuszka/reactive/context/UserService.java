package com.nhuszka.reactive.context;

import reactor.util.context.Context;

import java.util.Map;
import java.util.function.Function;

public class UserService {

    private static final Map<String, String> USER_CATEGORY = Map.of(
            "sam", "standard",
            "mike", "prime"
    );

    public static Function<Context, Context> userCategoryCtx() {
        return context -> {
            String user = context.get("user").toString();
            String category = USER_CATEGORY.get(user);
            return context.put("category", category);
        };
    }
}
