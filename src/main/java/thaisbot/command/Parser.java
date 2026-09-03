package thaisbot.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import thaisbot.ThaisBotException;
import thaisbot.command.commands.AddDeadlineCommand;
import thaisbot.command.commands.AddEventCommand;
import thaisbot.command.commands.AddTodoCommand;
import thaisbot.command.commands.DeleteCommand;
import thaisbot.command.commands.ExitCommand;
import thaisbot.command.commands.FindCommand;
import thaisbot.command.commands.FindOnDateCommand;
import thaisbot.command.commands.ListCommand;
import thaisbot.command.commands.MarkCommand;
import thaisbot.command.commands.UnmarkCommand;

/**
 * Parses user input into Command objects. Exposes helper parsing methods for the
 * different argument types used by commands.
 */
public class Parser {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter DATE_TIME_COLON_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Parses a raw user input line and returns the corresponding Command.
     * @param userInput raw input from the user
     * @return parsed Command
     * @throws ThaisBotException if the command or its arguments are invalid
     */
    public Command parse(String userInput) throws ThaisBotException {
        String[] parts = userInput.trim().split("\\s+", 2);
        String commandWord = parts[0];

        switch (commandWord) {
            case "bye":
                return new ExitCommand();
            case "list":
                return new ListCommand();
            case "mark":
                return new MarkCommand(parseTaskNumberArgument(parts,
                        "Please provide a task number to mark."));
            case "unmark":
                return new UnmarkCommand(parseTaskNumberArgument(parts,
                        "Please provide a task number to unmark."));
            case "todo":
                return new AddTodoCommand(parseTodoDescription(parts));
            case "deadline":
                return parseDeadlineCommand(parts);
            case "event":
                return parseEventCommand(parts);
            case "delete":
                return new DeleteCommand(parseTaskNumberArgument(parts,
                        "Please provide a task number to delete."));
            case "on":
                return new FindOnDateCommand(parseQueryDate(parts));
            case "find":
                return new FindCommand(parseFindQuery(parts));
            default:
                throw new ThaisBotException(
                        "I'm sorry, but I don't know what that means :( . Please try again!");
        }
    }

    /**
     * Parses a task number text into an integer.
     * @param taskNumberText the string containing the task number
     * @return parsed integer task number
     * @throws ThaisBotException if the text is not a valid integer
     */
    public int parseTaskNumber(String taskNumberText) throws ThaisBotException {
        try {
            return Integer.parseInt(taskNumberText);
        } catch (NumberFormatException e) {
            throw new ThaisBotException("Task number must be a valid integer.");
        }
    }

    /**
     * Parses a date string in yyyy-MM-dd format.
     * @param input date string
     * @return LocalDate parsed from input
     * @throws ThaisBotException if parsing fails
     */
    public LocalDate parseDate(String input) throws ThaisBotException {
        try {
            return LocalDate.parse(input, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ThaisBotException("Date must use yyyy-MM-dd format.");
        }
    }

    /**
     * Attempts to parse a date/time string using several supported formats. Returns a
     * ParsedDateTime which contains the parsed LocalDateTime and whether the original
     * input had a time component.
     * @param input input string
     * @param errorMessage message to include in exception on failure
     * @return ParsedDateTime with parsed value
     * @throws ThaisBotException if none of the supported formats apply
     */
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

    /**
     * Parses the two parts of a deadline command separated by " /by ".
     */
    public String[] parseDeadlineParts(String input, String usageMessage) throws ThaisBotException {
        String[] parts = input.split(" /by ", 2);
        if (parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new ThaisBotException(usageMessage);
        }
        return parts;
    }

    /**
     * Parses an event command into description, from and to parts.
     */
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

    /**
     * Helper to parse an integer task number argument from the command parts.
     */
    private int parseTaskNumberArgument(String[] parts, String missingNumberMessage)
            throws ThaisBotException {
        if (parts.length < 2) {
            throw new ThaisBotException(missingNumberMessage);
        }
        return parseTaskNumber(parts[1]);
    }

    /**
     * Helper to parse a todo description from command parts.
     */
    private String parseTodoDescription(String[] parts) throws ThaisBotException {
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new ThaisBotException("The description of a todo cannot be empty.");
        }
        return parts[1].trim();
    }

    private Command parseDeadlineCommand(String[] parts) throws ThaisBotException {
        if (parts.length < 2) {
            throw new ThaisBotException(
                    "Use: deadline <description> /by <yyyy-MM-dd or yyyy-MM-dd HHmm>");
        }
        String[] deadlineParts = parseDeadlineParts(parts[1],
                "Use: deadline <description> /by <yyyy-MM-dd or yyyy-MM-dd HHmm>");
        ParsedDateTime by = parseDateTime(deadlineParts[1].trim(),
                "Deadline date/time must be yyyy-MM-dd or yyyy-MM-dd HHmm.");
        return new AddDeadlineCommand(deadlineParts[0].trim(), by);
    }

    private Command parseEventCommand(String[] parts) throws ThaisBotException {
        if (parts.length < 2) {
            throw new ThaisBotException("Use: event <description> /from <start> /to <end>.");
        }
        String[] eventParts = parseEventParts(parts[1],
                "Use: event <description> /from <start> /to <end>.");
        ParsedDateTime from = parseDateTime(eventParts[1].trim(),
                "Event start must be yyyy-MM-dd or yyyy-MM-dd HHmm.");
        ParsedDateTime to = parseDateTime(eventParts[2].trim(),
                "Event end must be yyyy-MM-dd or yyyy-MM-dd HHmm.");
        if (to.getValue().isBefore(from.getValue())) {
            throw new ThaisBotException("Event end cannot be before event start.");
        }
        return new AddEventCommand(eventParts[0].trim(), from, to);
    }

    private LocalDate parseQueryDate(String[] parts) throws ThaisBotException {
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new ThaisBotException("Use: on <yyyy-MM-dd>");
        }
        return parseDate(parts[1].trim());
    }

    private String parseFindQuery(String[] parts) throws ThaisBotException {
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new ThaisBotException("Use: find <keyword>");
        }
        return parts[1].trim();
    }

    /**
     * Holder for a parsed date/time and whether the original input included a time component.
     */
    public static class ParsedDateTime {
        private final LocalDateTime value;
        private final boolean hasTime;

        /**
         * Creates a parsed date/time value.
         * @param value parsed LocalDateTime
         * @param hasTime true if the original input had a time component
         */
        public ParsedDateTime(LocalDateTime value, boolean hasTime) {
            this.value = value;
            this.hasTime = hasTime;
        }

        /**
         * Returns the parsed date/time.
         * @return parsed date/time
         */
        public LocalDateTime getValue() {
            return value;
        }

        /**
         * Returns whether the original input included a time component.
         * @return true if the original input included a time component
         */
        public boolean hasTime() {
            return hasTime;
        }
    }
}
