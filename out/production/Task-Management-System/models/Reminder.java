package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Reminder {
    private String date;
    private int taskId;

    public Reminder() {
    }

    public Reminder(String date, int taskId) {
        setDate(date);
        this.taskId = taskId;
    }

    public Reminder(String date) {
        setDate(date);
        this.taskId = 0;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public static boolean isValidDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date detected: " + date);
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reminder reminder = (Reminder) o;

        if (taskId != reminder.taskId) return false;
        return date != null ? date.equals(reminder.date) : reminder.date == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + taskId;
        return result;
    }

    @Override
    public String toString() {
        return String.format("Reminder[date='%s', taskId=%d]", date, taskId);
    }
}
