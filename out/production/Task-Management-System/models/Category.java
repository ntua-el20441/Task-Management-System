package models;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String name;
    private List<Task> tasks;

    public Category() {
        this.tasks = new ArrayList<>();
    }

    public Category(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        if (!tasks.contains(task)) {
            tasks.add(task);
        }
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public boolean hasTask(Task task) {
        return tasks.contains(task);
    }

    @Override
    public String toString() {
        return "Category{name='" + name + "', tasks=" + tasks + '}';
    }
}