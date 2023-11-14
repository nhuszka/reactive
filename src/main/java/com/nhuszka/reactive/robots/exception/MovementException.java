package com.nhuszka.reactive.robots.exception;

import com.nhuszka.reactive.robots.model.Box;
import com.nhuszka.reactive.robots.model.Coordinates;
import com.nhuszka.reactive.robots.model.Direction;
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
