package com.nhuszka.adventofcode.y2015;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Advent2015Day02Test {

    // --- PART1 ---
    @Test
    public void test1() {
        assertGetAreaWithSlack(
                "2x3x4",
                58
        );
    }

    @Test
    public void test2() {
        assertGetAreaWithSlack(
                "1x1x10",
                43
        );
    }

    @Test
    public void test3() {
        assertGetAreaWithSlack(
                "2x3x4\n" +
                        "1x1x10",
                58 + 43
        );
    }

    private static void assertGetAreaWithSlack(String input, long expectedAreaWithSlack) {
        Assertions.assertEquals(
                expectedAreaWithSlack,
                Advent2015Day02.getAreaPart1(input)
        );
    }

    // --- PART2 ---
    @Test
    public void test4() {
        assertGetRibbonLength(
                "2x3x4",
                34
        );
    }

    @Test
    public void test5() {
        assertGetRibbonLength(
                "1x1x10",
                14
        );
    }

    @Test
    public void test6() {
        assertGetRibbonLength(
                "2x3x4\n" +
                        "1x1x10",
                34 + 14
        );
    }

    private static void assertGetRibbonLength(String input, long expectedRibbonLength) {
        Assertions.assertEquals(
                expectedRibbonLength,
                Advent2015Day02.getLengthOfRibbonPart2(input)
        );
    }
}