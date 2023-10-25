package com.nhuszka.reactive.assignment.sinks8;

public class Chat {


    public static void main(String[] args) {
        // slackroom: if anybody post message, other should get the message

        // one class for slackroom
        // one class for member
        // message: string OR slack message class  (sender, timestamp, message)

        Member tom = new Member("Tom");
        Member mike = new Member("Mike");
        Member dave = new Member("Dave");

        Room.join(tom);
        Room.join(mike);
        tom.say("hi");
        mike.say("hi Tom");
        tom.say("how are you doing?");

        Room.join(dave);
        mike.say("doing fine, how about you guys?");
        mike.say("hi Dave");
        dave.say("hi both");
    }
}
