package thaisbot.command.commands;

import java.time.LocalDate;

import thaisbot.Storage;
import thaisbot.ThaisBotException;
import thaisbot.Ui;
import thaisbot.command.Command;
import thaisbot.task.TaskList;

public class FindOnDateCommand extends Command {
    private final LocalDate date;

    public FindOnDateCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ThaisBotException {
        ui.showTasksOnDate(tasks, date);
    }
}
