package com.nhuszka.reactive.context;

import com.nhuszka.reactive.util.DefaultSubscriber;
import reactor.util.context.Context;

public class ContextRateLimiterDemo {
    public static void main(String[] args) {
        System.out.println("anonymous user calling");
        // not allowed -> no user set in context
        BookService.getBook()
                .subscribe(new DefaultSubscriber());

        System.out.println("sam calling");
        // allowed:
        // user->sam, sam->standard, standard->2
        // actual available calls: 2 -> getBook
        BookService.getBook()
                .contextWrite(UserService.userCategoryCtx())
                .contextWrite(Context.of("user", "sam"))
                .subscribe(new DefaultSubscriber());

        // allowed:
        // user->sam, sam->standard, standard->2
        // actual available calls: 1 -> getBook
        BookService.getBook()
                .contextWrite(UserService.userCategoryCtx())
                .contextWrite(Context.of("user", "sam"))
                .subscribe(new DefaultSubscriber());

        // not allowed:
        // user->sam, sam->standard, standard->2,
        // actual available calls: 0 -> error
        BookService.getBook()
                .contextWrite(UserService.userCategoryCtx())
                .contextWrite(Context.of("user", "sam"))
                .subscribe(new DefaultSubscriber());

        System.out.println("mike calling");

        // 3 allowed callse, 1 not allowed call:
        // user->mike, mike->prime, prime->3 calls
        BookService.getBook()
                .repeat(3) // call once + repeat 3 times -> 4 calls                .contextWrite(UserService.userCategoryCtx())
                .contextWrite(UserService.userCategoryCtx())
                .contextWrite(Context.of("user", "mike"))
                .subscribe(new DefaultSubscriber());
    }
}
