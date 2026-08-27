import java.time.LocalDate;

public class FindOnDateCommand extends Command {
    private final LocalDate date;

    public FindOnDateCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasksOnDate(tasks, date);
    }
}
