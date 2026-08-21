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
                int taskNumber = Integer.parseInt(parts[1]);
                Task task = tasks.get(taskNumber - 1);
                task.markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + task);
            } else if (command.equals("unmark")) {
                int taskNumber = Integer.parseInt(parts[1]);
                Task task = tasks.get(taskNumber - 1);
                task.unmarkAsDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + task);
            } else if (command.equals("todo")) {
                String description = parts.length > 1 ? parts[1] : "";
                Task task = new Todo(description);
                tasks.add(task);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + task);
                System.out.println("Now you have " + tasks.size() + " tasks in the list.");
            } else if (command.equals("deadline")) {
                String[] deadlineParts = parts.length > 1 ? parts[1].split(" /by ", 2) : new String[0];
                if (deadlineParts.length == 2) {
                    Task task = new Deadline(deadlineParts[0], deadlineParts[1]);
                    tasks.add(task);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                }
            } else if (command.equals("event")) {
                String[] eventParts = parts.length > 1 ? parts[1].split(" /from | /to ", 3) : new String[0];
                if (eventParts.length == 3) {
                    Task task = new Event(eventParts[0], eventParts[1], eventParts[2]);
                    tasks.add(task);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                }
            } else {
                Task task = new Todo(userInput);
                tasks.add(task);
                System.out.println("added: " + userInput);
            }
        }
    }
}
