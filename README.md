[# Task Tracker CLI

A simple **Command Line Interface (CLI) Task Tracker** built using **Java**.
This project allows users to add, update, delete, and manage tasks directly from the command line.

Tasks are stored locally in a `tasks.json` file.

## Features

* Add a new task
* Update an existing task
* Delete a task
* Mark a task as **in-progress**
* Mark a task as **done**
* List all tasks
* List completed tasks
* List pending (`todo`) tasks
* List tasks currently in progress
* Automatically create `tasks.json` if it does not exist
* Store task creation and update timestamps
* Handle invalid inputs and task IDs

## Technologies Used

* Java
* Java File I/O
* JSON
* Command Line Arguments
* `ArrayList`
* `FileReader`
* `FileWriter`
* `BufferedReader`
* `LocalDateTime`

No external libraries or frameworks are used.

## Project Structure

```text
TaskTracker/
│
├── Task.java
├── TaskTracker.java
├── tasks.json
└── README.md
```

## Task Properties

Each task contains the following properties:

```json
{
    "id": 1,
    "description": "Learn Java",
    "status": "todo",
    "createdAt": "2026-08-28T01:20:00",
    "updatedAt": "2026-08-28T01:20:00"
}
```

### Status Values

A task can have one of three statuses:

* `todo` - Task has not been started
* `in-progress` - Task is currently being worked on
* `done` - Task has been completed

## How to Run

### 1. Clone the repository

```bash
git clone <your-github-repository-url>
```

### 2. Open the project

Open the project folder in VS Code or any Java IDE.

### 3. Compile the Java files

```bash
javac Task.java TaskTracker.java
```

### 4. Run the application

```bash
java TaskTracker
```

## Commands

### Add a Task

```bash
java TaskTracker add "Learn Java"
```

Output:

```text
Task added successfully (ID: 1)
```

### List All Tasks

```bash
java TaskTracker list
```

Example output:

```text
[1] Learn Java (todo)
[2] Practice DSA (done)
[3] Build Project (in-progress)
```

### Update a Task

```bash
java TaskTracker update 1 "Learn Java Collections"
```

### Delete a Task

```bash
java TaskTracker delete 1
```

### Mark Task as In Progress

```bash
java TaskTracker mark-in-progress 1
```

### Mark Task as Done

```bash
java TaskTracker mark-done 1
```

### List Completed Tasks

```bash
java TaskTracker list done
```

### List Todo Tasks

```bash
java TaskTracker list todo
```

### List In-Progress Tasks

```bash
java TaskTracker list in-progress
```

## Example Workflow

```bash
java TaskTracker add "Learn Java"
java TaskTracker add "Practice DSA"
java TaskTracker add "Build Task Tracker"

java TaskTracker list

java TaskTracker mark-in-progress 1
java TaskTracker mark-done 2

java TaskTracker update 3 "Build Task Tracker CLI"

java TaskTracker list
```

Example output:

```text
[1] Learn Java (in-progress)
[2] Practice DSA (done)
[3] Build Task Tracker CLI (todo)
```

## Data Storage

All tasks are stored in:

```text
tasks.json
```

The application automatically creates this file if it doesn't exist.

Example:

```json
[
    {
        "id": 1,
        "description": "Learn Java",
        "status": "done",
        "createdAt": "2026-08-28T01:20:00",
        "updatedAt": "2026-08-28T01:30:00"
    }
]
```

## Error Handling

The application handles common errors such as:

* Invalid task ID
* Task ID that does not exist
* Missing task description
* Invalid task status
* Unknown commands
* Missing `tasks.json` file
* Invalid command usage

## Learning Objectives

This project helps practice:

* Java programming
* Object-oriented programming
* Command-line arguments
* File handling
* JSON data storage
* Exception handling
* Collections
* Date and time handling
* Basic project structure
* Git and GitHub

## Future Improvements

Possible future improvements:

* Search tasks by keyword
* Add task priorities
* Add due dates
* Sort tasks by date or priority
* Add colored CLI output
* Create a proper `task-cli` executable command
* Add automated tests

## Author

**Md Kaunain Raza**

GitHub: `kaunain8766`

---

## License

This project is created for learning and educational purposes.
](https://roadmap.sh/projects/task-tracker)
