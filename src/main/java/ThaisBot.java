import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class ThaisBot {
    private static final Path DATA_FILE = Paths.get("data", "tasks.txt");

    public static void main(String[] args) {
        Parser parser = new Parser();
        Storage storage = new Storage(DATA_FILE.toString(), parser);
        Ui ui = new Ui();
        ui.showWelcome();

        TaskList tasks;
        try {
            tasks = storage.load();
        } catch (ThaisBotException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList();
        }

        while (true) {
            String userInput = ui.readCommand();

            try {
                String[] parts = parser.splitUserInput(userInput);
                String command = parts[0];

                if (command.equals("bye")) {
                    ui.showBye();
                    break;
                } else if (command.equals("list")) {
                    ui.showTaskList(tasks);
                } else if (command.equals("mark")) {
                    if (parts.length < 2) {
                        throw new ThaisBotException("Please provide a task number to mark.");
                    }
                    int taskNumber = parser.parseTaskNumber(parts[1], tasks.size());
                    Task task = tasks.get(taskNumber - 1);
                    task.markAsDone();
                    storage.save(tasks);
                    ui.showTaskMarkedDone(task);
                } else if (command.equals("unmark")) {
                    if (parts.length < 2) {
                        throw new ThaisBotException("Please provide a task number to unmark.");
                    }
                    int taskNumber = parser.parseTaskNumber(parts[1], tasks.size());
                    Task task = tasks.get(taskNumber - 1);
                    task.unmarkAsDone();
                    storage.save(tasks);
                    ui.showTaskMarkedNotDone(task);
                } else if (command.equals("todo")) {
                    if (parts.length < 2 || parts[1].trim().isEmpty()) {
                        throw new ThaisBotException("The description of a todo cannot be empty.");
                    }
                    Task task = new Todo(parts[1]);
                    tasks.add(task);
                    storage.save(tasks);
                    ui.showTaskAdded(task, tasks.size());
                } else if (command.equals("deadline")) {
                    if (parts.length < 2) {
                        throw new ThaisBotException(
                                "Use: deadline <description> /by <yyyy-MM-dd or yyyy-MM-dd HHmm>");
                    }
                    String[] deadlineParts = parser.parseDeadlineParts(parts[1],
                            "Use: deadline <description> /by <yyyy-MM-dd or yyyy-MM-dd HHmm>");
                    Parser.ParsedDateTime by = parser.parseDateTime(deadlineParts[1].trim(),
                            "Deadline date/time must be yyyy-MM-dd or yyyy-MM-dd HHmm.");
                    Task task = new Deadline(deadlineParts[0].trim(), by.getValue(), by.hasTime());
                    tasks.add(task);
                    storage.save(tasks);
                    ui.showTaskAdded(task, tasks.size());
                } else if (command.equals("event")) {
                    if (parts.length < 2) {
                        throw new ThaisBotException(
                                "Use: event <description> /from <start> /to <end>.");
                    }
                    String[] eventParts = parser.parseEventParts(parts[1],
                            "Use: event <description> /from <start> /to <end>.");
                    Parser.ParsedDateTime from = parser.parseDateTime(eventParts[1].trim(),
                            "Event start must be yyyy-MM-dd or yyyy-MM-dd HHmm.");
                    Parser.ParsedDateTime to = parser.parseDateTime(eventParts[2].trim(),
                            "Event end must be yyyy-MM-dd or yyyy-MM-dd HHmm.");
                    if (to.getValue().isBefore(from.getValue())) {
                        throw new ThaisBotException("Event end cannot be before event start.");
                    }
                    Task task = new Event(eventParts[0].trim(), from.getValue(), from.hasTime(),
                            to.getValue(), to.hasTime());
                    tasks.add(task);
                    storage.save(tasks);
                    ui.showTaskAdded(task, tasks.size());
                } else if (command.equals("on")) {
                    if (parts.length < 2 || parts[1].trim().isEmpty()) {
                        throw new ThaisBotException("Use: on <yyyy-MM-dd>");
                    }
                    LocalDate date = parser.parseDate(parts[1].trim());
                    ui.showTasksOnDate(tasks, date);
                } else if (command.equals("delete")) {
                    if (parts.length < 2) {
                        throw new ThaisBotException("Please provide a task number to delete.");
                    }
                    int taskNumber = parser.parseTaskNumber(parts[1], tasks.size());
                    Task removedTask = tasks.remove(taskNumber - 1);
                    storage.save(tasks);
                    ui.showTaskRemoved(removedTask, tasks.size());
                } else {
                    throw new ThaisBotException(
                            "I'm sorry, but I don't know what that means :( . Please try again!");
                }
            } catch (ThaisBotException e) {
                ui.showError(e.getMessage());
            }
        }
    }

}
