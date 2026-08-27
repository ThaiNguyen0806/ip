package thaisbot.task;

import java.time.LocalDate;

/**
 * Base class for tasks stored in the application. Subclasses represent specific task types
 * such as todos, deadlines and events.
 */
public class Task {
    protected String description;
    protected TaskStatus status;

    /**
     * Create a new Task with the given description. Status defaults to NOT_DONE.
     * @param description task description
     */
    public Task(String description) {
        this.description = description;
        this.status = TaskStatus.NOT_DONE;
    }

    /**
     * Mark this task as done.
     */
    public void markAsDone() {
        this.status = TaskStatus.DONE;
    }

    /**
     * Mark this task as not done.
     */
    public void unmarkAsDone() {
        this.status = TaskStatus.NOT_DONE;
    }

    /**
     * Return the status icon (e.g., X for done).
     * @return single-character status icon
     */
    public String getStatusIcon() {
        return status.getIcon();
    }

    /**
     * Set the task status explicitly.
     * @param status new status
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Convert the task to the pipe-separated format used for storage.
     * @return storage string
     */
    public String toFileString() {
        String doneFlag = status == TaskStatus.DONE ? "1" : "0";
        return "T | " + doneFlag + " | " + description;
    }

    /**
     * Returns true if this task occurs on the given date. Default implementation is false;
     * subclasses override it for date-aware tasks.
     * @param date date to test
     * @return true if the task occurs on date
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns the raw description string for text searches.
     * @return description
     */
    public String getDescription() {
        return description;
    }
}
