package com.nhuszka.reactive.robots;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Main {

    public static void main(String[] args) {
        Coordinates topLeft = new Coordinates(0, 100);
        Coordinates topRight = new Coordinates(100, 100);
        Coordinates bottomRight = new Coordinates(100, 0);
        Coordinates bottomLeft = new Coordinates(0, 0);

        Box box = new Box(topLeft, topRight, bottomRight, bottomLeft);
        Coordinates robotCoordinates = new Coordinates(0, 0);

        RobotRectangular robotRectangular = new RobotRectangular("fastRobot", robotCoordinates, Direction.EAST, box, 300, 1);
        robotRectangular.move();

        Flux.interval(Duration.ofMillis(1000))
                .map(__ -> robotRectangular.getCurrentPosition())
                .subscribe(position -> System.out.printf("queryPer1000 [x: %d, y: %d]%n", position.getX(), position.getY()));

        robotRectangular.getPath()
                .subscribe(position -> System.out.printf("queryWhenAvailable [x: %d, y: %d]%n", position.getX(), position.getY()));

        Util.waitFor(300);
    }
}
