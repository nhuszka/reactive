package com.nhuszka.reactive.robots.robot;

import com.nhuszka.reactive.robots.geometry.Coordinates;
import com.nhuszka.reactive.robots.geometry.Direction;

public class Movement {

    static Coordinates move(int distance, Coordinates coordinates, Direction direction) {
        int x = coordinates.getX();
        int y = coordinates.getY();
        switch (direction) {
            case NORTH:
                y += distance;
                break;
            case SOUTH:
                y -= distance;
                break;
            case EAST:
                x += distance;
                break;
            case WEST:
                x -= distance;
                break;
        }
        return new Coordinates(x, y);
    }

}
