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
            } else {
                tasks.add(new Task(userInput));
                System.out.println("added: " + userInput);
            }
        }
    }
}
