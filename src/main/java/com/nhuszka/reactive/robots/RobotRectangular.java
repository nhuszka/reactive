package com.nhuszka.reactive.robots;

import com.nhuszka.reactive.robots.exception.MovementException;
import com.nhuszka.reactive.robots.model.Box;
import com.nhuszka.reactive.robots.model.Coordinates;
import com.nhuszka.reactive.robots.model.Direction;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

// TODO fix lombok issue "java: package org.slf4j does not exist"
//@Slf4j
public class RobotRectangular {

    private final Logger log = Logger.getLogger(RobotRectangular.class.getSimpleName());

    private final String name;

    private final AtomicReference<Coordinates> coordinates;
    private final AtomicReference<Direction> direction;
    private final Box box;

    private final int moveDistance;
    private Disposable movement;
    private final Flux<Long> movementTimer;
    private final Flux<Coordinates> coordinatesFlux;

    public RobotRectangular(String name, Coordinates startingCoordinates, Direction initialDirection, Box box, int movePerMillis, int moveDistance) {
        this.name = name;
        this.coordinates = new AtomicReference<>(startingCoordinates);
        this.direction = new AtomicReference<>(initialDirection);
        this.box = box;
        this.moveDistance = moveDistance;
        this.movementTimer = Flux.interval(Duration.ofMillis(movePerMillis));
        this.coordinatesFlux = movementTimer
                .doOnNext(timeToMove -> {
                    Direction currentDirection = direction.get();
                    Coordinates currentCoordinates = coordinates.get();

                    Direction directionToMoveTo = RectangularMovement.getDirection(moveDistance, currentCoordinates, currentDirection, box);
                    Coordinates coordinatesToMoveTo = RectangularMovement.move(moveDistance, currentCoordinates, directionToMoveTo);

                    if (!RectangularMovement.isWithinBox(coordinatesToMoveTo, box)) {
                        throw new MovementException(currentDirection, currentCoordinates, directionToMoveTo, coordinatesToMoveTo, box);
                    }

                    direction.set(directionToMoveTo);
                    coordinates.set(coordinatesToMoveTo);
                })
                .map(timeToMove -> coordinates.get());
    }

    public void startMoving() {
        movement = coordinatesFlux
                .doOnError(throwable -> log.info("Error when moving robot: " + throwable.getMessage()))
                .doOnCancel(() -> log.info("Movement of robot " + name + " cancelled"))
                .subscribe();
    }

    public Flux<Coordinates> coordinatesFeed() {
        return movementTimer
                .takeUntil(coordinate -> isMovementStopped())
                .map(timeToMove -> this.getCurrentPosition());
    }

    private boolean isMovementStopped() {
        return movement == null || movement.isDisposed();
    }

    public void stopMoving() {
        movement.dispose();
    }

    public Coordinates getCurrentPosition() {
        return coordinates.get();
    }
}
