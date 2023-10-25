package com.nhuszka.reactive.assignment.sinks8.refactored;

import com.nhuszka.reactive.util.Util;

public class ChatRefactored {

    public static void main(String[] args) {

        RoomRefactored room = new RoomRefactored();

        MemberRefactored sam = new MemberRefactored("sam");
        MemberRefactored jake = new MemberRefactored("jake");
        MemberRefactored mike = new MemberRefactored("mike");

        room.joins(sam);
        room.joins(jake);
        sam.says("Hi");
        Util.waitFor(4);

        jake.says("Hey");
        sam.says("What's up?");
        Util.waitFor(4);

        room.joins(mike);
        mike.says("hi both");
    }
}
