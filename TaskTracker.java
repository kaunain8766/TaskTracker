import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TaskTracker {

    private static final String FILE_NAME = "tasks.json";

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");


    // Get current date and time
    private static String getCurrentTime() {
        return LocalDateTime.now().format(FORMATTER);
    }


    // Create file if it doesn't exist
    private static void initializeFile() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("[]");
            } catch (IOException e) {
                System.out.println("Error creating tasks.json");
            }
        }
    }


    // Read entire JSON file
    private static String readFile() {

        initializeFile();

        StringBuilder content = new StringBuilder();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

        } catch (IOException e) {
            System.out.println("Error reading tasks.json");
        }

        return content.toString();
    }


    // Save JSON content to file
    private static void saveFile(String content) {

        try (FileWriter writer = new FileWriter(FILE_NAME)) {

            writer.write(content);

        } catch (IOException e) {
            System.out.println("Error writing to tasks.json");
        }
    }


    // Escape special characters for JSON
    private static String escapeJson(String text) {

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }


    // Parse tasks from JSON
    private static List<Task> loadTasks() {

        List<Task> tasks = new ArrayList<>();

        String json = readFile().trim();

        if (json.equals("[]") || json.isEmpty()) {
            return tasks;
        }

        json = json.substring(1, json.length() - 1);

        String[] taskObjects = json.split("\\},\\s*\\{");

        for (String object : taskObjects) {

            object = object.replace("{", "")
                           .replace("}", "");

            String[] fields = object.split(",(?=\\s*\")");

            int id = 0;
            String description = "";
            String status = "";
            String createdAt = "";
            String updatedAt = "";

            for (String field : fields) {

                String[] keyValue = field.split(":", 2);

                if (keyValue.length != 2) {
                    continue;
                }

                String key = keyValue[0]
                        .trim()
                        .replace("\"", "");

                String value = keyValue[1]
                        .trim()
                        .replace("\"", "");

                switch (key) {

                    case "id":
                        id = Integer.parseInt(value);
                        break;

                    case "description":
                        description = value;
                        break;

                    case "status":
                        status = value;
                        break;

                    case "createdAt":
                        createdAt = value;
                        break;

                    case "updatedAt":
                        updatedAt = value;
                        break;
                }
            }

            tasks.add(new Task(
                    id,
                    description,
                    status,
                    createdAt,
                    updatedAt
            ));
        }

        return tasks;
    }


    // Convert tasks into JSON
    private static String convertToJson(List<Task> tasks) {

        StringBuilder json = new StringBuilder();

        json.append("[\n");

        for (int i = 0; i < tasks.size(); i++) {

            Task task = tasks.get(i);

            json.append("    {\n");

            json.append("        \"id\": ")
                    .append(task.id)
                    .append(",\n");

            json.append("        \"description\": \"")
                    .append(escapeJson(task.description))
                    .append("\",\n");

            json.append("        \"status\": \"")
                    .append(task.status)
                    .append("\",\n");

            json.append("        \"createdAt\": \"")
                    .append(task.createdAt)
                    .append("\",\n");

            json.append("        \"updatedAt\": \"")
                    .append(task.updatedAt)
                    .append("\"\n");

            json.append("    }");

            if (i < tasks.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("]");

        return json.toString();
    }


    // Generate new ID
    private static int generateId(List<Task> tasks) {

        int maxId = 0;

        for (Task task : tasks) {

            if (task.id > maxId) {
                maxId = task.id;
            }
        }

        return maxId + 1;
    }


    // Find task
    private static Task findTask(List<Task> tasks, int id) {

        for (Task task : tasks) {

            if (task.id == id) {
                return task;
            }
        }

        return null;
    }


    // ADD
    private static void addTask(String description) {

        List<Task> tasks = loadTasks();

        int id = generateId(tasks);

        String currentTime = getCurrentTime();

        Task task = new Task(
                id,
                description,
                "todo",
                currentTime,
                currentTime
        );

        tasks.add(task);

        saveFile(convertToJson(tasks));

        System.out.println(
                "Task added successfully (ID: " + id + ")"
        );
    }


    // UPDATE
    private static void updateTask(int id, String description) {

        List<Task> tasks = loadTasks();

        Task task = findTask(tasks, id);

        if (task == null) {

            System.out.println(
                    "Error: Task with ID " + id + " not found."
            );

            return;
        }

        task.description = description;
        task.updatedAt = getCurrentTime();

        saveFile(convertToJson(tasks));

        System.out.println("Task updated successfully.");
    }


    // DELETE
    private static void deleteTask(int id) {

        List<Task> tasks = loadTasks();

        Task task = findTask(tasks, id);

        if (task == null) {

            System.out.println(
                    "Error: Task with ID " + id + " not found."
            );

            return;
        }

        tasks.remove(task);

        saveFile(convertToJson(tasks));

        System.out.println("Task deleted successfully.");
    }


    // MARK TASK
    private static void markTask(int id, String status) {

        List<Task> tasks = loadTasks();

        Task task = findTask(tasks, id);

        if (task == null) {

            System.out.println(
                    "Error: Task with ID " + id + " not found."
            );

            return;
        }

        task.status = status;
        task.updatedAt = getCurrentTime();

        saveFile(convertToJson(tasks));

        System.out.println(
                "Task marked as " + status + "."
        );
    }


    // LIST TASKS
    private static void listTasks(String status) {

        List<Task> tasks = loadTasks();

        boolean found = false;

        for (Task task : tasks) {

            if (status == null || task.status.equals(status)) {

                System.out.println(
                        "[" + task.id + "] "
                        + task.description
                        + " (" + task.status + ")"
                );

                found = true;
            }
        }

        if (!found) {
            System.out.println("No tasks found.");
        }
    }


    // HELP
    private static void showHelp() {

        System.out.println("""
                
                Task Tracker CLI

                Commands:

                add "task description"
                    Add a new task

                update <id> "description"
                    Update a task

                delete <id>
                    Delete a task

                mark-in-progress <id>
                    Mark task as in-progress

                mark-done <id>
                    Mark task as done

                list
                    List all tasks

                list done
                    List completed tasks

                list todo
                    List todo tasks

                list in-progress
                    List in-progress tasks
                
                """);
    }


    // MAIN
    public static void main(String[] args) {

        if (args.length == 0) {
            showHelp();
            return;
        }

        String command = args[0];

        try {

            switch (command) {

                case "add":

                    if (args.length < 2) {
                        System.out.println(
                                "Usage: java TaskTracker add \"description\""
                        );
                        return;
                    }

                    addTask(joinArguments(args, 1));
                    break;


                case "update":

                    if (args.length < 3) {
                        System.out.println(
                                "Usage: java TaskTracker update <id> \"description\""
                        );
                        return;
                    }

                    int updateId = Integer.parseInt(args[1]);

                    updateTask(
                            updateId,
                            joinArguments(args, 2)
                    );

                    break;


                case "delete":

                    if (args.length != 2) {
                        System.out.println(
                                "Usage: java TaskTracker delete <id>"
                        );
                        return;
                    }

                    int deleteId = Integer.parseInt(args[1]);

                    deleteTask(deleteId);

                    break;


                case "mark-in-progress":

                    if (args.length != 2) {
                        System.out.println(
                                "Usage: java TaskTracker mark-in-progress <id>"
                        );
                        return;
                    }

                    int progressId = Integer.parseInt(args[1]);

                    markTask(progressId, "in-progress");

                    break;


                case "mark-done":

                    if (args.length != 2) {
                        System.out.println(
                                "Usage: java TaskTracker mark-done <id>"
                        );
                        return;
                    }

                    int doneId = Integer.parseInt(args[1]);

                    markTask(doneId, "done");

                    break;


                case "list":

                    if (args.length == 1) {

                        listTasks(null);

                    } else if (args.length == 2) {

                        String status = args[1];

                        if (!status.equals("todo")
                                && !status.equals("done")
                                && !status.equals("in-progress")) {

                            System.out.println(
                                    "Error: Invalid status."
                            );

                            return;
                        }

                        listTasks(status);

                    } else {

                        System.out.println(
                                "Usage: java TaskTracker list [status]"
                        );
                    }

                    break;


                default:

                    System.out.println(
                            "Unknown command: " + command
                    );

                    showHelp();
            }

        } catch (NumberFormatException e) {

            System.out.println(
                    "Error: Task ID must be a number."
            );
        }
    }


    // Join arguments
    private static String joinArguments(String[] args, int start) {

        StringBuilder result = new StringBuilder();

        for (int i = start; i < args.length; i++) {

            if (i > start) {
                result.append(" ");
            }

            result.append(args[i]);
        }

        return result.toString();
    }
}
