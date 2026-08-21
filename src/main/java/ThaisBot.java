import java.util.ArrayList;
import java.util.Scanner;

public class ThaisBot {
    public static void main(String[] args) {
        System.out.println("Hello! I'm Thai's Bot.");
        System.out.println("What can I do for you today?");

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine().trim();
            
            try {
                String[] parts = userInput.split("\\s+", 2);
                String command = parts[0];

                if (command.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    break;
                } else if (command.equals("list")) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i));
                    }
                } else if (command.equals("mark")) {
                    if (parts.length < 2) {
                        throw new ThaisBotException("Please provide a task number to mark.");
                    }
                    try {
                        int taskNumber = Integer.parseInt(parts[1]);
                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            throw new ThaisBotException("Task number out of range.");
                        }
                        Task task = tasks.get(taskNumber - 1);
                        task.markAsDone();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + task);
                    } catch (NumberFormatException e) {
                        throw new ThaisBotException("Task number must be a valid integer.");
                    }
                } else if (command.equals("unmark")) {
                    if (parts.length < 2) {
                        throw new ThaisBotException("Please provide a task number to unmark.");
                    }
                    try {
                        int taskNumber = Integer.parseInt(parts[1]);
                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            throw new ThaisBotException("Task number out of range.");
                        }
                        Task task = tasks.get(taskNumber - 1);
                        task.unmarkAsDone();
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  " + task);
                    } catch (NumberFormatException e) {
                        throw new ThaisBotException("Task number must be a valid integer.");
                    }
                } else if (command.equals("todo")) {
                    if (parts.length < 2 || parts[1].trim().isEmpty()) {
                        throw new ThaisBotException("The description of a todo cannot be empty.");
                    }
                    Task task = new Todo(parts[1]);
                    tasks.add(task);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.equals("deadline")) {
                    if (parts.length < 2) {
                        throw new ThaisBotException("Please provide a deadline in the format: deadline <description> /by <time>");
                    }
                    String[] deadlineParts = parts[1].split(" /by ", 2);
                    if (deadlineParts.length != 2 || deadlineParts[0].trim().isEmpty() || deadlineParts[1].trim().isEmpty()) {
                        throw new ThaisBotException("Please provide a deadline in the format: deadline <description> /by <time>");
                    }
                    Task task = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
                    tasks.add(task);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.equals("event")) {
                    if (parts.length < 2) {
                        throw new ThaisBotException("Please provide an event in the format: event <description> /from <time> /to <time>");
                    }
                    String[] eventParts = parts[1].split(" /from | /to ", 3);
                    if (eventParts.length != 3 || eventParts[0].trim().isEmpty() || eventParts[1].trim().isEmpty() || eventParts[2].trim().isEmpty()) {
                        throw new ThaisBotException("Please provide an event in the format: event <description> /from <time> /to <time>");
                    }
                    Task task = new Event(eventParts[0].trim(), eventParts[1].trim(), eventParts[2].trim());
                    tasks.add(task);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.equals("delete")) {
                    if (parts.length < 2) {
                        throw new ThaisBotException("Please provide a task number to delete.");
                    }
                    try {
                        int taskNumber = Integer.parseInt(parts[1]);
                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            throw new ThaisBotException("Task number out of range.");
                        }
                        Task removedTask = tasks.remove(taskNumber - 1);
                        System.out.println("Noted. I've removed this task:");
                        System.out.println("  " + removedTask);
                        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    } catch (NumberFormatException e) {
                        throw new ThaisBotException("Task number must be a valid integer.");
                    }
                } else {
                    throw new ThaisBotException("I'm sorry, but I don't know what that means :( . Please try again!");
                }
            } catch (ThaisBotException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
