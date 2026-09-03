package thaisbot.gui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import thaisbot.Ui;
import thaisbot.task.Task;
import thaisbot.task.TaskList;

/**
 * Captures application messages for display in the JavaFX window.
 */
public class StudyPlannerUi extends Ui {
    private final List<String> messages = new ArrayList<>();

    /**
     * Creates a GUI-facing UI helper.
     */
    public StudyPlannerUi() {
        super();
    }

    @Override
    public String readCommand() {
        throw new UnsupportedOperationException("The GUI does not read commands from the console.");
    }

    @Override
    public void showWelcome() {
        addMessages("Hello! I'm Thai's Bot.",
                "I can help you keep track of tasks, deadlines, and events.");
    }

    @Override
    public void showBye() {
        addMessages("Bye. Hope to see you again soon!");
    }

    @Override
    public void showError(String message) {
        addMessages("Error: " + message);
    }

    @Override
    public void showTaskList(TaskList tasks) {
        addMessages("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            addMessages((i + 1) + "." + tasks.get(i));
        }
    }

    @Override
    public void showTaskAdded(Task task, int taskCount) {
        addMessages(
                "Got it. I've added this task:",
                "  " + task,
                "Now you have " + taskCount + " tasks in the list."
        );
    }

    @Override
    public void showTaskMarkedDone(Task task) {
        addMessages("Nice! I've marked this task as done:",
                "  " + task);
    }

    @Override
    public void showTaskMarkedNotDone(Task task) {
        addMessages("OK, I've marked this task as not done yet:",
                "  " + task);
    }

    @Override
    public void showTaskRemoved(Task task, int taskCount) {
        addMessages(
                "Noted. I've removed this task:",
                "  " + task,
                "Now you have " + taskCount + " tasks in the list."
        );
    }

    @Override
    public void showTasksOnDate(TaskList tasks, LocalDate date) {
        addMessages("Here are the deadlines and events on " + date + ":");
        int shownCount = 0;
        for (Task task : tasks) {
            if (task.occursOn(date)) {
                shownCount++;
                addMessages(shownCount + "." + task);
            }
        }
        if (shownCount == 0) {
            addMessages("No deadlines or events found on that date.");
        }
    }

    @Override
    public void showMatchingTasks(TaskList tasks, String keyword) {
        addMessages("Here are the matching tasks in your list:");
        int shownCount = 0;
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                shownCount++;
                addMessages(shownCount + "." + task);
            }
        }
        if (shownCount == 0) {
            addMessages("No matching tasks found.");
        }
    }

    /**
     * Returns all buffered messages and clears the buffer.
     * @return buffered messages
     */
    public List<String> drainMessages() {
        List<String> copy = new ArrayList<>(messages);
        messages.clear();
        return copy;
    }

    /**
     * Adds one or more messages to the buffer.
     * @param newMessages messages to add
     */
    private void addMessages(String... newMessages) {
        for (String message : newMessages) {
            addMessage(message);
        }
    }

    private void addMessage(String message) {
        messages.add(message);
    }
}
