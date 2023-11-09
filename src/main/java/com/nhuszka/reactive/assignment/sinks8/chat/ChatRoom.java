package com.nhuszka.reactive.assignment.sinks8.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Sinks;

@Data
@AllArgsConstructor
public class ChatRoom {

    private final Sinks.Many<Msg> communicationChannel = Sinks.many().multicast().directBestEffort();

    public void sendMessage(Msg message) {
        System.out.println(message.getSenderName() + ": " + message.getContent());
        communicationChannel.tryEmitNext(message);
    }

    public void joinRoom(Chatter chatter) {
        communicationChannel.asFlux()
                .filter(message -> !message.getSenderName().equals(chatter.getUsername()))
                .subscribe(chatter.getMessageHandler());
        System.out.println("("+ chatter.getUsername() + " joined the room" + ")");
    }
}
