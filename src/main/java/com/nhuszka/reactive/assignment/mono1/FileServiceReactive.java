package com.nhuszka.reactive.assignment.mono1;

import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class FileServiceReactive {
    private static final Path FILE_PATH = Paths.get("/home/nandor/repo/textFile.txt");
    private static final String CONTENT = "File-Content-Here";

    public static void main(String[] args) {
        Mono.fromRunnable(getFileWriteJob())
                .subscribe(
                        getNext(),
                        onError(),
                        () -> Mono.fromRunnable(getReadLinesJob())
                                .subscribe(
                                        getNext(),
                                        onError(),
                                        () -> Mono.fromRunnable(getDeleteFileJob())
                                                .subscribe(
                                                        getNext(),
                                                        onError(),
                                                        () -> System.out.println("DONE")
                                                )
                                )
                );
    }

    private static Consumer<Throwable> onError() {
        return error -> System.out.println("error " + error);
    }

    private static Consumer<Object> getNext() {
        return item -> System.out.println("next");
    }

    private static Runnable getReadLinesJob() {
        return () -> {
            try {
                System.out.println(Files.readAllLines(FILE_PATH));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Runnable getFileWriteJob() {
        return () -> {
            try {
                Files.createFile(FILE_PATH);
                Files.write(FILE_PATH, CONTENT.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Runnable getDeleteFileJob() {
        return () -> {
            try {
                Files.delete(FILE_PATH);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

}
