package thaisbot.task;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toFileString() {
        String doneFlag = status == TaskStatus.DONE ? "1" : "0";
        return "T | " + doneFlag + " | " + description;
    }
}
