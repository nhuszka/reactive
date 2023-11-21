package com.nhuszka.reactive.robots.robot;

import com.nhuszka.reactive.robots.exception.MovementException;
import com.nhuszka.reactive.robots.geometry.Box;
import com.nhuszka.reactive.robots.geometry.Coordinates;
import com.nhuszka.reactive.robots.geometry.Direction;

import java.util.function.Consumer;

public class RobotRectangular extends Robot {

    private final Box box;

    public RobotRectangular(String name, Coordinates startingCoordinates, Direction initialDirection, Box box, int movePerMillis, int moveDistance) {
        super(name, startingCoordinates, initialDirection, moveDistance, movePerMillis);
        this.box = box;
    }

    protected Consumer<Long> move() {
        return timeToMove -> {
            Direction currentDirection = direction.get();
            Coordinates currentCoordinates = coordinates.get();

            Direction directionToMoveTo = RectangularMovement.getDirection(moveDistance, currentCoordinates, currentDirection, box);
            Coordinates coordinatesToMoveTo = Movement.move(moveDistance, currentCoordinates, directionToMoveTo);

            if (!RectangularMovement.isWithinBox(coordinatesToMoveTo, box)) {
                throw new MovementException(currentDirection, currentCoordinates, directionToMoveTo, coordinatesToMoveTo, box);
            }

            direction.set(directionToMoveTo);
            coordinates.set(coordinatesToMoveTo);
        };
    }
}
