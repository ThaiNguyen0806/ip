package thaisbot.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("MMM d yyyy HHmm");

    private final LocalDateTime by;
    private final boolean hasTime;

    public Deadline(String description, LocalDateTime by, boolean hasTime) {
        super(description);
        this.by = by;
        this.hasTime = hasTime;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + formatDateTime(by, hasTime) + ")";
    }

    @Override
    public String toFileString() {
        String doneFlag = status == TaskStatus.DONE ? "1" : "0";
        return "D | " + doneFlag + " | " + description + " | " + by + " | " + toFlag(hasTime);
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return by.toLocalDate().equals(date);
    }

    private String formatDateTime(LocalDateTime value, boolean hasTimePart) {
        if (hasTimePart) {
            return value.format(DATE_TIME_FORMATTER);
        }
        return value.format(DATE_FORMATTER);
    }

    private String toFlag(boolean value) {
        if (value) {
            return "1";
        }
        return "0";
    }
}
