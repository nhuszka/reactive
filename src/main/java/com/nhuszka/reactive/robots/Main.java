package com.nhuszka.reactive.robots;

import com.nhuszka.reactive.util.Util;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.logging.Logger;

@Slf4j
public class Main {

    private static Logger log = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        Coordinates topLeft = new Coordinates(0, 10);
        Coordinates topRight = new Coordinates(10, 10);
        Coordinates bottomRight = new Coordinates(10, 0);
        Coordinates bottomLeft = new Coordinates(0, 0);

        Box box = new Box(topLeft, topRight, bottomRight, bottomLeft);
        Coordinates robotCoordinates = new Coordinates(0, 0);

        RobotRectangular robotRectangular = new RobotRectangular("fastRobot", robotCoordinates, Direction.EAST, box, 1000, 1);
        robotRectangular.startMoving();

        Flux.interval(Duration.ofMillis(1000))
                .map(timeToPollPosition -> robotRectangular.getCurrentPosition())
                .subscribe(position -> System.out.printf("queryPer1000 [x: %d, y: %d]%n", position.getX(), position.getY()));
//
        robotRectangular.coordinates()
                .subscribe(
                        position -> log.info(String.format("getting robot coordinates [x: %d, y: %d]%n", position.getX(), position.getY()))
                );

//        robotRectangular.reportPosition()
//                .subscribe(currentPosition -> System.out.printf("reported Position [x: %d, y: %d]%n", currentPosition.getX(), currentPosition.getY()));

        Util.waitFor(5);

        log.info("Stopping robot.");
        robotRectangular.stopMoving();

        log.info("Waiting for program to finish.");
        Util.waitFor(5);
    }
}
