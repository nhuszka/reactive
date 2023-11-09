package com.nhuszka.reactive.assignment.sinks8.chat;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Data
@RequiredArgsConstructor
public class Chatter implements Subscriber<Msg> {

    private final String username;
    private ChatRoom chatRoom;
    private Subscription subscription;

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Msg msg) {
        System.out.println("(" + username + " got the message: " + msg.getContent() + ")");
    }

    @Override
    public void onError(Throwable throwable) {
    }

    @Override
    public void onComplete() {
    }

    public void sendMessage(String message) {
        chatRoom.sendMessage(new Msg(username, message));
    }

    public void join(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        chatRoom.joinRoom(this);
    }
}
