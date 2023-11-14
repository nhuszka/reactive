package com.nhuszka.reactive.robots.model;

public class Box {
    private final Coordinates topLeft;
    private final Coordinates topRight;
    private final Coordinates bottomRight;
    private final Coordinates bottomLeft;

    public Box(Coordinates topLeft, Coordinates topRight, Coordinates bottomRight, Coordinates bottomLeft) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;
    }

    public Coordinates getTopLeft() {
        return topLeft;
    }

    public Coordinates getTopRight() {
        return topRight;
    }

    public Coordinates getBottomRight() {
        return bottomRight;
    }

    public Coordinates getBottomLeft() {
        return bottomLeft;
    }
}