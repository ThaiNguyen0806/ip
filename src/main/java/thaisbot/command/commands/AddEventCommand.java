package thaisbot.command.commands;

import thaisbot.Storage;
import thaisbot.ThaisBotException;
import thaisbot.Ui;
import thaisbot.command.Command;
import thaisbot.command.Parser;
import thaisbot.task.Event;
import thaisbot.task.Task;
import thaisbot.task.TaskList;

public class AddEventCommand extends Command {
    private final String description;
    private final Parser.ParsedDateTime from;
    private final Parser.ParsedDateTime to;

    public AddEventCommand(String description, Parser.ParsedDateTime from, Parser.ParsedDateTime to) {
        this.description = description;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ThaisBotException {
        Task task = new Event(description, from.getValue(), from.hasTime(), to.getValue(), to.hasTime());
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }
}
