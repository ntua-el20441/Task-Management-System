package storage;

import models.Category;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CategoryLoader {
    public static List<Category> loadCategoriesFromJson(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Category> categories = objectMapper.readValue(new File(filePath), new TypeReference<List<Category>>() {});
            System.out.println("Categories successfully loaded from JSON");
            return categories;
        } catch (IOException e) {
            System.err.println("Error reading categories from JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveCategoriesToJson(String filePath, List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            System.out.println("No categories to save. Skipping file write.");
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(filePath), categories);
        } catch (IOException e) {
            System.err.println("Error writing tasks to JSON: " + e.getMessage());
        }
    }
}