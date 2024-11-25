package storage;

import models.Reminder;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReminderLoader {
    public static List<Reminder> loadRemindersFromJson(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Reminder> reminders = objectMapper.readValue(new File(filePath), new TypeReference<List<Reminder>>() {});
            System.out.println("Reminders successfully loaded from JSON");
            return reminders;
        } catch (IOException e) {
            System.err.println("Error reading Reminders from JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveRemindersToJson(String filePath, List<Reminder> reminders) {
        if (reminders == null || reminders.isEmpty()) {
            System.out.println("No Reminders to save. Skipping file write.");
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(filePath), reminders);
        } catch (IOException e) {
            System.err.println("Error writing Reminders to JSON: " + e.getMessage());
        }
    }
}