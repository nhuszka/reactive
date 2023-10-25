package com.nhuszka.reactive.util;

import com.github.javafaker.Faker;

import java.util.function.Consumer;

public class Util {

    private static final Faker INSTANCE = Faker.instance();

    public static Consumer<Integer> onNext() {
        return i -> System.out.println("Received: " + i);
    }

    public static Consumer<Integer> onError() {
        return error -> System.out.println("Error: " + error);
    }

    public static Runnable onComplete() {
        return () -> System.out.println("Done");
    }

    public static Faker faker() {
        return INSTANCE;
    }

    public static void waitForHalfOf(int seconds) {
        try {
            Thread.sleep(seconds * 500L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printThreadName(String msg) {
        System.out.println(msg + "\t\t : Thread : " + Thread.currentThread().getName());
    }

    public static void waitForMillis(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
