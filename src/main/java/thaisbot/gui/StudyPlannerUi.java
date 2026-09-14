package thaisbot.gui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

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
        addMessages("Hello! I'm Thai's Bot. Let's make the chaos manageable.",
                "Bring me tasks, deadlines, events, and tags.");
    }

    @Override
    public void showBye() {
        addMessages("All done for now. Come back whenever the stack grows.");
    }

    @Override
    public void showError(String message) {
        addMessages("Oops — Thai's Bot got tangled up: " + message);
    }

    @Override
    public void showTaskList(TaskList tasks) {
        addMessages("Here's your task lineup:");
        IntStream.range(0, tasks.size())
                .mapToObj(i -> (i + 1) + "." + tasks.get(i))
                .forEach(this::addMessage);
    }

    @Override
    public void showTaskAdded(Task task, int taskCount) {
        addMessages(
                "Nice! I've tucked this one into the lineup:",
                "  " + task,
                "Now you have " + taskCount + " tasks in the list."
        );
    }

    @Override
    public void showTaskMarkedDone(Task task) {
        addMessages("Great job. This task is officially done:",
                "  " + task);
    }

    @Override
    public void showTaskMarkedNotDone(Task task) {
        addMessages("No worries. I've put this one back on the list:",
                "  " + task);
    }

    @Override
    public void showTaskRemoved(Task task, int taskCount) {
        addMessages(
                "Out it goes:",
                "  " + task,
                "Now you have " + taskCount + " tasks in the list."
        );
    }

    @Override
    public void showTasksOnDate(TaskList tasks, LocalDate date) {
        addMessages("Let's check what's on " + date + ":");
        AtomicInteger shownCount = new AtomicInteger();
        StreamSupport.stream(tasks.spliterator(), false)
                .filter(task -> task.occursOn(date))
                .forEach(task -> addMessage(shownCount.incrementAndGet() + "." + task));
        if (shownCount.get() == 0) {
            addMessages("No deadlines or events found on that date.");
        }
    }

    @Override
    public void showMatchingTasks(TaskList tasks, String keyword) {
        addMessages("Here are the matches I found:");
        AtomicInteger shownCount = new AtomicInteger();
        StreamSupport.stream(tasks.spliterator(), false)
                .filter(task -> task.matchesSearch(keyword))
                .forEach(task -> addMessage(shownCount.incrementAndGet() + "." + task));
        if (shownCount.get() == 0) {
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
