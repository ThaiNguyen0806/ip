package thaisbot.command.commands;

import thaisbot.Storage;
import thaisbot.ThaisBotException;
import thaisbot.Ui;
import thaisbot.command.Command;
import thaisbot.command.Parser;
import thaisbot.task.Deadline;
import thaisbot.task.Task;
import thaisbot.task.TaskList;

/**
 * Command to add a deadline task.
 */
public class AddDeadlineCommand extends Command {
    private final String description;
    private final Parser.ParsedDateTime by;

    /**
     * Construct an AddDeadlineCommand.
     * @param description description text
     * @param by parsed deadline date/time
     */
    public AddDeadlineCommand(String description, Parser.ParsedDateTime by) {
        this.description = description;
        this.by = by;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ThaisBotException {
        Task task = new Deadline(description, by.getValue(), by.hasTime());
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }
}
