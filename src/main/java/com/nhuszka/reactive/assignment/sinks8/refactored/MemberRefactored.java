package com.nhuszka.reactive.assignment.sinks8.refactored;

import java.util.function.Consumer;

public class MemberRefactored {

    private final String name;
    private Consumer<String> messageConsumer;


    public MemberRefactored(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMessageConsumer(Consumer<String> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    public void receives(String msg) {
        System.out.println(msg);
    }

    public void says(String msg) {
        messageConsumer.accept(msg);
    }
}
