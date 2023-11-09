package com.nhuszka.reactive.assignment.sinks8.chat;

import com.nhuszka.reactive.util.Util;

public class Main {
    public static void main(String[] args) {
        ChatRoom chatRoom = new ChatRoom();

        Chatter mike = new Chatter("mike");
        Chatter sam = new Chatter("sam");
        Chatter tom = new Chatter("tom");

        mike.join(chatRoom);
        Util.waitFor(2);

        sam.join(chatRoom);
        Util.waitFor(2);

        mike.sendMessage("hello");
        Util.waitFor(2);

        tom.join(chatRoom);
        Util.waitFor(1);

        sam.sendMessage("hi Mike");
        Util.waitFor(4);
        tom.sendMessage("hi guys");
        Util.waitFor(2);

        mike.sendMessage("hi Tom");
        Util.waitFor(2);
        sam.sendMessage("hey Tom");

        Util.waitFor(10);
    }
}
