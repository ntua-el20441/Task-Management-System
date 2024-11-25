# Task Management Application

**Task Management Application** is a Java-based tool for efficiently managing tasks. Designed with a user-friendly interface, this application helps users keep track of tasks with various features like reminders, priorities, search functionalities, and categorization.

---

## Features

- **Task Management**
  - Add, edit, and delete tasks.
  - Assign categories, priorities, deadlines, statuses and remindera to tasks.
  - Automatically clear reminders for completed tasks.
  
- **Category Management**
  - A Task belongs to exactly one Category.
  - Add, rename, and delete categories.
  - When a category is removed all its tasks are also removed. 

- **Priority Management**
  - Add, rename, and delete priorities.
  - When a priority is removed all its tasks are set to "Default". 

- **Reminder Management**
  - Add, edit, and delete reminders.
  - Set reminders for tasks with options like:
    - `One day before the deadline.`
    - `One week before the deadline.`
    - `One month before the deadline.`
    - `A custom date.`
  - Reminders belong to exactly one Task.
  - Tasks may have multiple Reminders.
  - Tasks marked as  `"Completed"` automatically delete their Reminders.
  -  `"Completed"` Tasks cannot have new Reminders.

    
- **Search Functionality**
  - Search for tasks by:
    - Title.
    - Category.
    - Priority.
    
- **Task Summaries**
  - View task summaries with statistics:
    - Total tasks.
    - Completed tasks.
    - Delayed tasks.
    - Tasks due within 7 days.

- **Data Persistence**
  - Automatically saves and loads tasks, categories, priorities, and reminders from JSON files in the `<medialab/>` folder

---

## Prerequisites

To run the application, ensure the following:
1. **Java Development Kit (JDK)** version 17 or later is installed.
2. **JavaFX SDK** is downloaded and properly configured.

---

## How to Run

1. **Clone the Repository**
   ```bash
   git clone https://github.com/danaespentz/task-management-application.git
   cd task-management-application
   ```

2. **Compile the Application**
   Use the following command to compile the source code:
   ```bash
   javac --module-path <path-to-javafx-sdk>/lib --add-modules javafx.controls -cp "lib/*" -d bin src/models/*.java src/application/*.java src/storage/*.java src/components/*.java
   ```
   Replace `<path-to-javafx-sdk>` with the directory where JavaFX is located.

3. **Run the Application**
   Navigate to the `bin` directory and run the application:
   ```bash
   java --module-path <path-to-javafx-sdk>/lib --add-modules javafx.controls -cp "bin:lib/*" application.Main
   ```

4. **Save and Load Data**

    **Application Initialization**  
        - During startup, the application retrieves all information stored in JSON files.
        - It initializes the corresponding objects in memory based on the data retrieved.

    **Application Termination**  
        - Before closing, the application updates the JSON files with the current state of the system.
        - It saves the complete state of the application to ensure all changes made during the session are preserved.