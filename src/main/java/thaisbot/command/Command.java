package thaisbot.command;

import thaisbot.Storage;
import thaisbot.ThaisBotException;
import thaisbot.Ui;
import thaisbot.task.TaskList;

public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws ThaisBotException;

    public boolean isExit() {
        return false;
    }
}
