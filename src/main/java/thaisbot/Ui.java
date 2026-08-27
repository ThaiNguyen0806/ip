package thaisbot;

import java.time.LocalDate;
import java.util.Scanner;

import thaisbot.task.Task;
import thaisbot.task.TaskList;

public class Ui {
    private final Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println("Hello! I'm Thai's Bot.");
        System.out.println("What can I do for you today :D?");
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public void showError(String message) {
        System.out.println("Error: " + message);
    }

    public void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    public void showTaskMarkedDone(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    public void showTaskMarkedNotDone(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    public void showTaskRemoved(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

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
