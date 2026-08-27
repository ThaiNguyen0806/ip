public class MarkCommand extends Command {
    private final int taskNumber;

    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws ThaisBotException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new ThaisBotException("Task number out of range.");
        }
        Task task = tasks.get(taskNumber - 1);
        task.markAsDone();
        storage.save(tasks);
        ui.showTaskMarkedDone(task);
    }
}
