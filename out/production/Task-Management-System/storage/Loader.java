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
        try {
            return objectMapper.readValue(new File(filePath), typeReference);
        } catch (IOException e) {
            System.err.println("Error reading data from JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static <T> void saveToJson(String filePath, List<T> data) {
        if (data == null || data.isEmpty()) {
            System.out.println("No data to save. Skipping file write.");
            return;
        }

        try {
            objectMapper.writeValue(new File(filePath), data);
        } catch (IOException e) {
            System.err.println("Error writing data to JSON: " + e.getMessage());
        }
    }
}
