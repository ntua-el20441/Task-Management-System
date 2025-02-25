# Welcome to the Task Management System

**This project was developed as part of a university project for the Multimedia Technologies course.**


The **Task Management System** is a powerful and intuitive Java application designed to streamline your task organization. With features like task categorization, reminders, priorities, and status tracking, this tool helps you managing your daily responsibilities.

---

## Key Capabilities

### 1. Task Handling
- Create, update, and delete tasks with ease.
- Assign tasks to categories and priorities.
- Track deadlines and statuses for better organization.
- Clear reminders automatically when tasks are marked "Completed."

### 2. Category Organization
- Manage task categories:
  - Add new categories.
  - Rename existing categories.
  - Delete categories (removes associated tasks as well).

### 3. Priority Levels
- Customize priorities for tasks:
  - Add or rename priority levels.
  - Delete priorities (tasks revert to "Default").

### 4. Smart Reminders
- Add and manage reminders for tasks:
  - Options for reminders: `One day before`, `One week before`, `One month before`, or custom dates.
  - Automatically delete reminders for completed tasks.
  - Prevent reminders for tasks marked "Completed."

### 5. Search and Summarize
- Search for tasks by title, category, or priority.
- View task summaries:
  - Total tasks.
  - Completed tasks.
  - Delayed tasks.
  - Tasks due within a week.

### 6. Data Persistence
- Automatically saves and retrieves data from JSON files located in the `<medialab/>` folder.
- Ensures the application state is preserved across sessions.

---

## Quick Start Guide

### Prerequisites
Ensure you have:
1. **JDK 17 or later** installed.
2. **JavaFX 17 SDK** downloaded and configured.

### Steps to Run the Application

1. **Clone the Repository**
   ```bash
   git clone https://github.com/ntua-el20441/Task-Management-System.git
   cd Task-Management-System
   ./run.sh

