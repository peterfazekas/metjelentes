package hu.met.domain.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileWriter {

    public void write(String filename, List<String> content) {
        try {
            Files.write(Paths.get(filename), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
