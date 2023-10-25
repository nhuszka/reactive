package com.nhuszka.reactive.assignment.sinks8.refactored;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;

public class RoomRefactored {

    private final Flux<MessageRefactored> flux;
    private final Sinks.Many<MessageRefactored> sink;

    public RoomRefactored() {
        this.sink = Sinks.many().replay().all();
        this.flux = sink.asFlux();
    }

    public void joins(MemberRefactored member) {
        System.out.println(member.getName() + " joined");
        member.setMessageConsumer(msg -> this.post(msg, member));
        subscribe(member);
    }

    private void subscribe(MemberRefactored member) {
        flux
                .filter(messageRefactored -> !messageRefactored.getFrom().equals(member.getName()))
                .doOnNext(messageRefactored -> messageRefactored.setTo(member.getName()))
                .map(MessageRefactored::toString)
                .subscribe(member::receives);
    }

    private void post(String msg, MemberRefactored member) {
        sink.tryEmitNext(new MessageRefactored(member.getName(), msg, LocalDateTime.now().toString()));
    }
}
