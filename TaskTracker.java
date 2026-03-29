import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TaskTracker {
    private static final String FILE_NAME = "tasks.txt";
    private static ArrayList<Task> tasks = new ArrayList<>();

    // Internal Task Class to store more data
    static class Task {
        String description;
        boolean isDone;

        Task(String description, boolean isDone) {
            this.description = description;
            this.isDone = isDone;
        }

        @Override
        public String toString() {
            String status = isDone ? "[DONE] " : "[PENDING] ";
            return status + description;
        }

        // Format for saving to file: "description|isDone"
        public String toFileString() {
            return description + "|" + isDone;
        }
    }

    public static void main(String[] args) {
        loadTasksFromFile();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n1. Add | 2. View | 3. Mark Done | 4. Remove | 5. Exit");
            System.out.print("Choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Task description: ");
                    tasks.add(new Task(scanner.nextLine(), false));
                    saveTasksToFile();
                    break;
                case 2:
                    displayTasks();
                    break;
                case 3:
                    System.out.print("Enter task number to complete: ");
                    int doneIdx = scanner.nextInt();
                    if (doneIdx > 0 && doneIdx <= tasks.size()) {
                        tasks.get(doneIdx - 1).isDone = true;
                        saveTasksToFile();
                        System.out.println("Updated!");
                    }
                    break;
                case 4:
                    System.out.print("Enter task number to remove: ");
                    int remIdx = scanner.nextInt();
                    if (remIdx > 0 && remIdx <= tasks.size()) {
                        tasks.remove(remIdx - 1);
                        saveTasksToFile();
                    }
                    break;
                case 5:
                    running = false;
                    break;
            }
        }
        scanner.close();
    }

    private static void displayTasks() {
        System.out.println("\n--- Your Tasks ---");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    private static void saveTasksToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Task t : tasks) {
                writer.println(t.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Save error: " + e.getMessage());
        }
    }

    private static void loadTasksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    tasks.add(new Task(parts[0], Boolean.parseBoolean(parts[1])));
                }
            }
        } catch (IOException e) {
            System.out.println("Load error.");
        }
    }
}
