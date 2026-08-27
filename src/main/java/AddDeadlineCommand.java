public class AddDeadlineCommand extends Command {
    private final String description;
    private final Parser.ParsedDateTime by;

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
