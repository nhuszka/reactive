package com.nhuszka.adventofcode.y2015;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Advent2015Day01Test {

    // --- PART 1 ----
    @Test
    public void test1() {
        assertGetFloor(
                "(())",
                0
        );
    }

    @Test
    public void test2() {
        assertGetFloor(
                "()()",
                0
        );
    }

    @Test
    public void test3() {
        assertGetFloor(
                "(((",
                3
        );
    }

    @Test
    public void test4() {
        assertGetFloor(
                "(()(()(",
                3
        );
    }

    @Test
    public void test5() {
        assertGetFloor(
                "))(((((",
                3
        );
    }

    @Test
    public void test6() {
        assertGetFloor(
                "())",
                -1
        );
    }

    @Test
    public void test7() {
        assertGetFloor(
                "))(",
                -1
        );
    }

    @Test
    public void test8() {
        assertGetFloor(
                ")))",
                -3
        );
    }

    @Test
    public void test9() {
        assertGetFloor(
                ")())())",
                -3
        );
    }

    private static void assertGetFloor(String input, long expectedFloor) {
        Assertions.assertEquals(
                expectedFloor,
                Advent2015Day01.getFloorPart1(
                        input
                )
        );
    }

    // --- PART 2 ---

    @Test
    public void test10() {
        assertGetPositionOfCharFirstGoingToMinus1Part2(
                ")",
                1
        );
    }

    @Test
    public void test11() {
        assertGetPositionOfCharFirstGoingToMinus1Part2(
                "()())",
                5
        );
    }

    private static void assertGetPositionOfCharFirstGoingToMinus1Part2(String input, long expectedPositionOfChar) {
        Assertions.assertEquals(
                expectedPositionOfChar,
                Advent2015Day01.getPositionOfCharFirstGoingToMinus1Part2(
                        input
                )
        );
    }
}