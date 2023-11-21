package com.nhuszka.reactive.robots.exception;

import com.nhuszka.reactive.robots.geometry.Coordinates;
import com.nhuszka.reactive.robots.geometry.Direction;
import com.nhuszka.reactive.robots.geometry.Shape;
import lombok.Data;

@Data
public class MovementException extends RuntimeException {

    private final Direction currentDirection;
    private final Coordinates currentCoordinates;
    private final Direction directionToMoveTo;
    private final Coordinates coordinatesToMoveTo;
    private final Shape movementShape;

    public MovementException(Direction currentDirection, Coordinates currentCoordinates,
                             Direction directionToMoveTo, Coordinates coordinatesToMoveTo,
                             Shape movementShape) {
        this.currentDirection = currentDirection;
        this.currentCoordinates = currentCoordinates;
        this.directionToMoveTo = directionToMoveTo;
        this.coordinatesToMoveTo = coordinatesToMoveTo;
        this.movementShape = movementShape;
    }
}
