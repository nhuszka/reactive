package com.nhuszka.reactive.robots;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

public class RobotRectangular {

    private final String name;
    private final AtomicReference<Coordinates> coordinates;

    private final Box box;

    private final AtomicReference<Direction> direction;
    private final int movePerMillis;
    private final int moveDistance;

    public RobotRectangular(String name, Coordinates startingCoordinates, Direction initialDirection, Box box, int movePerMillis, int moveDistance) {
        this.name = name;
        this.coordinates = new AtomicReference<>(startingCoordinates);
        this.box = box;
        this.direction = new AtomicReference<>(initialDirection);
        this.movePerMillis = movePerMillis;
        this.moveDistance = moveDistance;
    }

    public void move() {
        Flux.interval(Duration.ofMillis(movePerMillis))
                .doOnNext(timeToMove -> {
                    this.direction.set(calculateDirection(moveDistance));
                    this.coordinates.set(getNewCoordinates(moveDistance));
                })
                .subscribe();

    }

    public Flux<Coordinates> getPath() {
        return Flux.interval(Duration.ofMillis(movePerMillis))
                .map(movementEvent -> this.getCurrentPosition());

    }

    public Coordinates getCurrentPosition() {
        return coordinates.get();
    }

    private Direction calculateDirection(int distanceToMove) {
        boolean shouldTurn = shouldTurn(distanceToMove);
        if (shouldTurn) {
            switch (direction.get()) {
                case NORTH:
                    return Direction.EAST;
                case EAST:
                    return Direction.SOUTH;
                case SOUTH:
                    return Direction.WEST;
                case WEST:
                    return Direction.NORTH;
            }
        }
        return direction.get();
    }

    private boolean shouldTurn(int distanceToMove) {
        int x = coordinates.get().getX();
        int y = coordinates.get().getY();

        boolean reachedEastBorder = direction.get() == Direction.EAST
                && (x + distanceToMove > box.getTopRight().getX());
        boolean reachedWestBorder = direction.get() == Direction.WEST
                && (x - distanceToMove < box.getBottomLeft().getX());
        boolean reachedNorthBorder = direction.get() == Direction.NORTH
                && (y + distanceToMove > box.getTopLeft().getY());
        boolean reachedSouthBorder = direction.get() == Direction.SOUTH
                && (y - distanceToMove < box.getBottomRight().getY());

        return reachedEastBorder || reachedWestBorder || reachedNorthBorder || reachedSouthBorder;
    }

    private Coordinates getNewCoordinates(int distance) {
        int x = coordinates.get().getX();
        int y = coordinates.get().getY();
        switch (direction.get()) {
            case NORTH:
                y += distance;
                break;
            case SOUTH:
                y -= distance;
                break;
            case EAST:
                x += distance;
                break;
            case WEST:
                x -= distance;
                break;
        }
        return new Coordinates(x, y);
    }
}
