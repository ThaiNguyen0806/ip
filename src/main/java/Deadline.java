public class Deadline extends Task {
    private String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    @Override
    public String toFileString() {
        String doneFlag = status == TaskStatus.DONE ? "1" : "0";
        return "D | " + doneFlag + " | " + description + " | " + by;
    }
}
