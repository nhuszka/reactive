package com.nhuszka.reactive.robots;

import com.nhuszka.reactive.robots.model.Box;
import com.nhuszka.reactive.robots.model.Coordinates;
import com.nhuszka.reactive.robots.model.Direction;
import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.logging.Logger;

public class Main {

    private static Logger log = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        Coordinates topLeft = new Coordinates(0, 10);
        Coordinates topRight = new Coordinates(10, 10);
        Coordinates bottomRight = new Coordinates(10, 0);
        Coordinates bottomLeft = new Coordinates(0, 0);

        Box box = new Box(topLeft, topRight, bottomRight, bottomLeft);
        Coordinates robotCoordinates = new Coordinates(0, 0);

        // TODO handle case, when robot is started with a wrong position-direction combination. Like x=0, y=0, direction=EAST
        //      it would work for counter-clockwise, but the robot wants to move clockwise -> would generate exception
        RobotRectangular robotRectangular = new RobotRectangular("fastRobot", robotCoordinates, Direction.EAST  , box, 100, 1);
        robotRectangular.startMoving();

        Flux.interval(Duration.ofMillis(1000))
                .map(timeToPollPosition -> robotRectangular.getCurrentPosition())
                .subscribe(position -> System.out.printf("poll position [x: %d, y: %d]%n", position.getX(), position.getY()));

        robotRectangular.coordinatesFeed()
                .subscribe(
                        position -> log.info(String.format("observe coordinates [x: %d, y: %d]%n", position.getX(), position.getY()))
                );

        Util.waitFor(5);

        log.info("Stopping robot.");
        robotRectangular.stopMoving();

        log.info("Waiting for program to finish.");
        Util.waitFor(5);
    }
}
