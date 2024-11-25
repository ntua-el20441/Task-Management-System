package storage;

import models.Task;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TaskLoader {
    public static List<Task> loadTasksFromJson(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Task> tasks = objectMapper.readValue(new File(filePath), new TypeReference<List<Task>>() {});
            System.out.println("Tasks successfully loaded from JSON");
            return tasks;
        } catch (IOException e) {
            System.err.println("Error reading tasks from JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveTasksToJson(String filePath, List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            System.out.println("No tasks to save. Skipping file write.");
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(filePath), tasks);
        } catch (IOException e) {
            System.err.println("Error writing tasks to JSON: " + e.getMessage());
        }
    }
}