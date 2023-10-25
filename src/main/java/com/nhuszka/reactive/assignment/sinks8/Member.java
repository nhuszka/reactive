package com.nhuszka.reactive.assignment.sinks8;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Member implements Subscriber<Message> {

    private final String name;
    private Sinks.Many<Message> room;

    public Member(String name) {
        this.name = name;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        Message chatMessage = createSystemChatMessage(name + " has joined");
        System.out.println(chatMessage);
        room.tryEmitNext(chatMessage);
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Message message) {
        System.out.println("( " + name + " has read message )");
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("onError" + throwable.getMessage());
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("onComplete");
    }

    public void say(String message) {
        Message chatMessage = createChatMessage(message);
        System.out.println(chatMessage);
        room.tryEmitNext(chatMessage);
    }

    private Message createChatMessage(String message) {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm:ss");
        String timestamp = time.format(formatter);
        return new Message(message, name, timestamp);
    }

    private Message createSystemChatMessage(String message) {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm:ss");
        String timestamp = time.format(formatter);
        return new Message(message, "SYSTEM", timestamp);
    }

    public void setRoom(Sinks.Many<Message> room) {
        this.room = room;
    }
}
