package com.nhuszka.reactive.assignment.sinks8;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class Room {

    private static final Sinks.Many<Message> ROOM = Sinks.many().multicast().onBackpressureBuffer();
    private static final Flux<Message> ROOM_FLUX = ROOM.asFlux()
//            .doOnSubscribe(subscription -> System.out.println("Someone joined"))
            ;

    public static void join(Member member) {
        member.setRoom(ROOM);
        ROOM_FLUX.subscribe(member);
    }
}
