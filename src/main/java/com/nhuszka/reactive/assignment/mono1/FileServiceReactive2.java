package com.nhuszka.reactive.assignment.mono1;

import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class FileServiceReactive2 {
    private static final Path FILE_PATH = Paths.get("/home/nandor/repo/textFile.txt");
    private static final String CONTENT = "File-Content-Here";

    public static void main(String[] args) {
        write().subscribe(onNext(), onError(), onComplete());
        read().subscribe(onNext(), onError(), onComplete());
        delete().subscribe(onNext(), onError(), onComplete());
    }

    private static Runnable onComplete() {
        return () -> System.out.print("DONE");
    }

    private static Mono<String> read() {
        return Mono.fromSupplier(() -> readLines());
    }

    private static Mono<Void> write() {
        return Mono.fromRunnable(() -> writeFile());
    }

    private static Mono<Void> delete() {
        return Mono.fromRunnable(() -> deleteFile());
    }

    private static void writeFile() {
        try {
            Files.createFile(FILE_PATH);
            Files.write(FILE_PATH, CONTENT.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Consumer<Throwable> onError() {
        return error -> System.out.println("error " + error);
    }

    private static Consumer<Object> onNext() {
        return item -> System.out.println("next");
    }

    private static String readLines() {
        try {
            return String.join("", Files.readAllLines(FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteFile() {
        try {
            Files.delete(FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
