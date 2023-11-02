package com.nhuszka.reactive.assignment.flux_advanced3;

import reactor.core.publisher.Flux;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReaderServiceNewAttempt {
    public static void main(String[] args) {
        Flux.create(fluxSink -> {
            try (Scanner scanner = new Scanner(new File("/home/nandor/repo/textFileForRead.txt"))) {

                while (scanner.hasNextLine()) {
                    fluxSink.next(scanner.nextLine());
                }

                fluxSink.complete();
            } catch (FileNotFoundException e) {
                fluxSink.error(e);
            }
        })
        .subscribe(System.out::println);

        Flux.generate(
                () ->  new Scanner(new File("/home/nandor/repo/textFileForRead.txt")),
                (scanner, synchronousSink) -> {
                    if (scanner.hasNextLine()) {
                        synchronousSink.next(scanner.nextLine());
                    } else {
                        synchronousSink.complete();
                    }
                    return scanner;
                },
                Scanner::close
        ).subscribe(System.out::println);

    }
}
