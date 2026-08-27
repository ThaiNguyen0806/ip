import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter DATE_TIME_COLON_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public String[] splitUserInput(String userInput) {
        return userInput.trim().split("\\s+", 2);
    }

    public int parseTaskNumber(String taskNumberText, int taskCount) throws ThaisBotException {
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new ThaisBotException("Task number out of range.");
            }
            return taskNumber;
        } catch (NumberFormatException e) {
            throw new ThaisBotException("Task number must be a valid integer.");
        }
    }

    public LocalDate parseDate(String input) throws ThaisBotException {
        try {
            return LocalDate.parse(input, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ThaisBotException("Date must use yyyy-MM-dd format.");
        }
    }

    public ParsedDateTime parseDateTime(String input, String errorMessage)
            throws ThaisBotException {
        try {
            LocalDateTime value = LocalDateTime.parse(input, DATE_TIME_FORMATTER);
            return new ParsedDateTime(value, true);
        } catch (DateTimeParseException ignored) {
            // Try next format.
        }

        try {
            LocalDateTime value = LocalDateTime.parse(input, DATE_TIME_COLON_FORMATTER);
            return new ParsedDateTime(value, true);
        } catch (DateTimeParseException ignored) {
            // Try date-only format.
        }

        try {
            LocalDate date = LocalDate.parse(input, DATE_FORMATTER);
            return new ParsedDateTime(date.atStartOfDay(), false);
        } catch (DateTimeParseException e) {
            throw new ThaisBotException(errorMessage);
        }
    }

    public String[] parseDeadlineParts(String input, String usageMessage) throws ThaisBotException {
        String[] parts = input.split(" /by ", 2);
        if (parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new ThaisBotException(usageMessage);
        }
        return parts;
    }

    public String[] parseEventParts(String input, String usageMessage) throws ThaisBotException {
        String[] parts = input.split(" /from | /to ", 3);
        if (parts.length != 3
                || parts[0].trim().isEmpty()
                || parts[1].trim().isEmpty()
                || parts[2].trim().isEmpty()) {
            throw new ThaisBotException(usageMessage);
        }
        return parts;
    }

    public static class ParsedDateTime {
        private final LocalDateTime value;
        private final boolean hasTime;

        public ParsedDateTime(LocalDateTime value, boolean hasTime) {
            this.value = value;
            this.hasTime = hasTime;
        }

        public LocalDateTime getValue() {
            return value;
        }

        public boolean hasTime() {
            return hasTime;
        }
    }
}
