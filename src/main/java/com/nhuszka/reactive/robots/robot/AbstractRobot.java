package com.nhuszka.reactive.robots.robot;

import com.nhuszka.reactive.robots.geometry.Coordinates;
import com.nhuszka.reactive.robots.geometry.Direction;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.logging.Logger;

public abstract class AbstractRobot {

    private final Logger log = Logger.getLogger(AbstractRobot.class.getSimpleName());

    private final String name;
    protected final AtomicReference<Coordinates> coordinates;
    protected final AtomicReference<Direction> direction;
    protected final int moveDistance;
    private final Flux<Long> movementTimer;
    private final Flux<Coordinates> coordinatesFlux;
    private Disposable movement;

    public AbstractRobot(String name, Coordinates startingCoordinates, Direction initialDirection, int moveDistance, int movePerMillis) {
        this.name = name;
        this.coordinates = new AtomicReference<>(startingCoordinates);
        this.direction = new AtomicReference<>(initialDirection);
        this.moveDistance = moveDistance;
        this.movementTimer = Flux.interval(Duration.ofMillis(movePerMillis));
        this.coordinatesFlux = movementTimer
                .doOnNext(move())
                .map(timeToMove -> coordinates.get());
    }

    protected abstract Consumer<Long> move();

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
