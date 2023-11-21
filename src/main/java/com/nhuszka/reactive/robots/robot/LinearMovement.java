package com.nhuszka.reactive.robots.robot;

import com.nhuszka.reactive.robots.geometry.Coordinates;
import com.nhuszka.reactive.robots.geometry.Direction;
import com.nhuszka.reactive.robots.geometry.Line;

import static com.nhuszka.reactive.robots.geometry.Direction.*;

class LinearMovement {

    static boolean isOnLine(Coordinates coordinate, Line line) {
        return coordinate.getX() >= line.getA().getX()
                && coordinate.getX() <= line.getB().getX();
    }

    static Direction getDirection(int distanceToMove, Coordinates coordinates, Direction direction, Line line) {
        boolean shouldTurn = shouldTurn(distanceToMove, coordinates, direction, line);
        if (shouldTurn) {
            switch (direction) {
                case NORTH:
                    return SOUTH;
                case EAST:
                    return WEST;
                case SOUTH:
                    return NORTH;
                case WEST:
                    return EAST;
            }
        }
        return direction;
    }

    private static boolean shouldTurn(int distanceToMove, Coordinates coordinates, Direction direction, Line line) {
        int x = coordinates.getX();
        int y = coordinates.getY();

        boolean reachedEastPoint = direction == EAST
                && (x + distanceToMove > line.getB().getX());
        boolean reachedWestPoint = direction == Direction.WEST
                && (x - distanceToMove < line.getA().getX());
        boolean reachedNorthPoint = direction == NORTH
                && (y + distanceToMove > line.getA().getY());
        boolean reachedSouthPoint = direction == SOUTH
                && (y - distanceToMove < line.getB().getY());

        return reachedEastPoint || reachedWestPoint || reachedNorthPoint || reachedSouthPoint;
    }
}
