package com.nhuszka.reactive.robots.exception;

import com.nhuszka.reactive.robots.Box;
import com.nhuszka.reactive.robots.Coordinates;
import com.nhuszka.reactive.robots.Direction;
import lombok.Data;

@Data
public class MovementException extends RuntimeException {

    private final Direction currentDirection;
    private final Coordinates currentCoordinates;
    private final Direction directionToMoveTo;
    private final Coordinates coordinatesToMoveTo;
    private final Box box;

    public MovementException(Direction currentDirection, Coordinates currentCoordinates,
                             Direction directionToMoveTo, Coordinates coordinatesToMoveTo,
                             Box box) {
        this.currentDirection = currentDirection;
        this.currentCoordinates = currentCoordinates;
        this.directionToMoveTo = directionToMoveTo;
        this.coordinatesToMoveTo = coordinatesToMoveTo;
        this.box = box;
    }
}
