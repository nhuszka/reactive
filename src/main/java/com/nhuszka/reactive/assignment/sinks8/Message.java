package com.nhuszka.reactive.assignment.sinks8;

public class Message {

    private final String message;
    private final String senderName;
    private final String timestamp;

    public Message(String message, String senderName, String timestamp) {
        this.message = message;
        this.senderName = senderName;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "[ " + timestamp + "] " + senderName + ": " + message;
    }
}
