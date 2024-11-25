package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Task {
    private static int nextId = 1; 

    private int id; 
    private String title;
    private String description;
    private String category;
    private String priority;
    private Status status;
    private String deadline;
    private List<Reminder> reminders;

    public enum Status {
        OPEN, IN_PROGRESS, POSTPONED, COMPLETED, DELAYED
    }

    public Task() {
        this.id = nextId++; 
        this.reminders = new ArrayList<>();
    }

    public Task(String title, String description, String category, String priority, String deadline) {
        this.id = nextId++; 
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = Status.OPEN;
        this.deadline = deadline;
        this.reminders = new ArrayList<>();
    }

    public Task(String title, String description, String category, String priority, Status status, String deadline) {
        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.deadline = deadline;
        this.reminders = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        if (status == Status.COMPLETED) {
            this.reminders.clear();
        }
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
        checkDelayed();
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate taskDeadline = LocalDate.parse(this.deadline, formatter);
        List<Reminder> validReminders = new ArrayList<>();

        for (Reminder reminder : reminders) {
            LocalDate reminderDate = LocalDate.parse(reminder.getDate(), formatter);
            if (!reminderDate.isBefore(taskDeadline)) {
                System.err.println("Invalid Reminder: Reminder date " + reminder.getDate() + " is after the deadline " + this.deadline + " for task ID " + this.id);
            } else {
                validReminders.add(reminder);
            }
        }

        this.reminders = validReminders;
    }

    public void addReminder(Reminder reminder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate taskDeadline = LocalDate.parse(this.deadline, formatter);
        LocalDate reminderDate = LocalDate.parse(reminder.getDate(), formatter);

        if (!reminderDate.isBefore(taskDeadline)) {
            System.err.println("Invalid Reminder: Reminder date " + reminder.getDate() + " is after the deadline " + this.deadline + " for task ID " + this.id);
        } else {
            this.reminders.add(reminder);
        }
    }

    public void removeReminder(Reminder reminder) {
        reminders.remove(reminder);
    }

    public void completeTask() {
        this.status = Status.COMPLETED;
        reminders.clear();
    }

    public void checkDelayed() {
        LocalDate currentDate = LocalDate.now();
        LocalDate taskDeadline = LocalDate.parse(deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (status != Status.COMPLETED && taskDeadline.isBefore(currentDate)) {
            this.status = Status.DELAYED;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
               Objects.equals(title, task.title) &&
               Objects.equals(description, task.description) &&
               Objects.equals(category, task.category) &&
               Objects.equals(priority, task.priority) &&
               status == task.status &&
               Objects.equals(deadline, task.deadline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, category, priority, status, deadline);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", priority='" + priority + '\'' +
                ", status=" + status +
                ", deadline='" + deadline + '\'' +
                ", reminders=" + reminders.size() +
                '}';
    }
}