package com.nhuszka.reactive.assignment.sinks8.refactored;

public class MessageRefactored {

    private final String from;
    private String to;
    private final String content;
    private final String timestamp;

    public MessageRefactored(String from, String content, String timestamp) {
        this.from = from;
        this.content = content;
        this.timestamp = timestamp;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return String.format("[%s] from: %s to: %s : %s", timestamp, from, to, content);
    }
}
