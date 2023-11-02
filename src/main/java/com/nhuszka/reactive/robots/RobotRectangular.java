package com.nhuszka.reactive.robots;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

@Slf4j
public class RobotRectangular {

    private final Logger log = Logger.getLogger(RobotRectangular.class.getSimpleName());

    private final String name;

    private final AtomicReference<Coordinates> coordinates;
    private final AtomicReference<Direction> direction;
    private final Box box;

    private final int movePerMillis;
    private final int moveDistance;
    private Disposable movement;

    public RobotRectangular(String name, Coordinates startingCoordinates, Direction initialDirection, Box box, int movePerMillis, int moveDistance) {
        this.name = name;
        this.coordinates = new AtomicReference<>(startingCoordinates);
        this.direction = new AtomicReference<>(initialDirection);
        this.box = box;
        this.movePerMillis = movePerMillis;
        this.moveDistance = moveDistance;
    }

    public void startMoving() {
        movement = Flux.interval(Duration.ofMillis(movePerMillis))
                .doOnNext(timeToMove -> {
                    Direction currentDirection = direction.get();
                    Coordinates currentCoordinates = coordinates.get();

                    direction.set(RectangularMovement.getDirection(moveDistance, currentCoordinates, currentDirection, box));
                    coordinates.set(RectangularMovement.move(moveDistance, currentCoordinates, currentDirection));
                })
                .subscribe();
    }

    public Flux<Coordinates> coordinates() {
        return Flux.interval(Duration.ofMillis(movePerMillis))
                .takeUntil(coordinate -> isMovementStopped())
                .map(movementEvent -> this.getCurrentPosition());
    }

    private boolean isMovementStopped() {
        return movement.isDisposed();
    }

    public void stopMoving() {
        movement.dispose();
    }

    public Coordinates getCurrentPosition() {
        return coordinates.get();
    }
}
