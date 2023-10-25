package com.nhuszka.reactive.assignment.flux_advanced3;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class FileReaderService {

    private static final String FILE_PATH = "/home/nandor/repo/textFileForRead.txt";

    public static void main(String[] args) {
        FileReaderService fileReaderService = new FileReaderService();

        fileReaderService
                .readWithFluxCreate(FILE_PATH)
//                .log()
                .subscribe(
                        System.out::println,
                        throwable -> System.out.println("ERROR in Flux.create: " + throwable.getMessage()),
                        () -> System.out.println("Done reading file with Flux.create: " + FILE_PATH)
                );

        System.out.println("-----------------------------");

        fileReaderService
                .readWithFluxGenerate(FILE_PATH)
//                .log()
                .take(2)
                .subscribe(
                        System.out::println,
                        throwable -> System.out.println("ERROR in Flux.generate : " + throwable.getMessage()),
                        () -> System.out.println("Done reading file with Flux.generate: " + FILE_PATH)
                );

        System.out.println("-----------------------------");
    }

    public Flux<String> readWithFluxCreate(String path) {
        return Flux.create(stringFluxSink -> {
            try (FileInputStream fileInputStream = new FileInputStream(path); Scanner scanner = new Scanner(fileInputStream)) {
                while (scanner.hasNextLine()) {
                    String nextLine = scanner.nextLine();
                    Util.waitForHalfOf(1);
                    stringFluxSink.next(nextLine);
                }
                stringFluxSink.complete();
            } catch (IOException e) {
                stringFluxSink.error(e);
            }
        });
    }

    public Flux<String> readWithFluxGenerate(String path) {
        return Flux.generate(
                () -> {
                    FileInputStream fileInputStream = new FileInputStream(path);
                    return new Scanner(fileInputStream);
                },
                (scanner, synchronousSink) -> {
                    if (scanner.hasNextLine()) {
                        String nextLine = scanner.nextLine();
                        Util.waitForHalfOf(1);
                        synchronousSink.next(nextLine);
                    } else {
                        synchronousSink.complete();
                    }
                    return scanner;
                },
                scanner -> {
                    System.out.println("closing file");
                    scanner.close();
                    System.out.println("closed file");
                });
    }
}
