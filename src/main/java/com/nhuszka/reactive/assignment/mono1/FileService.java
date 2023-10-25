package com.nhuszka.reactive.assignment.mono1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileService {
    private static final Path FILE_PATH = Paths.get("/home/nandor/repo/textFile.txt");
    private static final String CONTENT = "File-Content-Here";

    public static void main(String[] args) {
        FileService fileService = new FileService();
        fileService.createFileWriteContent();
        fileService.readFileReadContent();
        fileService.deleteFile();
    }

    public void readFileReadContent() {
        try {
            List<String> contents = Files.readAllLines(FILE_PATH);
            System.out.println(contents);
        } catch (IOException e) {
            System.out.println("file read error" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void createFileWriteContent() {
        try {
            Files.createFile(FILE_PATH);
            Files.write(FILE_PATH, CONTENT.getBytes());

        } catch (IOException e) {
            System.out.println("file write error" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteFile() {
        try {
            Files.delete(FILE_PATH);
        } catch (IOException e) {
            System.out.println("file delete error" + e.getMessage());
            e.printStackTrace();
        }
    }
}
