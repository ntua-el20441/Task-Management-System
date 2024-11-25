package storage;

import models.Priority;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PriorityLoader {
    public static List<Priority> loadPrioritiesFromJson(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Priority> priorities = objectMapper.readValue(new File(filePath), new TypeReference<List<Priority>>() {});
            System.out.println("Priorities successfully loaded from JSON");
            return priorities;
        } catch (IOException e) {
            System.err.println("Error reading priorities from JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void savePrioritiesToJson(String filePath, List<Priority> priorities) {
        if (priorities == null || priorities.isEmpty()) {
            System.out.println("No priorities to save. Skipping file write.");
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(filePath), priorities);
        } catch (IOException e) {
            System.err.println("Error writing priorities to JSON: " + e.getMessage());
        }
    }
}
