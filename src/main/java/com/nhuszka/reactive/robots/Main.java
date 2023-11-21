package com.nhuszka.reactive.robots;

import com.nhuszka.reactive.robots.robot.RobotLinear;
import com.nhuszka.reactive.robots.robot.RobotRectangular;
import com.nhuszka.reactive.robots.geometry.Box;
import com.nhuszka.reactive.robots.geometry.Coordinates;
import com.nhuszka.reactive.robots.geometry.Direction;
import com.nhuszka.reactive.robots.geometry.Line;
import com.nhuszka.reactive.util.Util;

import java.util.logging.Logger;

public class Main {

    private static Logger log = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        Box box1 = new Box(
                new Coordinates(2, 12),
                new Coordinates(12, 12),
                new Coordinates(12, 2),
                new Coordinates(2, 2)
        );
        Coordinates startCoordinates1 = new Coordinates(2, 2);
        // TODO handle case, when robot is started with a wrong position-direction combination. Like x=2, y=2, direction=EAST
        //      it would work for counter-clockwise, but the robot wants to move clockwise -> would generate exception
        RobotRectangular fastRobot1 = new RobotRectangular("fastRobot1", startCoordinates1, Direction.NORTH, box1, 1000, 1);

        Box box2 = new Box(
                new Coordinates(-12, 2),
                new Coordinates(-2, -2),
                new Coordinates(-2, -12),
                new Coordinates(-12, -12)
        );
        Coordinates startCoordinates2 = new Coordinates(-12, -2);
        // TODO handle case, when robot is started with a wrong position-direction combination. Like x=-12, y=-12, direction=EAST
        //      it would work for counter-clockwise, but the robot wants to move clockwise -> would generate exception
        RobotRectangular fastRobot2 = new RobotRectangular("fastRobot2", startCoordinates2, Direction.EAST, box2, 1000, 1);

        Line line = new Line(
                new Coordinates(-7, 0),
                new Coordinates(7, 0)
        );
        Coordinates startCoordinatesLinear = new Coordinates(-7, -7);
        // TODO handle case, when robot is started with a wrong position-direction combination. Like x=0, y=0, direction=EAST
        //      it would work for counter-clockwise, but the robot wants to move clockwise -> would generate exception
        RobotLinear linearRobot = new RobotLinear("linearRobot", startCoordinatesLinear, Direction.EAST, line, 500, 1);

//        Flux.interval(Duration.ofMillis(1000))
//                .map(timeToPollPosition -> fastRobot1.getCurrentPosition())
//                .subscribe(position -> System.out.printf("poll position [x: %d, y: %d]%n", position.getX(), position.getY()));

        fastRobot1.startMoving();
        fastRobot1.coordinatesFeed()
                .subscribe(
                        position -> log.info(String.format("observe coordinates rectangularRobot1 [x: %d, y: %d]%n", position.getX(), position.getY()))
                );

        Util.waitForMillis(500);

        linearRobot.startMoving();
        linearRobot.coordinatesFeed()
                .subscribe(
                        position -> log.info(String.format("observe coordinates linearRobot [x: %d, y: %d]%n", position.getX(), position.getY()))
                );

        Util.waitForMillis(500);

        fastRobot2.startMoving();
        fastRobot2.coordinatesFeed()
                .subscribe(
                        position -> log.info(String.format("observe coordinates rectangularRobot2 [x: %d, y: %d]%n", position.getX(), position.getY()))
                );

        Util.waitFor(10);
        log.info("Stopping robot.");
        fastRobot1.stopMoving();
        fastRobot2.stopMoving();
        linearRobot.stopMoving();

        log.info("Waiting for program to finish.");
        Util.waitFor(5);
    }
}
