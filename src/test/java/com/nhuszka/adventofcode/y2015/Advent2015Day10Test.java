package com.nhuszka.adventofcode.y2015;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class Advent2015Day10Test {

    @Test
    public void testLookAndSaySequence1() {
        assertLookAndDaySequence(
                List.of(1),
                List.of(1, 1)
        );
    }

    @Test
    public void testLookAndSaySequence11() {
        assertLookAndDaySequence(
                List.of(1, 1),
                List.of(2, 1)
        );
    }

    @Test
    public void testLookAndSaySequence21() {
        assertLookAndDaySequence(
                List.of(2, 1),
                List.of(1, 2, 1, 1)
        );
    }

    @Test
    public void testLookAndSaySequence1211() {
        assertLookAndDaySequence(
                List.of(1, 2, 1, 1),
                List.of(1, 1, 1, 2, 2, 1)
        );
    }

    @Test
    public void testLookAndSaySequence111221() {
        assertLookAndDaySequence(
                List.of(1, 1, 1, 2, 2, 1),
                List.of(3, 1, 2, 2, 1, 1)
        );
    }

    private static void assertLookAndDaySequence(List<Integer> input, List<Integer> expectedResult) {
        Assertions.assertEquals(
                expectedResult,
                Advent2015Day10.getLookAndSaySequence(
                        input
                )
        );
    }

}