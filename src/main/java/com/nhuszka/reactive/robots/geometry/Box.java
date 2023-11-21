package com.nhuszka.reactive.robots.geometry;

import lombok.Data;

@Data
public class Box implements Shape {
    private final Coordinates topLeft;
    private final Coordinates topRight;
    private final Coordinates bottomRight;
    private final Coordinates bottomLeft;
}
