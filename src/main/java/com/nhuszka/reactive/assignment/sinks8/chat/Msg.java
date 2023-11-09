package com.nhuszka.reactive.assignment.sinks8.chat;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Msg {

    private final String senderName;
    private final String content;
}
