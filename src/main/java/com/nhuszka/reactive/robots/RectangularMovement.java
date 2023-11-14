package com.nhuszka.reactive.robots;

import static com.nhuszka.reactive.robots.Direction.*;

public class RectangularMovement {

    static boolean isWithinBox(Coordinates coordinate, Box box) {
        return coordinate.getX() >= box.getBottomLeft().getX()
                && coordinate.getX() <= box.getBottomRight().getX()
                &&  coordinate.getY() >= box.getBottomLeft().getY()
                &&  coordinate.getY() <= box.getTopLeft().getY();
    }

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

    static Direction getDirection(int distanceToMove, Coordinates coordinates, Direction direction, Box box) {
        boolean shouldTurn = shouldTurn(distanceToMove, coordinates, direction, box);
        if (shouldTurn) {
            switch (direction) {
                case NORTH:
                    return EAST;
                case EAST:
                    return SOUTH;
                case SOUTH:
                    return Direction.WEST;
                case WEST:
                    return NORTH;
            }
        }
        return direction;
    }

    private static boolean shouldTurn(int distanceToMove, Coordinates coordinates, Direction direction, Box box) {
        int x = coordinates.getX();
        int y = coordinates.getY();

        boolean reachedEastBorder = direction == EAST
                && (x + distanceToMove > box.getTopRight().getX());
        boolean reachedWestBorder = direction == Direction.WEST
                && (x - distanceToMove < box.getBottomLeft().getX());
        boolean reachedNorthBorder = direction == NORTH
                && (y + distanceToMove > box.getTopLeft().getY());
        boolean reachedSouthBorder = direction == SOUTH
                && (y - distanceToMove < box.getBottomRight().getY());

        return reachedEastBorder || reachedWestBorder || reachedNorthBorder || reachedSouthBorder;
    }
}
