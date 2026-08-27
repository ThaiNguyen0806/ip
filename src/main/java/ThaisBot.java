import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ThaisBot {
    private static final Path DATA_FILE = Paths.get("data", "tasks.txt");
    private static final DateTimeFormatter INPUT_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter INPUT_DATE_TIME_COLON_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String TASK_NUMBER_ERROR =
            "Task number must be a valid integer.";
    private static final String TASK_RANGE_ERROR = "Task number out of range.";

    public static void main(String[] args) {
        System.out.println("Hello! I'm Thai's Bot.");
        System.out.println("What can I do for you today :D?");

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks;
        try {
            tasks = loadTasks();
        } catch (ThaisBotException e) {
            System.out.println("Error: " + e.getMessage());
            tasks = new ArrayList<>();
        }

        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine().trim();

            try {
                String[] parts = userInput.split("\\s+", 2);
                String command = parts[0];

                if (command.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    break;
                } else if (command.equals("list")) {
                    printTaskList(tasks);
                } else if (command.equals("mark")) {
                    if (parts.length < 2) {
                        throw new ThaisBotException("Please provide a task number to mark.");
                    }
                    int taskNumber = parseTaskNumber(parts[1], tasks.size());
                    Task task = tasks.get(taskNumber - 1);
                    task.markAsDone();
                    saveTasks(tasks);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + task);
                } else if (command.equals("unmark")) {
                    if (parts.length < 2) {
                        throw new ThaisBotException("Please provide a task number to unmark.");
                    }
                    int taskNumber = parseTaskNumber(parts[1], tasks.size());
                    Task task = tasks.get(taskNumber - 1);
                    task.unmarkAsDone();
                    saveTasks(tasks);
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + task);
                } else if (command.equals("todo")) {
                    if (parts.length < 2 || parts[1].trim().isEmpty()) {
                        throw new ThaisBotException("The description of a todo cannot be empty.");
                    }
                    Task task = new Todo(parts[1]);
                    tasks.add(task);
                    saveTasks(tasks);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.equals("deadline")) {
                    if (parts.length < 2) {
                        throw new ThaisBotException(
                                "Use: deadline <description> /by <yyyy-MM-dd or yyyy-MM-dd HHmm>");
                    }
                    String[] deadlineParts = parts[1].split(" /by ", 2);
                    if (deadlineParts.length != 2
                            || deadlineParts[0].trim().isEmpty()
                            || deadlineParts[1].trim().isEmpty()) {
                        throw new ThaisBotException(
                                "Use: deadline <description> /by <yyyy-MM-dd or yyyy-MM-dd HHmm>");
                    }
                    ParsedDateTime by = parseDateTime(deadlineParts[1].trim(),
                            "Deadline date/time must be yyyy-MM-dd or yyyy-MM-dd HHmm.");
                    Task task = new Deadline(deadlineParts[0].trim(), by.value, by.hasTime);
                    tasks.add(task);
                    saveTasks(tasks);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.equals("event")) {
                    if (parts.length < 2) {
                        throw new ThaisBotException(
                                "Use: event <description> /from <start> /to <end>.");
                    }
                    String[] eventParts = parts[1].split(" /from | /to ", 3);
                    if (eventParts.length != 3
                            || eventParts[0].trim().isEmpty()
                            || eventParts[1].trim().isEmpty()
                            || eventParts[2].trim().isEmpty()) {
                        throw new ThaisBotException(
                                "Use: event <description> /from <start> /to <end>.");
                    }
                    ParsedDateTime from = parseDateTime(eventParts[1].trim(),
                            "Event start must be yyyy-MM-dd or yyyy-MM-dd HHmm.");
                    ParsedDateTime to = parseDateTime(eventParts[2].trim(),
                            "Event end must be yyyy-MM-dd or yyyy-MM-dd HHmm.");
                    if (to.value.isBefore(from.value)) {
                        throw new ThaisBotException("Event end cannot be before event start.");
                    }
                    Task task = new Event(eventParts[0].trim(), from.value, from.hasTime,
                            to.value, to.hasTime);
                    tasks.add(task);
                    saveTasks(tasks);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.equals("on")) {
                    if (parts.length < 2 || parts[1].trim().isEmpty()) {
                        throw new ThaisBotException("Use: on <yyyy-MM-dd>");
                    }
                    LocalDate date = parseDate(parts[1].trim());
                    printTasksOnDate(tasks, date);
                } else if (command.equals("delete")) {
                    if (parts.length < 2) {
                        throw new ThaisBotException("Please provide a task number to delete.");
                    }
                    int taskNumber = parseTaskNumber(parts[1], tasks.size());
                    Task removedTask = tasks.remove(taskNumber - 1);
                    saveTasks(tasks);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else {
                    throw new ThaisBotException(
                            "I'm sorry, but I don't know what that means :( . Please try again!");
                }
            } catch (ThaisBotException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void saveTasks(ArrayList<Task> tasks) throws ThaisBotException {
        try {
            Files.createDirectories(DATA_FILE.getParent());
            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(task.toFileString());
            }
            Files.write(DATA_FILE, lines);
        } catch (IOException e) {
            throw new ThaisBotException("I couldn't save your tasks to disk.");
        }
    }

    private static ArrayList<Task> loadTasks() throws ThaisBotException {
        try {
            Files.createDirectories(DATA_FILE.getParent());
            if (!Files.exists(DATA_FILE)) {
                Files.createFile(DATA_FILE);
                return new ArrayList<>();
            }
            List<String> lines = Files.readAllLines(DATA_FILE);
            ArrayList<Task> tasks = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) {
                    continue;
                }
                tasks.add(parseTaskLine(line, i + 1));
            }
            return tasks;
        } catch (IOException e) {
            throw new ThaisBotException("I couldn't load tasks from disk.");
        }
    }

    private static Task parseTaskLine(String line, int lineNumber) throws ThaisBotException {
        String[] parts = line.split("\\s*\\|\\s*", -1);
        if (parts.length < 3) {
            throw new ThaisBotException("Saved task data is corrupted at line " + lineNumber + ".");
        }
        String taskType = parts[0];
        String statusFlag = parts[1];
        String description = parts[2];
        Task task;

        if ("T".equals(taskType)) {
            task = new Todo(description);
        } else if ("D".equals(taskType)) {
            if (parts.length < 4) {
                throw new ThaisBotException("Saved task data is corrupted at line " + lineNumber + ".");
            }
            ParsedDateTime by;
            if (parts.length >= 5) {
                by = parseStoredDateTimeWithFlag(parts[3], parts[4], lineNumber);
            } else {
                by = parseDateTime(parts[3],
                        "Saved task data is corrupted at line " + lineNumber + ".");
            }
            task = new Deadline(description, by.value, by.hasTime);
        } else if ("E".equals(taskType)) {
            if (parts.length < 5) {
                throw new ThaisBotException("Saved task data is corrupted at line " + lineNumber + ".");
            }
            ParsedDateTime from;
            ParsedDateTime to;
            if (parts.length >= 7) {
                from = parseStoredDateTimeWithFlag(parts[3], parts[4], lineNumber);
                to = parseStoredDateTimeWithFlag(parts[5], parts[6], lineNumber);
            } else {
                from = parseDateTime(parts[3],
                        "Saved task data is corrupted at line " + lineNumber + ".");
                to = parseDateTime(parts[4],
                        "Saved task data is corrupted at line " + lineNumber + ".");
            }
            task = new Event(description, from.value, from.hasTime, to.value, to.hasTime);
        } else {
            throw new ThaisBotException("Saved task data is corrupted at line " + lineNumber + ".");
        }

        if ("1".equals(statusFlag)) {
            task.setStatus(TaskStatus.DONE);
        } else if ("0".equals(statusFlag)) {
            task.setStatus(TaskStatus.NOT_DONE);
        } else {
            throw new ThaisBotException("Saved task data is corrupted at line " + lineNumber + ".");
        }

        return task;
    }

    private static void printTaskList(ArrayList<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    private static void printTasksOnDate(ArrayList<Task> tasks, LocalDate date) {
        System.out.println("Here are the deadlines and events on " + date + ":");
        int shownCount = 0;
        for (Task task : tasks) {
            if (task.occursOn(date)) {
                shownCount++;
                System.out.println(shownCount + "." + task);
            }
        }
        if (shownCount == 0) {
            System.out.println("No deadlines or events found on that date.");
        }
    }

    private static int parseTaskNumber(String taskNumberText, int taskCount)
            throws ThaisBotException {
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new ThaisBotException(TASK_RANGE_ERROR);
            }
            return taskNumber;
        } catch (NumberFormatException e) {
            throw new ThaisBotException(TASK_NUMBER_ERROR);
        }
    }

    private static LocalDate parseDate(String input) throws ThaisBotException {
        try {
            return LocalDate.parse(input, INPUT_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ThaisBotException("Date must use yyyy-MM-dd format.");
        }
    }

    private static ParsedDateTime parseDateTime(String input, String errorMessage)
            throws ThaisBotException {
        try {
            LocalDateTime value = LocalDateTime.parse(input, INPUT_DATE_TIME_FORMATTER);
            return new ParsedDateTime(value, true);
        } catch (DateTimeParseException ignored) {
            // Try next format.
        }

        try {
            LocalDateTime value = LocalDateTime.parse(input, INPUT_DATE_TIME_COLON_FORMATTER);
            return new ParsedDateTime(value, true);
        } catch (DateTimeParseException ignored) {
            // Try date-only format.
        }

        try {
            LocalDate date = LocalDate.parse(input, INPUT_DATE_FORMATTER);
            return new ParsedDateTime(date.atStartOfDay(), false);
        } catch (DateTimeParseException e) {
            throw new ThaisBotException(errorMessage);
        }
    }

    private static ParsedDateTime parseStoredDateTimeWithFlag(String value, String flag,
                                                              int lineNumber)
            throws ThaisBotException {
        try {
            LocalDateTime parsed = LocalDateTime.parse(value);
            if ("1".equals(flag)) {
                return new ParsedDateTime(parsed, true);
            } else if ("0".equals(flag)) {
                return new ParsedDateTime(parsed, false);
            }
        } catch (DateTimeParseException ignored) {
            // Fallthrough to uniform error.
        }
        throw new ThaisBotException("Saved task data is corrupted at line " + lineNumber + ".");
    }

    private static class ParsedDateTime {
        private final LocalDateTime value;
        private final boolean hasTime;

        private ParsedDateTime(LocalDateTime value, boolean hasTime) {
            this.value = value;
            this.hasTime = hasTime;
        }
    }
}
