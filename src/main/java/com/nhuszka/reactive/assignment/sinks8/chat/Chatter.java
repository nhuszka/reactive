package com.nhuszka.reactive.assignment.sinks8.chat;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscription;

import java.util.function.Consumer;

@Data
@RequiredArgsConstructor
public class Chatter {

    private final String username;
    private ChatRoom chatRoom;
    private Subscription subscription;

    public void sendMessage(String message) {
        chatRoom.sendMessage(new Msg(username, message));
    }

    public void join(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        chatRoom.joinRoom(this);
    }

    public Consumer<? super Msg> getMessageHandler() {
        Consumer<? super Msg> messageConsumer = message ->
                System.out.println("(" + username + " got the message: " + message.getContent() + ")");
        return messageConsumer;
    }
}
