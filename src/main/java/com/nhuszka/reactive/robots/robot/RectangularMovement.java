package com.nhuszka.reactive.robots.robot;

import com.nhuszka.reactive.robots.geometry.Box;
import com.nhuszka.reactive.robots.geometry.Coordinates;
import com.nhuszka.reactive.robots.geometry.Direction;

import static com.nhuszka.reactive.robots.geometry.Direction.*;

class RectangularMovement {

    static boolean isWithinBox(Coordinates coordinate, Box box) {
        return coordinate.getX() >= box.getBottomLeft().getX()
                && coordinate.getX() <= box.getBottomRight().getX()
                &&  coordinate.getY() >= box.getBottomLeft().getY()
                &&  coordinate.getY() <= box.getTopLeft().getY();
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
