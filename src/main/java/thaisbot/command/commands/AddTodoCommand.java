package thaisbot.command.commands;

import thaisbot.Storage;
import thaisbot.ThaisBotException;
import thaisbot.Ui;
import thaisbot.command.Command;
import thaisbot.task.Task;
import thaisbot.task.TaskList;
import thaisbot.task.Todo;

/**
 * Command to add a todo task.
 */
public class AddTodoCommand extends Command {
    private final String description;

    /**
     * Construct an AddTodoCommand.
     * @param description description of the todo
     */
    public AddTodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ThaisBotException {
        Task task = new Todo(description);
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }
}
