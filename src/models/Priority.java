package models;

import java.util.ArrayList;
import java.util.List;

public class Priority {

    private String name;
    private List<Task> tasks;

    public Priority() {
        this.name = "Default";
        this.tasks = new ArrayList<>();
    }

    public Priority(String name) {
        this.name = name != null && !name.isEmpty() ? name : "Default";
        this.tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null && !name.isEmpty() ? name : "Default";
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
        return "Priority{name='" + name + "', tasks=" + tasks + '}';
    }
}