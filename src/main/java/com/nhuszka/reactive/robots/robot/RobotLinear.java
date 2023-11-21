package com.nhuszka.reactive.robots.robot;

import com.nhuszka.reactive.robots.exception.MovementException;
import com.nhuszka.reactive.robots.geometry.Coordinates;
import com.nhuszka.reactive.robots.geometry.Direction;
import com.nhuszka.reactive.robots.geometry.Line;

import java.util.function.Consumer;

public class RobotLinear extends Robot {

    private final Line line;

    public RobotLinear(String name, Coordinates startingCoordinates, Direction initialDirection, Line line, int movePerMillis, int moveDistance) {
        super(name, startingCoordinates, initialDirection, moveDistance, movePerMillis);
        this.line = line;
    }

    protected Consumer<Long> move() {
        return timeToMove -> {
            Direction currentDirection = direction.get();
            Coordinates currentCoordinates = coordinates.get();

            Direction directionToMoveTo = LinearMovement.getDirection(moveDistance, currentCoordinates, currentDirection, line);
            Coordinates coordinatesToMoveTo = Movement.move(moveDistance, currentCoordinates, directionToMoveTo);

            if (!LinearMovement.isOnLine(coordinatesToMoveTo, line)) {
                throw new MovementException(currentDirection, currentCoordinates, directionToMoveTo, coordinatesToMoveTo, line);
            }

            direction.set(directionToMoveTo);
            coordinates.set(coordinatesToMoveTo);
        };
    }
}
