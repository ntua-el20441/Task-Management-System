/**
 * The Task class represents a task with attributes like title, description, category,
 * priority, status, deadline, and associated reminders. It provides functionality
 * to manage reminders, track status, and check deadlines.
 */
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

    /**
     * Enumeration representing the possible statuses of a task.
     */
    public enum Status {
        OPEN, IN_PROGRESS, POSTPONED, COMPLETED, DELAYED
    }

    /**
     * Default constructor that initializes a new Task with an auto-incremented ID
     * and an empty list of reminders.
     */
    public Task() {
        this.id = nextId++; 
        this.reminders = new ArrayList<>();
    }

    /**
     * Constructor to create a Task with the specified attributes.
     *
     * @param title       the title of the task
     * @param description a brief description of the task
     * @param category    the category to which the task belongs
     * @param priority    the priority level of the task
     * @param deadline    the deadline for the task in "yyyy-MM-dd" format
     */
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

    /**
     * Constructor to create a Task with the specified attributes including status.
     *
     * @param title       the title of the task
     * @param description a brief description of the task
     * @param category    the category to which the task belongs
     * @param priority    the priority level of the task
     * @param status      the current status of the task
     * @param deadline    the deadline for the task in "yyyy-MM-dd" format
     */
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

    /**
     * Gets the unique identifier of the task.
     *
     * @return the task ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the title of the task.
     *
     * @return the task title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the task.
     *
     * @param title the new title of the task
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the task.
     *
     * @param description the new description of the task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the category of the task.
     *
     * @return the task category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the task.
     *
     * @param category the new category of the task
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the priority of the task.
     *
     * @return the task priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the priority of the task.
     *
     * @param priority the new priority of the task
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Gets the status of the task.
     *
     * @return the task status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status of the task. If the status is set to COMPLETED, all reminders are cleared.
     *
     * @param status the new status of the task
     */
    public void setStatus(Status status) {
        this.status = status;
        if (status == Status.COMPLETED) {
            this.reminders.clear();
        }
    }

    /**
     * Gets the deadline of the task.
     *
     * @return the task deadline in "yyyy-MM-dd" format
     */
    public String getDeadline() {
        return deadline;
    }

    /**
     * Sets the deadline of the task and checks if the task is delayed.
     *
     * @param deadline the new deadline in "yyyy-MM-dd" format
     */
    public void setDeadline(String deadline) {
        this.deadline = deadline;
        checkDelayed();
    }

    /**
     * Gets the list of reminders for the task.
     *
     * @return a list of reminders
     */
    public List<Reminder> getReminders() {
        return reminders;
    }

    /**
     * Sets the reminders for the task. Invalid reminders with dates after the deadline are discarded.
     *
     * @param reminders a list of reminders to be set
     */
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

    /**
     * Adds a reminder to the task if the reminder date is before the deadline.
     *
     * @param reminder the reminder to add
     */
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

    /**
     * Removes a reminder from the task.
     *
     * @param reminder the reminder to remove
     */
    public void removeReminder(Reminder reminder) {
        reminders.remove(reminder);
    }

    /**
     * Marks the task as completed and clears all reminders.
     */
    public void completeTask() {
        this.status = Status.COMPLETED;
        reminders.clear();
    }

    /**
     * Checks if the task is delayed and updates the status accordingly.
     */
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

