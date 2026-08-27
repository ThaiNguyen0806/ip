package thaisbot.task;

import java.time.LocalDate;

public class Task {
    protected String description;
    protected TaskStatus status;

    public Task(String description) {
        this.description = description;
        this.status = TaskStatus.NOT_DONE;
    }

    public void markAsDone() {
        this.status = TaskStatus.DONE;
    }

    public void unmarkAsDone() {
        this.status = TaskStatus.NOT_DONE;
    }

    public String getStatusIcon() {
        return status.getIcon();
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String toFileString() {
        String doneFlag = status == TaskStatus.DONE ? "1" : "0";
        return "T | " + doneFlag + " | " + description;
    }

    public boolean occursOn(LocalDate date) {
        return false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
