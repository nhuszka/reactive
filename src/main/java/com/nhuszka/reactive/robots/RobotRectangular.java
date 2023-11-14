package com.nhuszka.reactive.robots;

import com.nhuszka.reactive.robots.exception.MovementException;
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

    private final int moveDistance;
    private Disposable movement;
    private final Flux<Long> movementTimer;

    public RobotRectangular(String name, Coordinates startingCoordinates, Direction initialDirection, Box box, int movePerMillis, int moveDistance) {
        this.name = name;
        this.coordinates = new AtomicReference<>(startingCoordinates);
        this.direction = new AtomicReference<>(initialDirection);
        this.box = box;
        this.moveDistance = moveDistance;
        this.movementTimer = Flux.interval(Duration.ofMillis(movePerMillis));
    }

    public void startMoving() {
        movement = movementTimer
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
                .doOnCancel(() -> log.info("Movement of robot " + name + " cancelled"))
                .subscribe();
    }

    public Flux<Coordinates> coordinatesFeed() {
        return movementTimer
                .takeUntil(coordinate -> isMovementStopped())
                .map(timeToMove -> this.getCurrentPosition());
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
