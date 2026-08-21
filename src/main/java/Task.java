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

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
