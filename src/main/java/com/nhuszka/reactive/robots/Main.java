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
        Box box1 = new Box(
                new Coordinates(0, 10),
                new Coordinates(10, 10),
                new Coordinates(10, 0),
                new Coordinates(0, 0)
        );
        Coordinates startCoordinates1 = new Coordinates(0, 0);
        // TODO handle case, when robot is started with a wrong position-direction combination. Like x=0, y=0, direction=EAST
        //      it would work for counter-clockwise, but the robot wants to move clockwise -> would generate exception
        RobotRectangular fastRobot1 = new RobotRectangular("fastRobot", startCoordinates1, Direction.NORTH, box1, 1000, 1);

        Box box2 = new Box(
                new Coordinates(-10, 0),
                new Coordinates(0, 0),
                new Coordinates(0, -10),
                new Coordinates(-10, -10)
        );
        Coordinates startCoordinates2 = new Coordinates(-10, -10);
        // TODO handle case, when robot is started with a wrong position-direction combination. Like x=0, y=0, direction=EAST
        //      it would work for counter-clockwise, but the robot wants to move clockwise -> would generate exception
        RobotRectangular fastRobot2 = new RobotRectangular("fastRobot", startCoordinates2, Direction.NORTH, box2, 1000, 1);

//        Flux.interval(Duration.ofMillis(1000))
//                .map(timeToPollPosition -> fastRobot1.getCurrentPosition())
//                .subscribe(position -> System.out.printf("poll position [x: %d, y: %d]%n", position.getX(), position.getY()));

        fastRobot1.startMoving();
        fastRobot1.coordinatesFeed()
                .subscribe(
                        position -> log.info(String.format("observe coordinates robot1 [x: %d, y: %d]%n", position.getX(), position.getY()))
                );

        Util.waitForMillis(500);

        fastRobot2.startMoving();
        fastRobot2.coordinatesFeed()
                .subscribe(
                        position -> log.info(String.format("observe coordinates robot2 [x: %d, y: %d]%n", position.getX(), position.getY()))
                );

        Util.waitFor(10);
        log.info("Stopping robot.");
        fastRobot1.stopMoving();
        fastRobot2.stopMoving();

        log.info("Waiting for program to finish.");
        Util.waitFor(5);
    }
}
