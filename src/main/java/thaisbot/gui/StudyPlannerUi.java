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
        addMessage("Hello! I'm Thai's Bot.");
        addMessage("I can help you keep track of tasks, deadlines, and events.");
    }

    @Override
    public void showBye() {
        addMessage("Bye. Hope to see you again soon!");
    }

    @Override
    public void showError(String message) {
        addMessage("Error: " + message);
    }

    @Override
    public void showTaskList(TaskList tasks) {
        addMessage("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            addMessage((i + 1) + "." + tasks.get(i));
        }
    }

    @Override
    public void showTaskAdded(Task task, int taskCount) {
        addMessage("Got it. I've added this task:");
        addMessage("  " + task);
        addMessage("Now you have " + taskCount + " tasks in the list.");
    }

    @Override
    public void showTaskMarkedDone(Task task) {
        addMessage("Nice! I've marked this task as done:");
        addMessage("  " + task);
    }

    @Override
    public void showTaskMarkedNotDone(Task task) {
        addMessage("OK, I've marked this task as not done yet:");
        addMessage("  " + task);
    }

    @Override
    public void showTaskRemoved(Task task, int taskCount) {
        addMessage("Noted. I've removed this task:");
        addMessage("  " + task);
        addMessage("Now you have " + taskCount + " tasks in the list.");
    }

    @Override
    public void showTasksOnDate(TaskList tasks, LocalDate date) {
        addMessage("Here are the deadlines and events on " + date + ":");
        int shownCount = 0;
        for (Task task : tasks) {
            if (task.occursOn(date)) {
                shownCount++;
                addMessage(shownCount + "." + task);
            }
        }
        if (shownCount == 0) {
            addMessage("No deadlines or events found on that date.");
        }
    }

    @Override
    public void showMatchingTasks(TaskList tasks, String keyword) {
        addMessage("Here are the matching tasks in your list:");
        int shownCount = 0;
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                shownCount++;
                addMessage(shownCount + "." + task);
            }
        }
        if (shownCount == 0) {
            addMessage("No matching tasks found.");
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

    private void addMessage(String message) {
        messages.add(message);
    }
}
