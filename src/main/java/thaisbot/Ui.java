package thaisbot;

import java.time.LocalDate;
import java.util.Scanner;

import thaisbot.task.Task;
import thaisbot.task.TaskList;

/**
 * Handles input/output with the user. Responsible for printing messages to the console and
 * reading user input.
 */
public class Ui {
    private final Scanner scanner;

    /**
     * Constructs a Ui that reads from System.in.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Shows the welcome message when the application starts.
     */
    public void showWelcome() {
        System.out.println("Hello! I'm Thai's Bot.");
        System.out.println("What can I do for you today :D?");
    }

    /**
     * Reads a line of input from the user.
     * @return trimmed user input line
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Shows the goodbye message when the application exits.
     */
    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Shows an error message to the user.
     * @param message the error description
     */
    public void showError(String message) {
        System.out.println("Error: " + message);
    }

    /**
     * Displays the full task list.
     * @param tasks the task list to display
     */
    public void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays a message after a task is added.
     * @param task the task that was added
     * @param taskCount current number of tasks
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays a message after a task is marked done.
     * @param task the task that was marked
     */
    public void showTaskMarkedDone(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Displays a message after a task is unmarked.
     * @param task the task that was unmarked
     */
    public void showTaskMarkedNotDone(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Displays a message after a task is removed.
     * @param task the task that was removed
     * @param taskCount current number of tasks
     */
    public void showTaskRemoved(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Shows deadlines and events that occur on a specific date.
     * @param tasks the task list to search
     * @param date the date to match
     */
    public void showTasksOnDate(TaskList tasks, LocalDate date) {
        System.out.println("Here are the deadlines and events on " + date + ":");
        int shownCount = 0;
        for (Task task : tasks) {
            if (task.occursOn(date)) {
                shownCount++;
                System.out.println(shownCount + "." + task);
            }
        }
        if (shownCount == 0) {
            System.out.println("No deadlines or events found on that date.");
        }
    }

    /**
     * Shows tasks whose descriptions contain the given keyword.
     * @param tasks the task list to search
     * @param keyword the keyword to match
     */
    public void showMatchingTasks(TaskList tasks, String keyword) {
        System.out.println("Here are the matching tasks in your list:");
        int shownCount = 0;
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                shownCount++;
                System.out.println(shownCount + "." + task);
            }
        }
        if (shownCount == 0) {
            System.out.println("No matching tasks found.");
        }
    }
}
