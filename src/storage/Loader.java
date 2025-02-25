package storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> List<T> loadFromJson(String filePath, TypeReference<List<T>> typeReference) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("File not found: " + filePath + ". Returning an empty list.");
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, typeReference);
        } catch (IOException e) {
            System.err.println("Error reading data from JSON file: " + filePath + ". Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static <T> void saveToJson(String filePath, List<T> data) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }

            objectMapper.writeValue(file, data == null ? new ArrayList<>() : data);
            System.out.println("Data successfully saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing data to JSON file: " + filePath + ". Error: " + e.getMessage());
        }
    }
}

