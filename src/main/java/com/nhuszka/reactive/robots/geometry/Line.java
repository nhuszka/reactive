package com.nhuszka.reactive.robots.geometry;

import lombok.Data;

@Data
public class Line implements Shape {
    private final Coordinates a;
    private final Coordinates b;
}
